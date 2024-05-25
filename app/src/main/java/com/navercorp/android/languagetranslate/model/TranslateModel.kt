package com.navercorp.android.languagetranslate.model

import com.google.mlkit.common.sdkinternal.ModelType
import com.google.mlkit.nl.translate.TranslateRemoteModel

data class TranslateModel(
    val languageCode: String,
    val modelName: String?,
    val modelHash: String?,
    val modelType: ModelType,
    val isBaseModel: Boolean,
//    val modelNameForBackend: String,
//    val uniqueModelNameForPersist: String,
)

fun TranslateRemoteModel.toModel() = TranslateModel(
    languageCode = language,
    modelName = modelName,
    modelHash = modelHash,
    modelType = modelType,
    isBaseModel = isBaseModel,
//    modelNameForBackend = modelNameForBackend,
//    uniqueModelNameForPersist = uniqueModelNameForPersist,
)