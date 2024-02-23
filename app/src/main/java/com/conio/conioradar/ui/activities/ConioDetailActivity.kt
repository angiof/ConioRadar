package com.conio.conioradar.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.conio.conioradar.R
import com.conio.conioradar.ui.fragments.ConioDetailFragmnet
import com.conio.conioradar.network.responses.CoinMarketData
import com.conio.conioradar.ui.activities.ConioDetailActivity

import com.h2appi.conioradar.ui.utils.UtilsBundles.COIN

class ConioDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conio_detail)

        val coinMarketData = intent.getSerializableExtra(COIN) as? CoinMarketData

        if (savedInstanceState == null) {
            val bundle = Bundle().apply {
                putSerializable(COIN, coinMarketData)
            }

            val fragment = ConioDetailFragmnet().apply {
                arguments = bundle
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }
    }
}


