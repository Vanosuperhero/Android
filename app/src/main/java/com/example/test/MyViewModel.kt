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

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log

import kotlinx.coroutines.withContext

val TAG = "C-Manager"


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
        _currentFilter.value = MyApiFilter.RANDOM
        getDataRandom()

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
                _property.value = MyProperty("Ошибка: ${e.message}","")
                _status.value = MyApiStatus.ERROR
//                _errors.value = "Failure: ${e.message}"
            }
        }
    }

    private fun getDataRandom(){
        coroutineScope.launch {
            val getRandomDeferred = MyApiRandom.retrofitService.getProperties()
            try {
//                _status.value = MyApiStatus.LOADING
                val listResult = getRandomDeferred.await()
//                _status.value = MyApiStatus.DONE
//                  кладем гиф в список
                listofgif.add(listResult)
                _property.value = listofgif[index]


            }catch (e:Exception) {
                _property.value = MyProperty("Ошибка: ${e.message}","")

//                _status.value = MyApiStatus.ERROR
//                _errors.value = "Failure: ${e.localizedMessage}"
            }
        }
    }




    fun next(){
//      вытаскиваем гиф из списка и кладем в лайвдату
        if (index == listofgif.lastIndex) {
            index++
            page++
            _errors.value = ""
            if (currentFilter.value == MyApiFilter.RANDOM ){getDataRandom()}
            else {getDataProperties(currentFilter.value?:MyApiFilter.LATEST, page)}
        }
        else {
            index++
            try {
                _property.value = listofgif[index]
            }catch (e:Exception){
                _property.value = MyProperty("Ошибка: ${e.message}","")
                index = 0
                listofgif.clear()
                if (currentFilter.value == MyApiFilter.RANDOM ){getDataRandom()}
                else {getDataProperties(currentFilter.value?:MyApiFilter.LATEST, page)}
            }
        }
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
//class ConnectionLiveData(context: Context) : LiveData<Boolean>() {
//
//
//    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
//    private val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//    private val validNetworks: MutableSet<Network> = HashSet()
//
//    private fun checkValidNetworks() {
//        postValue(validNetworks.size > 0)
//    }
//
//    override fun onActive() {
//        networkCallback = createNetworkCallback()
//        val networkRequest = NetworkRequest.Builder()
//            .addCapability(NET_CAPABILITY_INTERNET)
//            .build()
//        cm.registerNetworkCallback(networkRequest, networkCallback)
//    }
//
//    override fun onInactive() {
//        cm.unregisterNetworkCallback(networkCallback)
//    }
//
//    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
//
//        /*
//          Called when a network is detected. If that network has internet, save it in the Set.
//          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)
//         */
//        override fun onAvailable(network: Network) {
//            Log.d(TAG, "onAvailable: ${network}")
//            val networkCapabilities = cm.getNetworkCapabilities(network)
//            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
//            Log.d(TAG, "onAvailable: ${network}, $hasInternetCapability")
//            if (hasInternetCapability == true) {
//                // check if this network actually has internet
//                CoroutineScope(Dispatchers.IO).launch {
//                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
//                    if(hasInternet){
//                        withContext(Dispatchers.Main){
//                            Log.d(TAG, "onAvailable: adding network. ${network}")
//                            validNetworks.add(network)
//                            checkValidNetworks()
//                        }
//                    }
//                }
//            }
//        }
//
//        /*
//          If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
//          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
//         */
//        override fun onLost(network: Network) {
//            Log.d(TAG, "onLost: ${network}")
//            validNetworks.remove(network)
//            checkValidNetworks()
//        }
//
//    }
//
//}