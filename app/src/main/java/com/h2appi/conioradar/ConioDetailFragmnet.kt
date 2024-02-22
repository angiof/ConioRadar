package com.h2appi.conioradar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.h2appi.conioradar.databinding.FragmentConioDetailFragmnetBinding
import com.h2appi.conioradar.mdoels.CoinMarketData
import com.h2appi.conioradar.mdoels.services.MarketChartResponse
import com.h2appi.conioradar.states.CoinStateDetail
import com.h2appi.conioradar.states.DetailIntent
import com.h2appi.conioradar.ui.home.CoinDetailViewModel
import com.h2appi.conioradar.ui.utils.UtilsBundles.COIN
import com.h2appi.conioradar.ui.utils.serializable
import java.security.KeyStore


class ConioDetailFragmnet : Fragment() {
    private var coinMarketData: CoinMarketData? = null
    private val viewModel: CoinDetailViewModel by viewModels()
    private lateinit var binding: FragmentConioDetailFragmnetBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            coinMarketData = it.serializable(COIN) as? CoinMarketData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConioDetailFragmnetBinding.inflate(layoutInflater, container, false)

        bindingUI()

        return binding.root
    }


    fun bindingUI() {
        binding.apply {
            binding.tvRank.text =
                getString(R.string.rank_detail, coinMarketData?.market_cap_rank.toString())
            binding.coinName.setText(coinMarketData?.name.toString())
            binding.tvCurrentPrize.text = coinMarketData?.current_price.toString()
            binding.tvSimbol.text = coinMarketData?.symbol.toString()
            Glide.with(binding.imageView.context).load(coinMarketData?.image).circleCrop()
                .into(binding.imageView)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val coinId = coinMarketData?.id ?: "defaultCoinId"
        viewModel.processIntent(DetailIntent.LoadCoinDetails(coinId = coinId))
        viewModel.processIntent(DetailIntent.LoadMarketChart(coinId, "EUR", "7", "daily"))



        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CoinStateDetail.Loading -> {
                    // Gestire il caricamento
                }
                is CoinStateDetail.SuccessMarketChart -> {
                    setupMarketChart(state.marketChart)
                }
                is CoinStateDetail.Error -> {
                    Toast.makeText(binding.root.context, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }



        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CoinStateDetail.Loading -> {
                    // Mostra animazione di caricamento

                }

                is CoinStateDetail.Success -> {
                    val coinDetailDescription = state.coins.description.en
                    Toast.makeText(binding.root.context, coinDetailDescription, Toast.LENGTH_SHORT)
                        .show()
                    binding.description.text = coinDetailDescription
                }

                is CoinStateDetail.Error -> {
                    Toast.makeText(binding.root.context, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }

    }


    private fun setupMarketChart(marketChartResponse: MarketChartResponse) {
        val entries = ArrayList<Entry>()

        marketChartResponse.prices.forEachIndexed { index, price ->
            val (timestamp, value) = price
            entries.add(Entry(timestamp.toFloat(), value.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Prezzo")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_200)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)
        binding.chart.data = lineData
        binding.chart.description.isEnabled = false
        binding.chart.invalidate()
    }

}