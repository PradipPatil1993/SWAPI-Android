package com.example.startwars.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.startwars.api.model.Resource
import com.example.startwars.databinding.FragmentStartWarCharacterDetailsBinding
import com.example.startwars.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartWarCharacterDetailsFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentStartWarCharacterDetailsBinding
    private val safeArgs: StartWarCharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentStartWarCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFilms()
        observedFilms()
        setUpUI()
    }

    private fun setUpUI() {
        binding.apply {

            tvCharacterBirthYear.text = safeArgs.characterArg.birthYear
            safeArgs.let {
                it.characterArg.let { result ->
                    tvCharacterName.text = result.name

                    if(result.birthYear.isNullOrEmpty()){
                        tvCharacterBirthYear.visibility = View.GONE
                    }else{
                        tvCharacterBirthYear.visibility = View.VISIBLE
                        tvCharacterBirthYear.text = result.birthYear
                    }

                    if(result.height.isNullOrEmpty()){
                        tvHeight.visibility = View.GONE
                    }else{
                        tvHeight.visibility = View.VISIBLE
                        tvHeight.text = "${result.height} cm"
                    }

                }
            }
        }
    }


    private fun loadFilms() {
        mainViewModel.loadFilmsByCharacter(safeArgs.characterArg)
    }

    private fun observedFilms() {
        mainViewModel.filmResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { it ->
                        binding.rvFilms.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            setHasFixedSize(true)
                            adapter = FilmAdapter(it)
                            addItemDecoration(
                                DividerItemDecoration(
                                    requireContext(),
                                    LinearLayout.VERTICAL
                                )
                            )
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