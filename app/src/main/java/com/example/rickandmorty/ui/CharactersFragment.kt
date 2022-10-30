package com.example.rickandmorty.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rickandmorty.R
import com.example.rickandmorty.data.Character
import com.example.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters),
    CharacterAdapter.OnItemClickListener {

    private var binding: FragmentCharactersBinding? = null
    private val viewModel by viewModels<CharactersViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharactersBinding.bind(view)
        val adapter = CharacterAdapter(this)

        binding?.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter =
                adapter.withLoadStateHeaderAndFooter(header = CharacterLoadStateAdapter { adapter.retry() },
                    footer = CharacterLoadStateAdapter { adapter.retry() })
            btRetry.setOnClickListener {
                adapter.retry()
            }
        }
        viewModel.characters.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


        //swipe to refresh
        val swipe: SwipeRefreshLayout? = binding?.swipeToRefresh
        swipe?.setOnRefreshListener {
            adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            viewModel.refreshedCharacters.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
            swipe.isRefreshing = false
        }

        adapter.addLoadStateListener { loadState ->
            binding?.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading

                //when there's no result for the query
                val errorState = when {
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    else -> null
                }
                tvNoResult.isVisible=false
                errorState?.let {
                    if (errorState.error.localizedMessage?.trim() == "HTTP 404"){
                        recyclerView.isVisible=false
                        btRetry.isVisible=false
                        tvError.isVisible=false
                        tvNoResult.isVisible=true
                    }else{
                        //when there's no internet connection
                        tvNoResult.isVisible=false
                        btRetry.isVisible = loadState.source.refresh is LoadState.Error
                        tvError.isVisible = loadState.source.refresh is LoadState.Error
                    }
                }
                //Log.i("error message", errorState?.error?.localizedMessage.toString())
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_character, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

                searchView.setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        binding?.recyclerView?.scrollToPosition(0)
                        Log.i("query", query)
                        viewModel.searchCharacterByName(query)
                        Log.i("query2", "happened")
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.status_all -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByStatus("")
                        }
                        true
                    }
                    R.id.status_alive -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByStatus("alive")
                        } else {
                            viewModel.searchCharacterByStatus("")
                        }
                        true
                    }
                    R.id.status_dead -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByStatus("dead")
                        } else {
                            viewModel.searchCharacterByStatus("")
                        }
                        true
                    }
                    R.id.status_unknown -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByStatus("unknown")
                        } else {
                            viewModel.searchCharacterByStatus("")
                        }
                        true
                    }
                    R.id.gender_all -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByGender("")
                        }
                        true
                    }
                    R.id.gender_male -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByGender("male")
                        } else {
                            viewModel.searchCharacterByGender("")
                        }
                        true
                    }
                    R.id.gender_female -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByGender("female")
                        } else {
                            viewModel.searchCharacterByGender("")
                        }
                        true
                    }
                    R.id.gender_genderless -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByGender("genderless")
                        } else {
                            viewModel.searchCharacterByGender("")
                        }
                        true
                    }
                    R.id.gender_unknown -> {
                        menuItem.isChecked = !menuItem.isChecked
                        if (menuItem.isChecked) {
                            viewModel.searchCharacterByGender("unknown")
                        } else {
                            viewModel.searchCharacterByGender("")
                        }
                        true
                    }
                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    override fun onItemClick(character: Character) {
        val action =
            CharactersFragmentDirections.actionCharactersFragmentToDetailsFragment(character)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}