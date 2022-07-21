package com.example.startwars.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.example.startwars.R
import com.example.startwars.api.model.Character
import com.example.startwars.api.model.Film
import com.example.startwars.api.model.Resource
import com.example.startwars.api.model.Result
import com.example.startwars.api.repository.Repository
import com.example.startwars.util.AppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, private val startStarWarApplication: Application) : AndroidViewModel(startStarWarApplication) {
    private val characterList = ArrayList<Result>()
    private var counter: Int = 1
    private val _characterResponse = MutableLiveData<Resource<List<Result>>>()
    val characterResponse: LiveData<Resource<List<Result>>> = _characterResponse

    private val _filmResponse = MutableLiveData<Resource<List<Film>>>()
    val filmResponse: LiveData<Resource<List<Film>>> = _filmResponse

    init {
        loadCharacters()
    }
    private fun loadCharacters() {
        if(AppUtils.hasInternetConnection(startStarWarApplication)){
            _characterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                try {
                    fetchCharacterList().let {
                        it.body()?.count?.let { resultCount ->
                            counter = ceil(resultCount.toDouble() / 10).toInt()
                        }
                    }
                    val listPageNumbers = arrayListOf<Int>()
                    (2..counter).forEach { listPageNumbers.add(it) }
                    listPageNumbers.map {
                        async {
                            fetchCharacterList(it)
                        }
                    }.awaitAll()


                    _characterResponse.postValue(Resource.Success(characterList))

                } catch (e: Exception) {
                    _characterResponse.postValue(Resource.Error(startStarWarApplication.getString(R.string.something_went_wrong_error)))
                }
            }
        }else{
            _characterResponse.postValue(Resource.Error(startStarWarApplication.getString(R.string.no_intenet_connection)))
        }
    }

    private suspend fun fetchCharacterList(pageNumber: Int = 1): Response<Character> {
        val characterFetch = repository.getCharacters(pageNumber)
        if (characterFetch.isSuccessful) {
            characterFetch.body()?.let { characterList.addAll(it.results) }
        }
        return characterFetch
    }

     fun getSelectedCharacter(text: String): Result? {
        return characterList.firstOrNull { result -> result.name.equals(text, true) }
    }

    fun loadFilmsByCharacter(characterArg: Result) {
        if(AppUtils.hasInternetConnection(startStarWarApplication)){
            _filmResponse.postValue(Resource.Loading())


            try {
                viewModelScope.launch {
                    val resultFilms = characterArg.films?.map {
                        async {
                            repository.getCharactersFilms(Uri.parse(it).lastPathSegment.toString())
                        }
                    }?.awaitAll()

                    val listOfFilmResponse = arrayListOf<Film>()
                    resultFilms?.forEach {
                        if (it.isSuccessful) {
                            it.body()?.let { film ->
                                listOfFilmResponse.add(film)
                            }
                        }
                    }
                    _filmResponse.postValue(Resource.Success(listOfFilmResponse))
                }
            } catch (e: Exception) {
                _filmResponse.postValue(Resource.Error(startStarWarApplication.getString(R.string.something_went_wrong_error)))
            }
        }else{
            _filmResponse.postValue(Resource.Error(startStarWarApplication.getString(R.string.no_intenet_connection)))
        }
    }
}