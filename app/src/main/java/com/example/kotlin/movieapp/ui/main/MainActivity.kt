package com.example.kotlin.movieapp.ui.main

import com.example.kotlin.movieapp.R
import com.example.kotlin.movieapp.databinding.ActivityMainBinding
import com.example.kotlin.movieapp.ext.app
import com.example.kotlin.movieapp.ui.base.BaseActivity

class MainActivity : BaseActivity<MainActivity, MainViewModel, MainViewModel.Factory, ActivityMainBinding, MainComponent>() {
    override val viewModelClass = MainViewModel::class.java
    override val layoutResId = R.layout.activity_main
    override val component by lazy {
        DaggerMainComponent
                .builder()
                .appComponent(app.component)
                .mainModule(MainModule())
                .build()
    }
}
