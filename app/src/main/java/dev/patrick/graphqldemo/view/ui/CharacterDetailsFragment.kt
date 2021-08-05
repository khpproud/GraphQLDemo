package dev.patrick.graphqldemo.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.patrick.graphqldemo.databinding.FragmentCharacterDetailsBinding
import dev.patrick.graphqldemo.view.state.ViewState
import dev.patrick.graphqldemo.viewmodel.CharacterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding: FragmentCharacterDetailsBinding
        get() = _binding!!
    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val viewModel: CharacterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.queryCharacter(args.id)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.character.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    binding.progressFetch.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.GONE
                }
                is ViewState.Success -> {
                    if (response.value?.data?.character == null) {
                        binding.progressFetch.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    } else {
                        binding.query = response.value.data
                        binding.progressFetch.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                    }
                }
                is ViewState.Error -> {
                    binding.progressFetch.visibility = View.GONE
                    binding.tvNotFound.visibility = View.VISIBLE
                }
            }
        }
    }

}