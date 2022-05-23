package com.project.ibook

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.bumptech.glide.Glide
import com.project.ibook.databinding.ActivitySplashScreenBinding
import com.project.ibook.search.SearchActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var binding: ActivitySplashScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val uri = intent.data

        Glide.with(this)
            .load(R.drawable.splash_new)
            .into(binding!!.splash)

        Handler().postDelayed({
            if(uri != null) {
                val params = uri.pathSegments
                val id = params[params.size - 1]
                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra(SearchActivity.NOVEL_ID_DEEPLINK, id)
                startActivity(intent)
            } else {
                startActivity(Intent(this, BottomNavigationActivity::class.java))
            }
            finish()
        }, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}