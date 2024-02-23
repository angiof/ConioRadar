package com.conio.conioradar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.h2appi.conioradar.databinding.ListItmesBinding
import com.conio.conioradar.network.responses.CoinMarketData

class CoinsAdapter(private val onItemClicked: (CoinMarketData) -> Unit) :
    ListAdapter<CoinMarketData, CoinViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItmesBinding.inflate(layoutInflater, parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin)
        holder.itemView.setOnClickListener { onItemClicked(coin) }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<CoinMarketData>() {
        override fun areItemsTheSame(oldItem: CoinMarketData, newItem: CoinMarketData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CoinMarketData, newItem: CoinMarketData): Boolean {
            return oldItem.id == newItem.id
        }
    }


}
