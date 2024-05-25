//package com.navercorp.android.languagetranslate
//
//import android.util.Log
//import android.util.LruCache
//import androidx.lifecycle.MediatorLiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.asFlow
//import androidx.lifecycle.viewModelScope
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.Tasks
//import com.google.mlkit.common.model.DownloadConditions
//import com.google.mlkit.common.model.RemoteModelManager
//import com.google.mlkit.nl.translate.TranslateLanguage
//import com.google.mlkit.nl.translate.TranslateRemoteModel
//import com.google.mlkit.nl.translate.Translation
//import com.google.mlkit.nl.translate.Translator
//import com.google.mlkit.nl.translate.TranslatorOptions
//import com.navercorp.android.languagetranslate.model.Language
//import com.navercorp.android.languagetranslate.model.ResultOrError
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import java.util.Locale
//
//class TranslateViewModel : ViewModel() {
//
//    companion object {
//        // This specifies the number of translators instance we want to keep in our LRU cache.
//        // Each instance of the translator is built with different options based on the source
//        // language and the target language, and since we want to be able to manage the number of
//        // translator instances to keep around, an LRU cache is an easy way to achieve this.
//        private const val NUM_TRANSLATORS = 3
//    }
//
//    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()
//    private val pendingDownloads: HashMap<String, Task<Void>> = hashMapOf()
//    private val translators =
//        object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
//            override fun create(options: TranslatorOptions): Translator {
//                return Translation.getClient(options)
//            }
//            override fun entryRemoved(
//                evicted: Boolean,
//                key: TranslatorOptions,
//                oldValue: Translator,
//                newValue: Translator?,
//            ) {
//                oldValue.close()
//            }
//        }
//    val sourceLang = MutableLiveData<Language>(Language("en"))
//    val targetLang = MutableLiveData<Language>(Language("vi"))
//    val sourceText = MutableLiveData<String>()
//    val translatedText = MediatorLiveData<ResultOrError>()
//    val availableModels = MutableLiveData<List<String>>()
//
//    val translatedTextFlow = translatedText.asFlow()
//        .onEach {
//            Log.d("toantp", "${it.result}")
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue = ResultOrError(null, null)
//        )
//
//    // Gets a list of all available translation languages.
//    val availableLanguages: List<Language> = TranslateLanguage.getAllLanguages().map { Language(it) }
//
//    init {
//        // Create a translation result or error object.
//        val processTranslation =
//            OnCompleteListener<String> { task ->
//                if (task.isSuccessful) {
//                    translatedText.value = ResultOrError(task.result, null)
//                } else {
//                    translatedText.value = ResultOrError(null, task.exception)
//                }
//                // Update the list of downloaded models as more may have been
//                // automatically downloaded due to requested translation.
//                fetchDownloadedModels()
//            }
//        // Start translation if any of the following change: input text, source lang, target lang.
//        translatedText.addSource(sourceText) { translate().addOnCompleteListener(processTranslation) }
//        val languageObserver =
//            Observer<Language> { translate().addOnCompleteListener(processTranslation) }
//        translatedText.addSource(sourceLang, languageObserver)
//        translatedText.addSource(targetLang, languageObserver)
//
//        // Update the list of downloaded models.
//        fetchDownloadedModels()
//
//        viewModelScope.launch {
//            delay(1000)
//            downloadLanguage(
//               availableLanguages.first { it.code == "en" }
//            )
//            downloadLanguage(
//               availableLanguages.first { it.code == "vi" }
//            )
//        }
//
////        viewModelScope.launch {
////            sourceText.postValue("Hello")
////        }
//    }
//
//    fun updateText(text: String) {
//        sourceText.postValue(text)
//    }
//
//    private fun getModel(languageCode: String): TranslateRemoteModel {
//        return TranslateRemoteModel.Builder(languageCode).build()
//    }
//
//    // Updates the list of downloaded models available for local translation.
//    private fun fetchDownloadedModels() {
//        modelManager.getDownloadedModels(TranslateRemoteModel::class.java).addOnSuccessListener {
//                remoteModels ->
//            availableModels.value = remoteModels.sortedBy { it.language }.map { it.language }
//        }
//    }
//
//    // Starts downloading a remote model for local translation.
//    internal fun downloadLanguage(language: Language) {
//        val model = getModel(TranslateLanguage.fromLanguageTag(language.code)!!)
//        var downloadTask: Task<Void>?
//        if (pendingDownloads.containsKey(language.code)) {
//            downloadTask = pendingDownloads[language.code]
//            // found existing task. exiting
//            if (downloadTask != null && !downloadTask.isCanceled) {
//                return
//            }
//        }
//        downloadTask =
//            modelManager.download(model, DownloadConditions.Builder().build()).addOnCompleteListener {
//                pendingDownloads.remove(language.code)
//                fetchDownloadedModels()
//            }
//        pendingDownloads[language.code] = downloadTask
//    }
//
//    // Returns if a new model download task should be started.
//    fun requiresModelDownload(
//        lang: Language,
//        downloadedModels: List<String?>?,
//    ): Boolean {
//        return if (downloadedModels == null) {
//            true
//        } else !downloadedModels.contains(lang.code) && !pendingDownloads.containsKey(lang.code)
//    }
//
//    // Deletes a locally stored translation model.
//    internal fun deleteLanguage(language: Language) {
//        val model = getModel(TranslateLanguage.fromLanguageTag(language.code)!!)
//        modelManager.deleteDownloadedModel(model).addOnCompleteListener { fetchDownloadedModels() }
//        pendingDownloads.remove(language.code)
//    }
//
//    fun translate(): Task<String> {
//        val text = sourceText.value
//        val source = sourceLang.value
//        val target = targetLang.value
//        if (source == null || target == null || text == null || text.isEmpty()) {
//            return Tasks.forResult("")
//        }
//        val sourceLangCode = TranslateLanguage.fromLanguageTag(source.code)!!
//        val targetLangCode = TranslateLanguage.fromLanguageTag(target.code)!!
//        val options =
//            TranslatorOptions.Builder()
//                .setSourceLanguage(sourceLangCode)
//                .setTargetLanguage(targetLangCode)
//                .build()
//        return translators[options].downloadModelIfNeeded().continueWithTask { task ->
//            if (task.isSuccessful) {
//                translators[options].translate(text)
//            } else {
//                Tasks.forException<String>(
//                    task.exception
//                        ?: Exception("unknown_error")
//                )
//            }
//        }
//    }
//
//
//    override fun onCleared() {
//        super.onCleared()
//        // Each new instance of a translator needs to be closed appropriately. Here we utilize the
//        // ViewModel's onCleared() to clear our LruCache and close each Translator instance when
//        // this ViewModel is no longer used and destroyed.
//        translators.evictAll()
//    }
//
//
//}