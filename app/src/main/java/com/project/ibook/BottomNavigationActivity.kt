package com.project.ibook

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.project.ibook.databinding.ActivityBottomNavigationBinding
import com.project.ibook.ui.account.AccountFragment
import com.project.ibook.ui.book.BookFragment
import com.project.ibook.ui.home.HomeFragment

class BottomNavigationActivity : AppCompatActivity() {

    private var binding: ActivityBottomNavigationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val navView = findViewById<ChipNavigationBar>(R.id.nav_view)

        navView.setItemSelected(R.id.navigation_home, true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment()).commit()


        bottomMenu(navView)
    }

    @SuppressLint("NonConstantResourceId")
    private fun bottomMenu(navView: ChipNavigationBar) {
        navView.setOnItemSelectedListener { i: Int ->
            var fragment: Fragment? = null
            when (i) {
                R.id.navigation_home -> fragment = HomeFragment()
                R.id.navigation_book -> fragment = BookFragment()
                R.id.navigation_account -> fragment = AccountFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    fragment!!
                ).commit()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}