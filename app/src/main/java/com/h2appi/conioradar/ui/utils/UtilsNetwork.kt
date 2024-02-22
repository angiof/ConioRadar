package com.h2appi.conioradar.ui.utils

import android.os.Build
import android.os.Bundle
import java.io.Serializable

object UtilsNetwork {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"

}

object UtilsParams {
    const val EUR: String = "eur"
    const val ORDER: String = "market_cap_desc"
    const val PER_PAGE: Int = 1
    const val PAGE: Int = 1

}

object UtilsBundles {
    const val COIN: String = "COIN"

}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}