package com.h2appi.conioradar.network.responses

data class MarketChartResponse(
    val prices: List<List<Double>>,
    val market_caps: List<List<Double>>,
    val total_volumes: List<List<Double>>
)
