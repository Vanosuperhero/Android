package com.example.test

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

//@BindingAdapter("app:next")
//fun Next(index, listofgif, GetMarsRealEstateProperties){
////        Здесь нужно вытаскивать гиф из списка и класть в лайвдату
//    if (index == listofgif.lastIndex){ GetMarsRealEstateProperties()}
////        index++
////        _property.value = listofgif[index]
//}
//@BindingAdapter("app:prev")
//fun Prev(){
//    if (index > 0){
//        index--
//        _property.value = listofgif[index]
//    }
//}

