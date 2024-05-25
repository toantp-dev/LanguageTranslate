package com.navercorp.android.languagetranslate

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navercorp.android.languagetranslate.datasource.Favorite
import com.navercorp.android.languagetranslate.model.Language
import com.navercorp.android.languagetranslate.model.ResultOrError
import com.navercorp.android.languagetranslate.repository.FavoriteRepository
import com.navercorp.android.languagetranslate.repository.FavoriteRepositoryImpl
import com.navercorp.android.languagetranslate.repository.LanguageRepository
import com.navercorp.android.languagetranslate.repository.LanguageRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class TranslateViewModel(
    context: Context,
) : ViewModel() {

    class Factory(
        private val context: Context,
    ) : AbstractSavedStateViewModelFactory() {

        override fun <T : ViewModel> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            if (modelClass.isAssignableFrom(TranslateViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TranslateViewModel(
                    context,
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context) { status ->
            if(status != TextToSpeech.ERROR) {
//                textToSpeech.setLanguage(Locale.US)
                textToSpeech.setLanguage(Locale("en"))
            }
        }
    }

    private val languageRepository: LanguageRepository = LanguageRepositoryImpl()

    private val _sourceLang = MutableStateFlow(Language("en"))
    private val _targetLang = MutableStateFlow(Language("vi"))

    private val _sourceText = MutableStateFlow("")

    val translatedTextFlow = combine(
        _sourceLang,
        _targetLang,
        _sourceText
    ) { source, target, text ->
        languageRepository.translate(source, target, text)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ResultOrError("", null)
    )

    init {

        textToSpeech.speak("", TextToSpeech.QUEUE_FLUSH, null, "no")

        viewModelScope.launch {

//            languageRepository.deleteLanguage(_sourceLang.value)
//            languageRepository.deleteLanguage(_targetLang.value)

            _sourceLang.onEach { sourceLang ->
                val downloadedModels = languageRepository.fetchDownloadedModels()
                if (!downloadedModels.any { it.languageCode == sourceLang.code }) {
                    languageRepository.downloadLanguage(sourceLang)
                }
            }.collect()

            _targetLang.onEach { targetLang ->
                val downloadedModels = languageRepository.fetchDownloadedModels()
                if (!downloadedModels.any { it.languageCode == targetLang.code }) {
                    languageRepository.downloadLanguage(targetLang)
                }
            }.collect()

            val downloadedModels = languageRepository.fetchDownloadedModels()
            Log.d("toantp",downloadedModels.toString())

        }

    }

    fun updateText(text: String) {
        _sourceText.update { text }
    }

    fun textToSpeech(text: String) {
        val result = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "no")
        if (result == TextToSpeech.ERROR) {
            Log.d("toantp", "Cannot text to speech")
        }
    }

    // use for conversation

    private val _conversation: MutableStateFlow<List<Conversation>> = MutableStateFlow(
        listOf()
    )
    val conversation = _conversation.asStateFlow()

    fun addToConversation(text: String) {
        viewModelScope.launch {
            val translated = languageRepository.translate(_sourceLang.value, _targetLang.value, text)
            _conversation.update { conversations ->
                conversations + Conversation(text, translated.result.orEmpty())
            }
        }
    }

    // use for favorite

    private val favoriteRepository: FavoriteRepository = FavoriteRepositoryImpl()

    val favorites: StateFlow<List<Favorite>> =  favoriteRepository.getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

    fun insertFavorite(original: String, translated: String) {
        viewModelScope.launch {
            favoriteRepository.addFavorite(
                Favorite(
                    originalText = original,
                    translatedText = translated,
                )
            )
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favorite)
        }
    }

    // language picker

    val availableLanguages = flowOf(
        languageRepository.fetchAvailableLanguage()
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
        )

}

data class Conversation(
    val originText: String,
    val translatedText: String,
)