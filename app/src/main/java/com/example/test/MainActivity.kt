 package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.GridView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.ActivityMainBinding

import android.widget.Button


 class MainActivity : AppCompatActivity() {

//     lateinit var textV: TextView
     lateinit var myViewModel: MyViewModel

//     private val liveData: MyViewModel by lazy {
//         ViewModelProvider(this).get(MyViewModel::class.java)
//     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//         val binding = GridViewItemBinding.inflate(inflater)
         myViewModel = ViewModelProvider(this)
             .get(MyViewModel::class.java)

         binding.lifecycleOwner = this
         binding.liveData =  myViewModel


    }

//    override fun onStart() {
//        textV = findViewById(R.id.textV)
//        super.onStart()
//        myViewModel.liveData.observe(this, Observer {
//            textV.text = it
//        })
//    }

}
