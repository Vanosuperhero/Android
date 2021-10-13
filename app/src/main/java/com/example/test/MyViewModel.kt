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
enum class MyApiFilter(val value:String){LATEST("latest"),TOP("top"),HOT("hot"),RANDOM("")}
class MyViewModel() :ViewModel(){

    private val _status = MutableLiveData<MyApiStatus>()
    val status : LiveData<MyApiStatus>
    get() = _status

    private val _errors = MutableLiveData<String>()
    val errors : LiveData<String>
        get() = _errors

    private val _currentFilter = MutableLiveData<MyApiFilter>()
    val currentFilter : LiveData<MyApiFilter>
        get() = _currentFilter

    private val _property = MutableLiveData<MyProperty>()
    val property: LiveData<MyProperty>
    get() = _property


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Main)



    val listofgif = mutableListOf<MyProperty>()
    var index = 0
    var page = 0
    init {
        _currentFilter.value = MyApiFilter.LATEST
        getDataProperties(MyApiFilter.LATEST, page)

    }

    private fun getDataProperties(filter: MyApiFilter, page: Int){
        coroutineScope.launch {
           val getPropertiesDeferred = MyApi.retrofitService.getProperties(filter.value, page)
            try {
                _status.value = MyApiStatus.LOADING
                val listResult = getPropertiesDeferred.await()
                _status.value = MyApiStatus.DONE
//                if(listResult.size>0) {
//                    _property.value = listResult
//                }
                    //если пришел список
                for (i in listResult.result){
                    listofgif.add(i)
                }
                _property.value = listofgif[index]

                }catch (e:Exception) {
                _property.value = MyProperty("","")
                _status.value = MyApiStatus.ERROR
                _errors.value = "Failure: ${e.message}"
            }
        }
    }

    private fun getDataRandom(){
        coroutineScope.launch {
            val getRandomDeferred = MyApiRandom.retrofitService.getProperties()
            try {
                _status.value = MyApiStatus.LOADING
                val listResult = getRandomDeferred.await()
                _status.value = MyApiStatus.DONE
//                  кладем гиф в список
                listofgif.add(listResult)
                _property.value = listofgif[index]


            }catch (e:Exception) {
                _property.value = MyProperty("","")
                _status.value = MyApiStatus.ERROR
                _errors.value = "Failure: ${e.message}"
            }
        }
    }




    fun next(){
//      вытаскиваем гиф из списка и кладем в лайвдату
        if (index == listofgif.lastIndex) {
            index++
            page++
            _errors.value = ""
            if (currentFilter.value == MyApiFilter.RANDOM) {getDataRandom()}
            else {getDataProperties(currentFilter.value?:MyApiFilter.LATEST, page)}
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

    fun updateFilter(filter: MyApiFilter){
        listofgif.clear()
        _errors.value = ""
        _currentFilter.value = filter
        index = 0
        page = 0
        if (filter == MyApiFilter.RANDOM) {getDataRandom()}
        else {getDataProperties(filter,0)}

    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}