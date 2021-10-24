package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.ActivityMainBinding

import android.widget.Button
import android.widget.ImageView


class MainActivity : AppCompatActivity() {


     lateinit var myViewModel: MyViewModel



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

//     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//         inflater.inflate(R.menu.overflow_menu, menu)
//         super.onCreateOptionsMenu(menu, inflater)
//     }
     override fun onCreateOptionsMenu(menu: Menu): Boolean {
         val inflater: MenuInflater = menuInflater
         inflater.inflate(R.menu.overflow_menu, menu)
         return true
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         myViewModel.updateFilter(
             when(item.itemId) {
                 R.id.show_latest_menu -> MyApiFilter.LATEST
                 R.id.show_top_menu -> MyApiFilter.TOP
                 R.id.show_hot_menu -> MyApiFilter.HOT
                 R.id.show_random_menu -> MyApiFilter.RANDOM
                 else -> MyApiFilter.RANDOM
             }
         )
         return true
     }
 }

