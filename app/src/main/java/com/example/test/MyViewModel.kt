package com.example.test

import android.app.Application
import android.media.Image
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
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
enum class MyApiStatus{LOADING,ERROR,DONE}
class MyViewModel() :ViewModel(){

    private val _status = MutableLiveData<MyApiStatus>()
    val status : LiveData<MyApiStatus>
    get() = _status

    private val _truestatus = MutableLiveData<String>()
    val truestatus : LiveData<String>
        get() = _truestatus

    private val _property = MutableLiveData<MyProperty>()
    val property: LiveData<MyProperty>
    get() = _property


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    val listofgif = mutableListOf<MyProperty>()
    var index = 0

    init {
        GetDataProperties()

    }
    private fun GetDataProperties(){
        coroutineScope.launch {
           var getPropertiesDeferred = MyApi.retrofitService.getProperties()
            try {
                _status.value = MyApiStatus.LOADING
                val listResult = getPropertiesDeferred.await()
                _status.value = MyApiStatus.DONE
//                if(listResult.size>0) {
//                    _property.value = listResult
//                }
//                  кладем гиф в список
//                listofgif.add(listResult)
//                index++
//                _property.value = listofgif[index]


                    //если пришел список
                for (i in listResult.result){
                    listofgif.add(i)
                }
                _property.value = listofgif[index]

                }catch (e:Exception) {
                _status.value = MyApiStatus.ERROR
//                _property.value =
                _truestatus.value = "Failure: ${e.message}"
            }
        }
    }

    fun next(){
//      вытаскиваем гиф из списка и кладем в лайвдату
        if (index == listofgif.lastIndex) {
            index++
            GetDataProperties()

        }
        else { index++
        _property.value = listofgif[index]}
    }


    fun prev(){
        if (index > 0){
        index--
        _property.value = listofgif[index]
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}