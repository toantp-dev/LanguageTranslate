package com.navercorp.android.languagetranslate.repository

import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.navercorp.android.languagetranslate.model.Language
import com.navercorp.android.languagetranslate.model.ResultOrError
import com.navercorp.android.languagetranslate.model.TranslateModel
import com.navercorp.android.languagetranslate.model.toModel
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LanguageRepository {

    suspend fun fetchDownloadedModels() : List<TranslateModel>

    suspend fun downloadLanguage(language: Language)

    suspend fun deleteLanguage(language: Language)

    suspend fun translate(
        sourceLang: Language,
        targetLang: Language,
        text : String,
    ): ResultOrError

    fun fetchAvailableLanguage(): List<Language>
}

class LanguageRepositoryImpl : LanguageRepository {

    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()

    override fun fetchAvailableLanguage() : List<Language> {
        return TranslateLanguage.getAllLanguages().map { Language(it) }
    }

    override suspend fun translate(
        sourceLang: Language,
        targetLang: Language,
        text : String,
    ): ResultOrError {
        return suspendCoroutine { cont ->
            if (text.isEmpty()) {
                cont.resume(ResultOrError("", null))
            } else {
                val sourceLangCode = TranslateLanguage.fromLanguageTag(sourceLang.code)!!
                val targetLangCode = TranslateLanguage.fromLanguageTag(targetLang.code)!!
                val options =
                    TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLangCode)
                        .setTargetLanguage(targetLangCode)
                        .build()

                val translator = Translation.getClient(options)
                translator
                    .downloadModelIfNeeded()
                    .continueWithTask { task ->
                        if (task.isSuccessful) {
                            translator.translate(text)
                        } else {
                            Tasks.forException<String>(
                                task.exception
                                    ?: Exception("unknown_error")
                            )
                        }
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            cont.resume(ResultOrError(task.result, null))
                        } else {
                            ResultOrError(null, task.exception)
                        }
                    }.addOnCanceledListener {
                        cont.resumeWithException(CancellationException())
                    }.addOnFailureListener {
                        cont.resumeWithException(it)
                    }
            }

        }
    }

    // Updates the list of downloaded models available for local translation.
    override suspend fun fetchDownloadedModels() : List<TranslateModel> {
        return suspendCoroutine { cont ->
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { remoteModels ->
                    val result = remoteModels.sortedBy { it.language }.map { it.toModel() }
                    cont.resume(result)
                }.addOnCanceledListener {
                    cont.resumeWithException(CancellationException())
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }

    override suspend fun downloadLanguage(language: Language) {
        return suspendCoroutine { cont ->
            val model = getModel(TranslateLanguage.fromLanguageTag(language.code)!!)
            modelManager.download(
                model,
                DownloadConditions.Builder().build()
            ).addOnCompleteListener {
                cont.resume(Unit)
            }.addOnCanceledListener {
                cont.resumeWithException(CancellationException())
            }.addOnFailureListener {
                cont.resumeWithException(it)
            }
        }
    }

    override suspend fun deleteLanguage(language: Language) {
        return suspendCoroutine { cont ->
            val model = getModel(TranslateLanguage.fromLanguageTag(language.code)!!)
            modelManager.deleteDownloadedModel(model)
                .addOnCompleteListener {
                    cont.resume(Unit)
                }.addOnCanceledListener {
                    cont.resumeWithException(CancellationException())
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        }
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

}