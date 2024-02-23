package com.conio.conioradar.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.h2appi.conioradar.R
import com.h2appi.conioradar.databinding.FragmentConioDetailFragmnetBinding
import com.conio.conioradar.network.responses.CoinMarketData
import com.conio.conioradar.network.responses.MarketChartResponse
import com.conio.conioradar.states.CoinStateDetail
import com.conio.conioradar.states.DetailIntent
import com.conio.conioradar.ui.home.CoinDetailViewModel
import com.h2appi.conioradar.ui.utils.UtilsBundles.COIN
import com.h2appi.conioradar.ui.utils.UtilsNetwork.DAILY
import com.h2appi.conioradar.ui.utils.UtilsNetwork.DAYS
import com.h2appi.conioradar.ui.utils.UtilsParams.EUR
import com.h2appi.conioradar.ui.utils.serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.shimmerViewContainer.stopShimmer()
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val coinId = coinMarketData?.id ?: "defaultCoinId"
        viewModel.processIntent(DetailIntent.LoadCoinDetails(coinId = coinId))
        viewModel.processIntent(
            DetailIntent.LoadMarketChart(
                coinId,
                EUR,
                DAYS,
                DAILY
            )
        )



        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CoinStateDetail.Loading -> {
                    binding.shimmerViewContainer.startShimmer()
                    binding.includeLayout.constraintMain.visibility = View.GONE


                }

                is CoinStateDetail.SuccessMarketChart -> {
                    setupMarketChart(state.marketChart)
                    binding.shimmerViewContainer.stopShimmer()
                    binding.includeLayout.constraintMain.visibility = View.VISIBLE

                }

                is CoinStateDetail.Error -> {
                    Toast.makeText(binding.root.context, state.message, Toast.LENGTH_SHORT).show()
                    binding.shimmerViewContainer.startShimmer()
                    binding.includeLayout.constraintMain.visibility = View.GONE

                }

                is CoinStateDetail.Success -> {
                    val rawDescription = state.coins.description.en
                    val decodedDescription = decodeUnicode(rawDescription)
                    val cleanDescription = stripHtmlTags(decodedDescription)
                    binding.includeLayout.description.text = cleanDescription
                    binding.tvWebPage.setOnClickListener {
                        openWebPage(state.coins.links.homepage[0])
                    }
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                }


                else -> {

                }
            }
        }


    }


    private fun setupMarketChart(marketChartResponse: MarketChartResponse) {
        val entries = ArrayList<Entry>()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val sevenDaysAgo = calendar.timeInMillis / 1000

        marketChartResponse.prices.forEach { price ->
            val (timestamp, value) = price
            if (timestamp >= sevenDaysAgo) {
                entries.add(Entry(timestamp.toFloat(), value.toFloat()))
            }
        }

        val dataSet = LineDataSet(entries, "Prize in the last 7 days")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.red)
        dataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        dataSet.setDrawCircles(true)
        dataSet.setDrawValues(true)

        val lineData = LineData(dataSet)
        binding.includeLayout.chart.data = lineData

        val xAxis = binding.includeLayout.chart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormatter = SimpleDateFormat("EEE", Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                val millis = value.toLong() * 1000
                return ""
            }
        }
        xAxis.setDrawLabels(true)
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        val leftAxis = binding.includeLayout.chart.axisLeft
        leftAxis.valueFormatter = object : ValueFormatter() {
            private val daysOfWeekShort = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            override fun getFormattedValue(value: Float): String {
                if (value < 0) return ""
                val dayIndex = value.toInt() % 7
                return daysOfWeekShort.getOrElse(dayIndex) { "" }
            }
        }

        leftAxis.textColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.includeLayout.chart.description.isEnabled = false

        binding.includeLayout.chart.axisRight.isEnabled = false

        binding.includeLayout.chart.xAxis.setDrawAxisLine(false)
        binding.includeLayout.chart.xAxis.setDrawGridLines(false)

        binding.includeLayout.chart.invalidate()

        binding.includeLayout.chart.animateXY(2000, 2000, Easing.EaseInElastic)
    }



    private fun decodeUnicode(string: String): String {
        val regex = """\\u([0-9a-fA-F]{4})""".toRegex()
        return regex.replace(string) {
            val charCode = it.groupValues[1].toInt(16)
            charCode.toChar().toString()
        }
    }

    private fun stripHtmlTags(html: String): String {
        return html.replace(Regex("<[^>]*>"), "")
    }

    private fun openWebPage(url: String?) {
        if (!url.isNullOrEmpty()) {
            val webpage: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No application can handle this request. Please install a web browser or check the URL.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "URL is not available", Toast.LENGTH_SHORT).show()
        }
    }


}
