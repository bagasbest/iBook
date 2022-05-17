package com.project.ibook

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.project.ibook.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.splash)
            .into(binding!!.splash)

        Handler().postDelayed({
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
        }, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}