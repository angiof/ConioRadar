package com.conio.conioradar.network.responses

data class MarketChartResponse(
    val prices: List<List<Double>>,
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
)
