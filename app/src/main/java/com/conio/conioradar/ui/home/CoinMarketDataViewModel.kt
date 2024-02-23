package com.conio.conioradar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.conio.conioradar.network.repo.CoinRepository
import com.conio.conioradar.network.repo.Resource
import com.conio.conioradar.states.CoinIntent
import com.conio.conioradar.states.CoinState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CoinViewModel(
) : ViewModel() {
    private val repository: CoinRepository = CoinRepository()
    private val _intent = Channel<CoinIntent>(Channel.UNLIMITED)
    private val _state = MutableLiveData<CoinState>()
    val state: LiveData<CoinState> = _state

    init {
        handleIntents()

    }


    private fun handleIntents() {
        viewModelScope.launch {
            _intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is CoinIntent.LoadTopCoins -> loadTopCoins()
                    else -> {}
                }
            }
        }
    }

    private suspend fun loadTopCoins() {
        _state.value = CoinState.Loading
        when (val response =
            repository.getTopCoins(
                "eur",
                "market_cap_desc",
                10,
                1,
                false,
                "it"
            )) {
            is Resource.Success -> _state.value = CoinState.Success(response.data)
            is Resource.Error -> _state.value = CoinState.Error(response.message)
            else -> {
                //da impostare dopo
            }
        }
    }

    fun processIntent(intent: CoinIntent) {
        viewModelScope.launch { _intent.send(intent) }
    }
}
