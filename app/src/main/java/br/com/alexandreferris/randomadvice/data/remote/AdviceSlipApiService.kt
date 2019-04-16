package br.com.alexandreferris.randomadvice.data.remote

import br.com.alexandreferris.randomadvice.model.AdviceResponse
import io.reactivex.Single
import retrofit2.http.GET

interface AdviceSlipApiService {

    //https://api.adviceslip.com/advice
    @GET("advice")
    fun getRandomAdvice(): Single<AdviceResponse>
}