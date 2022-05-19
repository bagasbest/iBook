package com.project.ibook.ui.account.buy_coin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.ibook.databinding.ActivityBuyCoinDashboardBinding

class BuyCoinDashboardActivity : AppCompatActivity() {

    private var binding: ActivityBuyCoinDashboardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyCoinDashboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.coin18?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 18)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.12.000")
            startActivity(intent)
        }

        binding?.coin31?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 31)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.25.000")
            startActivity(intent)
        }

        binding?.coin62?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 62)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.50.000")
            startActivity(intent)
        }

        binding?.coin125?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 125)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.100.000")
            startActivity(intent)
        }

        binding?.coin250?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 250)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.200.000")
            startActivity(intent)
        }

        binding?.coin625?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 625)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.500.000")
            startActivity(intent)
        }

        binding?.coin1250?.setOnClickListener {
            val intent = Intent(this, BuyCoinCheckoutActivity::class.java)
            intent.putExtra(BuyCoinCheckoutActivity.COIN, 1250)
            intent.putExtra(BuyCoinCheckoutActivity.PRICE, "Rp.1.000.000")
            startActivity(intent)
        }

        binding?.transactionBtn?.setOnClickListener {
            startActivity(Intent(this, BuyCoinTransactionActivity::class.java))
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}