package com.navercorp.android.languagetranslate.model

/** Holds the result of the translation or any error. */
data class ResultOrError(
    var result: String?,
    var error: Exception?
)