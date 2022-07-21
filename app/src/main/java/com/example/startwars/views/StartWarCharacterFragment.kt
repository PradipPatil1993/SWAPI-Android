package com.example.startwars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.startwars.R
import com.example.startwars.api.model.Resource
import com.example.startwars.databinding.FragmentStartWarCharacterBinding
import com.example.startwars.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StartWarCharacterFragment : Fragment() {
    private lateinit var binding: FragmentStartWarCharacterBinding
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentStartWarCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observedCharacterList()
        binding.btnSeeDetail.setOnClickListener {
                  mainViewModel.getSelectedCharacter(binding.autoTvCharacter.text.toString())?.let {
                   findNavController().navigate(
                       StartWarCharacterFragmentDirections.actionStartWarCharacterFragmentToStartWarCharacterDetailsFragment(it)
                   )
               }?: (activity as MainActivity).showSnackBarMessage(requireContext().getString(R.string.character_selection_error_msg))
        }
    }

    private fun observedCharacterList() {
        mainViewModel.characterResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { it ->
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            it.map { data-> data.name }).also {
                            binding.autoTvCharacter.setAdapter(it)
                        }
                        (activity as MainActivity).showLoaderApp(false)
                    }
                }
                is Resource.Error -> {
                    (activity as MainActivity).showLoaderApp(false)
                    response.message?.let { (activity as MainActivity).showSnackBarMessage(it) }
                }
                is Resource.Loading -> {
                    (activity as MainActivity).showLoaderApp(true)
                }
            }
        }
    }
}