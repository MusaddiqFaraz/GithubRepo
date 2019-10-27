package com.example.repo.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.repo.R

fun ImageView.load(url: String, placeholder: Int = R.drawable.account_placeholder) {


    Glide.with(context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)


}