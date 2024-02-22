package com.h2appi.conioradar.states

import CoinDetail
import com.h2appi.conioradar.mdoels.CoinMarketData
import com.h2appi.conioradar.mdoels.services.MarketChartResponse

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
