package com.h2appi.conioradar.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.h2appi.conioradar.databinding.FragmentHomeBinding
import com.h2appi.conioradar.states.CoinIntent
import com.h2appi.conioradar.states.CoinState
import com.h2appi.conioradar.ui.activities.ConioDetailActivity
import com.h2appi.conioradar.ui.adapter.CoinsAdapter
import com.h2appi.conioradar.ui.utils.UtilsBundles.COIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: CoinViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapterView = CoinsAdapter { coin ->
            Intent(
                binding.root.context,
                ConioDetailActivity::class.java
            ).apply {
                this.putExtra(COIN, coin)
                startActivity(this)
            }

        }


        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CoinState.Loading -> {
                    binding.loadingAnimation.visibility = View.VISIBLE
                    binding.mCardview.visibility = View.GONE
                }

                is CoinState.Success -> {
                    binding.mCardview.visibility = View.VISIBLE

                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            binding.recyCoin.apply {
                                this.adapter = adapterView
                                binding.loadingAnimation.visibility = View.GONE
                                adapterView.submitList(state.coins)
                                this.setHasFixedSize(true)
                            }
                        }
                    }

                }

                is CoinState.Error -> {
                    // Mostra errore
                    Toast.makeText(
                        binding.root.context,
                        state.message+"hai superato il limite di richieste https",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.processIntent(CoinIntent.LoadTopCoins)
        return root

    }

}


