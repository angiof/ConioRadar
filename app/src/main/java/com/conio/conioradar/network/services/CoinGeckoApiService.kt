package com.conio.conioradar.network.services

import CoinDetail
import com.conio.conioradar.network.responses.CoinMarketData
import com.conio.conioradar.network.responses.MarketChartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinGeckoApiService {
    @GET("coins/markets")
    suspend fun getTopCoins(
        @Query("vs_currency") currency: String = "eur",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("locale") locale: String = "it"
    ): List<CoinMarketData>


    @GET("coins/{coin_id}")
    suspend fun getCoinData(
        @Path("coin_id") coinId: String,
        @Query("localization") localization: Boolean = false,
        @Query("tickers") tickers: Boolean = false,
        @Query("market_data") marketData: Boolean = true,
        @Query("community_data") communityData: Boolean = false,
        @Query("developer_data") developerData: Boolean = false,
        @Query("sparkline") sparkline: Boolean = false
    ): CoinDetail


    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") vsCurrency: String = "vs_currency",
        @Query("days") days: String = "7",
        @Query("interval") interval: String = "daily"
    ): Response<MarketChartResponse>

}
