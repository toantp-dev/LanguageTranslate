package com.navercorp.android.languagetranslate.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.TranslateViewModel
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.component.LanguageSwitcher
import com.navercorp.android.languagetranslate.model.ResultOrError
import com.navercorp.android.languagetranslate.ui.theme.Black
import com.navercorp.android.languagetranslate.ui.theme.CyanIcon
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.GrayText
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun TranslateScreen(
    navController: NavHostController,
    viewModel: TranslateViewModel = viewModel(factory = TranslateViewModel.Factory(LocalContext.current)),
) {

    var text by remember { mutableStateOf("") }

    val translatedText by viewModel.translatedTextFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightColorScheme.background)
    ) {
        AppBar(
            title = stringResource(id = R.string.translate),
            canBack = true,
            navController = navController,
        )

        Column(modifier = Modifier.padding(8.dp)) {
            InputContent(
                text = text,
                onValueChange = {
                    text = it
                    viewModel.updateText(it)
                },
            )

            Text(
                text = stringResource(id = R.string.vietnamese),
                color = GrayDarker,
                modifier = Modifier.padding(top = 20.dp, start = 2.dp)
            )

            Text(
                text = translatedText.result.orEmpty(),
                color = LightColorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp, start = 2.dp)
            )

            CTAButton(
                viewModel = viewModel,
                originText = text,
                translatedText = translatedText,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }

    }
}

@Composable
fun InputContent(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        LanguageSwitcher()
        TextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(stringResource(id = R.string.english)) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Black,
                backgroundColor = LightColorScheme.secondary,
                focusedLabelColor = LightColorScheme.primary,
                unfocusedLabelColor = GrayText,
                cursorColor = LightColorScheme.primary,
                focusedIndicatorColor = LightColorScheme.primary,
                unfocusedIndicatorColor = LightColorScheme.primary,
            ),
            textStyle = TextStyle(fontSize = 24.sp),
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier
                            .clickable {
                                onValueChange("")
                            }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Composable
fun CTAButton(
    viewModel: TranslateViewModel,
    originText: String,
    translatedText: ResultOrError,
    modifier: Modifier = Modifier,
) {

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Row(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.sound),
            contentDescription = "sound",
            tint = CyanIcon,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = { viewModel.textToSpeech(originText) }),
        )
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "star",
            tint = CyanIcon,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable {
                    viewModel.insertFavorite(originText, translatedText.result.orEmpty())
                    Toast.makeText(context, "$originText đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show()
                }
        )
        Icon(
            painter = painterResource(id = R.drawable.copy),
            contentDescription = "copy",
            tint = CyanIcon,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable {
                    clipboardManager.setText(AnnotatedString(translatedText.result.orEmpty()))
                }
        )
    }
}

@Composable
@Preview
fun PreviewInputContent() {
    LanguageTranslateTheme {
        InputContent(
            text = "Hello",
            onValueChange = {}
        )
    }
}


@Composable
@Preview
fun PreviewTranslateScreen() {
    LanguageTranslateTheme {
        TranslateScreen(navController = NavHostController(LocalContext.current))
    }
}