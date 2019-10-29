package com.example.repo.api

import org.mockito.ArgumentCaptor
import org.mockito.Mockito


/**
 * A kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

inline fun <reified T> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)