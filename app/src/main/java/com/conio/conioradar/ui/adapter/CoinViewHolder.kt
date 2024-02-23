package com.conio.conioradar.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.h2appi.conioradar.databinding.ListItmesBinding
import com.conio.conioradar.network.responses.CoinMarketData
import com.h2appi.conioradar.ui.utils.UtilsParams
import java.text.NumberFormat
import java.util.Currency

class CoinViewHolder(private val binding: ListItmesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(coin: CoinMarketData) {

        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(UtilsParams.EUR)

        val formattedPrice = format.format(coin.current_price)

        binding.textViewPrice.text = buildString {
            append("Rank ")
            append(coin.market_cap_rank.toString().uppercase())
        }
        binding.textViewName.text = coin.name
        binding.textViewVariation.text = formattedPrice


        binding.textViewSymbol.text = coin.symbol
        Glide.with(binding.imageViewLogo)
            .load(coin.image)
            .into(binding.imageViewLogo)
    }
}