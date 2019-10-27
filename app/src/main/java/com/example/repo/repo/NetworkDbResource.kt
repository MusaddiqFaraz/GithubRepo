package com.example.repo.repo

import android.util.Log
import androidx.lifecycle.*
import com.example.repo.githubapi.Resource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * The database serves as the single source of truth.
 * Therefore UI can receive data updates from database only.
 * Function notify UI about:
 * [Resource.Status.SUCCESS] - with data from database
 * [Resource.Status.ERROR] - if error has occurred from any source
 * [Resource.Status.LOADING] - to show loading state
 */
fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                          networkCall: suspend () -> Resource<A>,
                          saveCallResult: suspend (A) -> Unit,
                          shouldFetch: (T?) -> Boolean): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {

        emit(Resource.loading<T>())

        val dbSource = databaseQuery()
        val dbValue = dbSource.await()


        val source = databaseQuery().map {
            Resource.success(it)
        }

        emitSource(source)


        if(shouldFetch(dbValue)) {
            val responseStatus = networkCall()
            if (responseStatus.status == Resource.Status.SUCCESS) {
                saveCallResult(responseStatus.data!!)
            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error<T>(responseStatus.message!!))
                emitSource(source)
            }
        }
    }




private suspend fun <T> LiveData<T>.await() = withContext(Dispatchers.Main) {
    val receivedValue = CompletableDeferred<T?>()
    val observer = Observer<T> {
        if (receivedValue.isActive){
            receivedValue.complete(it)
        }
    }
    try {
        observeForever(observer)
        return@withContext receivedValue.await()
    } finally {
        removeObserver(observer)
    }
}