package com.conio.conioradar.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conio.conioradar.network.repo.CoinRepository
import com.conio.conioradar.network.repo.Resource
import com.conio.conioradar.states.CoinStateDetail
import com.conio.conioradar.states.DetailIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CoinDetailViewModel : ViewModel() {
    private val repository: CoinRepository = CoinRepository()

    private val _intent = Channel<DetailIntent>(Channel.UNLIMITED)
    private val _state = MutableLiveData<CoinStateDetail>()
    val state: LiveData<CoinStateDetail> = _state

    init {

        handleIntents()
    }


    private fun handleIntents() {
        viewModelScope.launch {
            _intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is DetailIntent.LoadCoinDetails -> loadCoinDetails(intent.coinId)
                    is DetailIntent.LoadMarketChart -> loadMarketChart(
                        intent.coinId,
                        intent.vsCurrency,
                        intent.days,
                        intent.interval
                    )

                    else -> {}
                }
            }
        }
    }


    suspend fun loadMarketChart(
        coinId: String,
        vsCurrency: String,
        days: String,
        interval: String
    ) {
        _state.value = CoinStateDetail.Loading
        when (val response = repository.getMarketChart(coinId, vsCurrency, days, interval)) {
            is Resource.Success -> {
                _state.value =
                    CoinStateDetail.SuccessMarketChart(response.data) // Assicurati di avere questo stato definito
            }

            is Resource.Error -> {
                _state.value = CoinStateDetail.Error(response.message)
            }

            else -> {}
        }
    }


    suspend fun loadCoinDetails(id: String) {
        _state.value = CoinStateDetail.Loading
        if (id != null) {
            repository.getCoinDetails(id)
        }
        when (val response = id?.let { repository.getCoinDetails(it) }) {
            is Resource.Success -> {
                _state.value = CoinStateDetail.Success(response.data)
                Log.d("parito ok ", "onSucces")
            }

            is Resource.Error -> {
                Log.d("parito ok ", "error")
                _state.value = CoinStateDetail.Error(response.message)
            }

            else -> {}
        }

    }

    fun processIntent(intent: DetailIntent) {
        viewModelScope.launch { _intent.send(intent) }
    }


}