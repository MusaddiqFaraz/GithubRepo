package com.example.repo.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.layout_menu.view.*

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SubMenuPopUp (context: Context, view: View, val actionCallback : (String) -> Unit) : PopupWindow(context){

    companion object {
        val STARS = "STARS"
        val NAME = "NAME"
    }

    init {

        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT

        elevation = 10f
        contentView = view
        setBackgroundDrawable(null)


        isOutsideTouchable = true

        with(view) {
            sortByStars.setOnClickListener {
                actionCallback(STARS)
            }

            sortByName.setOnClickListener {
                actionCallback(NAME)
            }
        }
    }

}