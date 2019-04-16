package br.com.alexandreferris.randomadvice.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.alexandreferris.randomadvice.data.remote.AdviceSlipApiService
import br.com.alexandreferris.randomadvice.model.AdviceResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

open class MainVM @Inject constructor(private val networkApi: AdviceSlipApiService): ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val advice: MutableLiveData<String> = MutableLiveData<String>()
    private val adviceLoadError = MutableLiveData<Boolean>()
    private val loading = MutableLiveData<Boolean>()

    fun getAdvice(): MutableLiveData<String> {
        return advice
    }

    fun getError(): LiveData<Boolean> {
        return adviceLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun fetchRandomAdvice() {
        loading.value = true
        disposable.add(networkApi.getRandomAdvice()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<AdviceResponse>() {
                override fun onError(e: Throwable) {
                    adviceLoadError.value = true
                    loading.value = false
                }

                override fun onSuccess(adviceResponse: AdviceResponse) {
                    adviceLoadError.value = false
                    loading.value = false

                    advice.value = adviceResponse.slip.advice
                }
            })
        )
    }

    init {
        fetchRandomAdvice()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}