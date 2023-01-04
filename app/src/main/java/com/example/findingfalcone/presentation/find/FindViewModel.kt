package com.example.findingfalcone.presentation.find

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.findingfalcone.application.extensions.SingleLiveEvent
import com.example.findingfalcone.domain.Repository
import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import io.reactivex.disposables.Disposable
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FindViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _viewState = MutableLiveData<FindResponse>()
    val viewState: LiveData<FindResponse> = _viewState

    private val _loading = SingleLiveEvent<Loading>()
    val loading: LiveData<Loading> = _loading

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String> = _message

    private var initialised = false
    private var disposable: Disposable? = null

    fun init(planets: List<Planet>, vehicles: List<Vehicle>) {
        if (!initialised) {
            initialised = true

            findPrince(planets, vehicles)
        }
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    private fun findPrince(planets: List<Planet>, vehicles: List<Vehicle>) {
        disposable = repository.findPrinces(planets, vehicles)
            .doOnSubscribe { _loading.postValue(Loading.Show) }
            .doAfterTerminate { _loading.postValue(Loading.Hide) }
            .subscribe(::onSuccess, ::onFailure)
    }

    private fun onSuccess(response: FindResponse) {
        _viewState.postValue(response)
    }

    private fun onFailure(it: Throwable) {
        Log.e("MAIN VIEW MODEL", it.message.orEmpty())
        _message.postValue(resolveError(it))
        // TODO: good to have a nicer way for failure also need implement retry functionality
    }

    // for now having errors here, but better to have domain exception and map some exception from data layer to domain meaningful exception
    private fun resolveError(error: Throwable) =
        when (error) {
            is SocketException,
            is UnknownHostException -> "No Network Error"
            is SocketTimeoutException -> "Timeout Error"
            else -> "Something went wrong"
        }

    sealed class Loading {
        object Show : Loading()
        object Hide : Loading()
    }
}