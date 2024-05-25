package com.navercorp.android.languagetranslate.screen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.Conversation
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.TranslateViewModel
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.component.LanguageSwitcher
import com.navercorp.android.languagetranslate.repository.VoiceToTextParser
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun ConversationScreen(
    navController: NavHostController,
    viewModel: TranslateViewModel = viewModel(factory = TranslateViewModel.Factory(LocalContext.current)),
) {

    val context = LocalContext.current

    val voiceToTextParser = remember {
        VoiceToTextParser(
            context = context,
            onTextRecorded = viewModel::addToConversation,
        )
    }

    var canRecord by remember {
        mutableStateOf(false)
    }

    val requestRecordPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        canRecord = isGranted
    }

    LaunchedEffect(requestRecordPermissionLauncher) {
        requestRecordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val state by voiceToTextParser.state.collectAsState()

    val conversations by viewModel.conversation.collectAsState()

    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.conversation)) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        voiceToTextParser.startListening()
                    }
                },
                backgroundColor = if (state.isSpeaking) LightColorScheme.error else LightColorScheme.primary
            ) {
                AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
                    if (isSpeaking) {
                        Icon(
                            painter = painterResource(id = R.drawable.stop_squared),
                            contentDescription = "mic",
                            tint = LightColorScheme.secondary,
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.mic),
                            contentDescription = "mic",
                            tint = LightColorScheme.secondary,
                        )
                    }
                }
            }
        }
    ) {
        Log.d("toantp", it.toString())
        Column(modifier = Modifier.padding(top = 12.dp)) {
            LanguageSwitcher()
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                itemsIndexed(conversations) { index, conversation ->
                    Row(
                        horizontalArrangement = if (index % 2 == 1) Arrangement.Start else Arrangement.End,
                        modifier = Modifier.fillMaxWidth(),
                    ){
                        ConversationItem(
                            conversation = conversation,
                            onClickSound = viewModel::textToSpeech,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: Conversation,
    modifier: Modifier = Modifier,
    onClickSound: (String) -> Unit = {},
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = LightColorScheme.secondary,
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.originText,
                    color = GrayDarker,
                    fontSize = 14.sp
                )
                Text(
                    text = conversation.translatedText,
                    color = LightColorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.sound),
                contentDescription = "sound",
                tint = LightColorScheme.primary,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(onClick = { onClickSound(conversation.originText) }),
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationItem() {
    LanguageTranslateTheme {
        ConversationItem(
            Conversation(
                originText = "Can I help you?",
                translatedText = "Tôi có thể giúp gì cho bạn không?"
            )
        )
    }
}

//@Preview
//@Composable
//fun PreviewConversationScreen() {
//    LanguageTranslateTheme {
//        ConversationScreen(navController = NavHostController(LocalContext.current))
//    }
//}