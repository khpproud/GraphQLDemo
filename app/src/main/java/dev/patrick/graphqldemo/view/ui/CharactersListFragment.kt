package dev.patrick.graphqldemo.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.patrick.graphqldemo.databinding.FragmentCharactersListBinding
import dev.patrick.graphqldemo.view.adapter.CharacterAdapter
import dev.patrick.graphqldemo.view.state.ViewState
import dev.patrick.graphqldemo.viewmodel.CharacterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharactersListFragment : Fragment() {

    private var _binding: FragmentCharactersListBinding? = null
    private val binding: FragmentCharactersListBinding
        get() = _binding!!

    private val characterAdapter by lazy { CharacterAdapter() }

    private val viewModel by viewModels<CharacterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCharacters.adapter = characterAdapter
        viewModel.queryCharactersList()
        observeLiveData()
        characterAdapter.setOnItemClicked { character ->
            if (!character.id.isNullOrBlank()) {
                findNavController().navigate(
                    CharactersListFragmentDirections.toCharacterDetailsFragment(
                        id = character.id
                    )
                )
            }
        }
    }




    private fun observeLiveData() {
        viewModel.charactersList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    binding.rvCharacters.visibility = View.GONE
                    binding.progressFetch.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    if (response.value?.data?.characters?.results?.size == 0) {
                        characterAdapter.submitList(emptyList())
                        binding.progressFetch.visibility = View.GONE
                        binding.rvCharacters.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rvCharacters.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = View.GONE
                    }
                    val results = response.value?.data?.characters?.results
                    characterAdapter.submitList(results)
                    binding.progressFetch.visibility = View.GONE
                }
                is ViewState.Error -> {
                    characterAdapter.submitList(emptyList())
                    binding.progressFetch.visibility = View.GONE
                    binding.rvCharacters.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}