package com.example.findingfalcone.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.findingfalcone.domain.Repository
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import com.nhaarman.mockito_kotlin.firstValue
import com.nhaarman.mockito_kotlin.secondValue
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModelTest {

    private val mockedPlanetResponse = listOf(
        Planet("planetA", 10),
        Planet("PlanetB", 40),
        Planet("PlanetC", 60),
        Planet("PlanetD", 90)
    )
    private val mockedVehicleResponse = listOf(
        Vehicle("shuttle", 1, 300, 20),
        Vehicle("space shuttle", 2, 100, 10),
        Vehicle("space ship", 1, 100, 90),
        Vehicle("space bus", 1, 200, 5)
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var repository: Repository

    @Mock
    lateinit var viewState: Observer<MainViewModel.ViewState>

    @Mock
    lateinit var loading: Observer<MainViewModel.Loading>

    @Mock
    lateinit var message: Observer<String>

    private lateinit var lifecycle: Lifecycle
    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        vm = MainViewModel(repository)

        vm.viewState.observeForever(viewState)
        vm.loading.observeForever(loading)
        vm.message.observeForever(message)

        Mockito.`when`(repository.getPlanets()).thenReturn(Single.just(mockedPlanetResponse))
        Mockito.`when`(repository.getVehicles()).thenReturn(Single.just(mockedVehicleResponse))
        Mockito.`when`(repository.getToken()).thenReturn(Completable.complete())
    }

    @Test
    fun init() {
        val selectedItem = mapOf<Planet, Vehicle?>(
            Planet("planetA", 10) to null,
            Planet("PlanetB", 40) to null,
            Planet("PlanetC", 60) to null,
            Planet("PlanetD", 90) to null
        )
        val captor = ArgumentCaptor.forClass(MainViewModel.Loading::class.java)
        vm.init()

        Mockito.verify(loading, Mockito.times(2)).onChanged(captor.capture())

        // on subscribe
        kotlin.test.assertEquals(MainViewModel.Loading.Show, captor.firstValue)

        // on success
        Mockito.verify(viewState).onChanged(MainViewModel.ViewState(selectedItem, false))

        // on terminate
        kotlin.test.assertEquals(MainViewModel.Loading.Hide, captor.secondValue)
    }

    @Test
    fun initFailedApiNetworkException() {
        Mockito.`when`(repository.getVehicles())
            .thenReturn(Single.error { UnknownHostException() })
        vm.init()

        Mockito.verify(message).onChanged("No Network Error")
    }

    @Test
    fun initFailedApiTimeOutException() {
        Mockito.`when`(repository.getVehicles())
            .thenReturn(Single.error { SocketTimeoutException() })
        vm.init()

        Mockito.verify(message).onChanged("Timeout Error")
    }
}