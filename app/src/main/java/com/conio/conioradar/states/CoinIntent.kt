package com.conio.conioradar.states

sealed class CoinIntent {
    data object LoadTopCoins : CoinIntent()

}

sealed class DetailIntent {
    data class LoadCoinDetails(val coinId: String) : DetailIntent()
    data class LoadMarketChart(val coinId: String, val vsCurrency: String, val days: String, val interval: String) : DetailIntent()

}
