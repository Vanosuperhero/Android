package com.example.test

import android.app.Application
import android.media.Image
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.io.File

class MyViewModel() :ViewModel(){

    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
    get() = _status

    private val _property = MutableLiveData<MyProperty>()
    val property: LiveData<MyProperty>
    get() = _property

//    val URL = "https://developerslife.ru/random?json=true"
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        GetMarsRealEstateProperties()
//        getImages(application,liveData)
//        timer()
    }
    private fun GetMarsRealEstateProperties(){
        coroutineScope.launch {
           var getPropertiesDeferred = MyApi.retrofitService.getProperties()
            try {
                val listResult = getPropertiesDeferred.await()
//                if(listResult.size>0) {
                    _property.value = listResult

//                }
                }catch (e:Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


//    fun getImages(application: Application,liveData: MutableLiveData<ImageView>){
//        Glide.with(application)
//            .load(URL)
////            .apply(
////                RequestOptions()
////                    .placeholder(R.drawable.loading_animation)
////                    .error(R.drawable.ic_broken_image))
//            .into(liveData.value)
//    }
//    fun timer(){
//        object : CountDownTimer(5000, 1000) {
//
//            override fun onFinish() {
//                Toast.makeText(getApplication(), "Fuck $text", Toast.LENGTH_SHORT).show()
//            }
//            override fun onTick(p0: Long) {
//                liveData.value = (p0 / 1000).toString()
//            }
//        }.start()
//    }

}