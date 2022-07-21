package com.example.startwars.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.startwars.TestCoroutineRule
import com.example.startwars.api.model.Character
import com.example.startwars.api.model.Film
import com.example.startwars.api.model.Resource
import com.example.startwars.api.model.Result
import com.example.startwars.api.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response


@ExperimentalCoroutinesApi
class MainViewModelTestTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var characterObserver: Observer<Resource<List<Result>>>

    @Mock
    private lateinit var filmObserver: Observer<Resource<List<Film>>>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun terminate() {
        framework().clearInlineMocks()
    }

    @Test
    fun testCharacterFetchAPICallWithSuccess() {
        mockInternetAvailable()
        testCoroutineRule.runBlockingTest {

            doReturn(Response.success(getMockCharacterResponse()))
                .`when`(repository)
                .getCharacters(1)

            val viewModel = MainViewModel(repository, application)
            viewModel.characterResponse.observeForever(characterObserver)
            verify(repository).getCharacters(1)
            verify(characterObserver).onChanged(any())
            assertNotNull(viewModel.characterResponse.value)
            assertEquals(
                Resource.Success(listOf(getMockCharacter())).data?.get(0)?.name,
                viewModel.characterResponse.value?.data?.get(0)?.name
            )
        }
    }

    @Test
    fun testCharacterFetchAPICallWithFailureWithoutInternet() {
        mockInternetAvailable(false)
        testCoroutineRule.runBlockingTest {

            doReturn(Response.success(getMockCharacterResponse()))
                .`when`(repository)
                .getCharacters(1)

            `when`(application.getString(anyInt())).thenReturn("No internet connection, try after sometime")
            val viewModel = MainViewModel(repository, application)
            viewModel.characterResponse.observeForever(characterObserver)
            verify(repository, times(0)).getCharacters(1)
            verify(characterObserver).onChanged(any())
            assertNotNull(viewModel.characterResponse.value)
            assertEquals(
                "No internet connection, try after sometime",
                viewModel.characterResponse.value?.message
            )
        }
    }

    @Test
    fun testCharacterFetchAPICallWithError() {
        mockInternetAvailable()
        val exception = mock(HttpException::class.java)

        testCoroutineRule.runBlockingTest {

            doThrow(exception)
                .`when`(repository)
                .getCharacters(1)

            val viewModel = MainViewModel(repository, application)
            viewModel.characterResponse.observeForever(characterObserver)
            verify(repository).getCharacters(1)
            verify(characterObserver).onChanged(any())
            assertNotNull(viewModel.characterResponse.value)
            assertEquals(null, viewModel.characterResponse.value?.data)

        }
    }

    @Test
    fun testFilmsFetchAPICallWithSuccess() {
        mockInternetAvailable()
        val mockUri = mock(Uri::class.java)
        `when`(mockUri.lastPathSegment).thenReturn("1")
        mockStatic(Uri::class.java).use { mockedUuid ->
            mockedUuid.`when`<Any> { Uri.parse(anyString()) }.thenReturn(mockUri)
            assertEquals(Uri.parse("https://swapi.dev/api/films/1/").lastPathSegment, "1")

            testCoroutineRule.runBlockingTest {
                doReturn(Response.success(getMockFilm()))
                    .`when`(repository)
                    .getCharactersFilms("1")

                val viewModel = MainViewModel(repository, application)
                viewModel.loadFilmsByCharacter(getMockCharacter())

                viewModel.characterResponse.observeForever(characterObserver)
                viewModel.filmResponse.observeForever(filmObserver)

                verify(repository).getCharactersFilms("1")

                verify(characterObserver).onChanged(any())
                verify(filmObserver).onChanged(any())

                assertNotNull(viewModel.filmResponse.value)
                assertEquals(getMockFilm().title, viewModel.filmResponse.value?.data?.get(0)?.title)

            }
        }
    }

    @Test
    fun testFilmsFetchAPICallWithError() {
        mockInternetAvailable()
        val mockUri = mock(Uri::class.java)
        `when`(mockUri.lastPathSegment).thenReturn("1")
        mockStatic(Uri::class.java).use { mockedUuid ->
            mockedUuid.`when`<Any> { Uri.parse(anyString()) }.thenReturn(mockUri)
            assertEquals(Uri.parse("https://swapi.dev/api/films/1/").lastPathSegment, "1")

            val exception = mock(HttpException::class.java)

            testCoroutineRule.runBlockingTest {

                doThrow(exception)
                    .`when`(repository)
                    .getCharactersFilms("1")

                val viewModel = MainViewModel(repository, application)
                viewModel.loadFilmsByCharacter(getMockCharacter())

                viewModel.characterResponse.observeForever(characterObserver)
                viewModel.filmResponse.observeForever(filmObserver)

                verify(repository).getCharactersFilms("1")
                verify(characterObserver).onChanged(any())
                verify(filmObserver).onChanged(any())
                assertNotNull(viewModel.filmResponse.value)
                assertEquals(null, viewModel.filmResponse.value?.data)

            }
        }
    }

    @Test
    fun testVerifySelectedCharacter() {
        mockInternetAvailable()
        testCoroutineRule.runBlockingTest {

            doReturn(Response.success(getMockCharacterResponse()))
                .`when`(repository)
                .getCharacters(1)

            val viewModel = MainViewModel(repository, application)
            viewModel.characterResponse.observeForever(characterObserver)
            verify(repository).getCharacters(1)
            verify(characterObserver).onChanged(any())
            assertNotNull(viewModel.characterResponse.value)

            assertEquals(
                viewModel.getSelectedCharacter(getMockCharacter().name),
                getMockCharacter()
            )
        }
    }

    private fun mockInternetAvailable(isInternetAvailable: Boolean = true) {
        val connectivityManager = mock(ConnectivityManager::class.java) as ConnectivityManager
        val activeNetWork = mock(NetworkCapabilities::class.java)
        `when`(this.application.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(
            activeNetWork
        )
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        `when`(capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)).thenReturn(
            isInternetAvailable
        )
    }

    private fun getMockFilm(): Film {
        return Film(
            title = "A New Hope",
            episodeID = 12.00.toLong(),
            opening_crawl = "Film Description",
            director = "George Lucas",
            producer = "Gary Kurtz, Rick McCallum",
            releaseDate = "1977-05-25",
            characters = listOf(
                "https://swapi.dev/api/people/1/",
                "https://swapi.dev/api/people/2/"
            ),
            planets = listOf(
                "https://swapi.dev/api/planets/1/",
                "https://swapi.dev/api/planets/2/"
            ),
            starships = listOf(
                "https://swapi.dev/api/starships/1/",
                "https://swapi.dev/api/starships/2/"
            ),
            vehicles = listOf(
                "https://swapi.dev/api/vehicles/1/",
                "https://swapi.dev/api/vehicles/2/"
            ),
            species = listOf(
                "https://swapi.dev/api/species/1/",
                "https://swapi.dev/api/species/2/"
            ),
            created = "2014-12-10T14:23:31.880000Z",
            edited = "2014-12-10T14:23:31.880000Z",
            url = "https://swapi.dev/api/films/1/"
        )
    }

    private fun getMockCharacter(): Result {
        return Result(
            name = "Luke Skywalker",
            height = "172",
            birthYear = "1993",
            films = listOf("https://swapi.dev/api/films/1/")
        )
    }

    private fun getMockCharacterResponse(): Character {
        return Character(
            count = 10,
            next = "https://swapi.dev/api/people/?page=2",
            previous = null,
            results = listOf(getMockCharacter())
        )
    }
}