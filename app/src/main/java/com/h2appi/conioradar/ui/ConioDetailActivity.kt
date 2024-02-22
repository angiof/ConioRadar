package com.h2appi.conioradar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.h2appi.conioradar.ConioDetailFragmnet
import com.h2appi.conioradar.R
import com.h2appi.conioradar.mdoels.CoinMarketData
import com.h2appi.conioradar.ui.utils.UtilsBundles.COIN

class ConioDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conio_detail)

        val coinMarketData = intent.getSerializableExtra("COIN") as? CoinMarketData

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


