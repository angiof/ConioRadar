package com.conio.conioradar.states

import CoinDetail
import com.conio.conioradar.network.responses.CoinMarketData
import com.conio.conioradar.network.responses.MarketChartResponse

sealed class CoinState {
    data object Loading : CoinState()
    data class Success(val coins: List<CoinMarketData>) : CoinState()
    data class Error(val message: String) : CoinState()
}

sealed class CoinStateDetail {
    data object Loading : CoinStateDetail()
    data class Success(val coins: CoinDetail) : CoinStateDetail()
    data class Error(val message: String) : CoinStateDetail()
    data class SuccessMarketChart(val marketChart: MarketChartResponse) : CoinStateDetail()


}
