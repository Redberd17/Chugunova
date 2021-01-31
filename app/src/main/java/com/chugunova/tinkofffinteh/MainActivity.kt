package com.chugunova.tinkofffinteh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chugunova.tinkofffinteh.mainpage.MainPageFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showMainPage()
    }

    private fun showMainPage() {
        val mainFragment = MainPageFragment()
        val fragmentTransaction =
            supportFragmentManager.beginTransaction().replace(R.id.main, mainFragment)
        fragmentTransaction.commit()
    }
}