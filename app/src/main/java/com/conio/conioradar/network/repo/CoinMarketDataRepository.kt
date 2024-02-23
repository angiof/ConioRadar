package com.conio.conioradar.network.repo

import CoinDetail
import com.conio.conioradar.network.responses.CoinMarketData
import com.conio.conioradar.network.client.NetworkModule
import com.conio.conioradar.network.responses.MarketChartResponse
import com.h2appi.conioradar.ui.utils.UtilsParams.EUR

class CoinRepository {
    private val apiService = NetworkModule.apiService


    suspend fun getTopCoins(
        currency: String,
        order: String,
        perPage: Int,
        page: Int,
        sparkline: Boolean,
        locale: String
    ): Resource<List<CoinMarketData>> {
        return try {
            val response = apiService.getTopCoins(
                currency,
                order,
                perPage,
                page,
                sparkline,
                locale
            )
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Unknown error")
        }
    }

    suspend fun getCoinDetails(coinId: String): Resource<CoinDetail> {
        return try {
            val response = apiService.getCoinData(coinId)
            Resource.Success(data = response)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Errore sconosciuto")
        }
    }


    suspend fun getMarketChart(
        coinId: String,
        vsCurrency: String = EUR,
        days: String = "7",
        interval: String = "daily"
    ): Resource<MarketChartResponse> {
        return try {
            val response = apiService.getMarketChart(coinId, vsCurrency, days, interval)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(data = response.body()!!)
            } else {
                Resource.Error(message = response.message() ?: "Errore sconosciuto")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Errore sconosciuto")
        }
    }


}

