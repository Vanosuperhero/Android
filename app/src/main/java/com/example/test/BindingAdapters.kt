package com.example.test

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        val imgUrl = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUrl)
//            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)
            .into(imgView)
    }
}

//@BindingAdapter("myApiStatus")
//fun bindStatus(statusImageView: ImageView, status: MyApiStatus?){
//    when(status){
////        MyApiStatus.LOADING -> {
//////            statusImageView.visibility = View.VISIBLE
//////            statusImageView.setImageResource(R.drawable.loading_animation)
////            statusImageView.visibility = View.GONE
////        }
//        MyApiStatus.ERROR -> {
//
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_connection_error)
//        }
//        MyApiStatus.DONE -> {
//            statusImageView.visibility = View.GONE
//        }
//    }
//}


