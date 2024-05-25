package com.navercorp.android.languagetranslate.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.TranslateViewModel
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.model.Language
import com.navercorp.android.languagetranslate.ui.theme.Black
import com.navercorp.android.languagetranslate.ui.theme.Gray
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme
import com.navercorp.android.languagetranslate.ui.theme.White

@Composable
fun LanguagePickerScreen(
    navController: NavHostController,
    viewModel: TranslateViewModel = viewModel(factory = TranslateViewModel.Factory(LocalContext.current)),
    ) {

    val languages by viewModel.availableLanguages.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.display_language),
                canBack = true,
                navController = navController,
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = LightColorScheme.background,
    ) {
        Log.d("toantp", it.toString())
        LanguageList(
            languages = languages,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(White)
                .padding(horizontal = 8.dp)
        )

    }
}

@Composable
fun LanguageList(
    languages: List<Language>,
    modifier: Modifier = Modifier,
    onLanguageClicked: (Language) -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier,
    ) {
        items(languages) { language ->
            LanguageItem(
                language = language,
                onLanguageClicked = onLanguageClicked,
            )
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    modifier: Modifier = Modifier,
    onLanguageClicked: (Language) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onLanguageClicked(language) }
            .padding(6.dp),
    ) {
        Text(
            text = language.displayName,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.weight(1f),
        )
        Image(
            painter = painterResource(id = R.drawable.star_outlined),
            colorFilter = ColorFilter.tint(Gray),
            contentDescription = "star",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
@Preview
fun PreviewLanguageItem() {
    LanguageTranslateTheme {
        LanguageItem(
            language = Language("en")
        )
    }
}

@Composable
@Preview
fun PreviewLanguagePickerScreen() {
    LanguageTranslateTheme {
        LanguagePickerScreen(navController = NavHostController(LocalContext.current))
    }
}