package br.com.alexandreferris.randomadvice.model

import com.squareup.moshi.Json

data class AdviceResponse (
    val slip: AdviceSlip
)

data class AdviceSlip (
    val advice: String,
    @Json(name = "slip_id") val slipId: String
)