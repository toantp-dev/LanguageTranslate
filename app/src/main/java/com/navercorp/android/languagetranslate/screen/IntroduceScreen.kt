package com.navercorp.android.languagetranslate.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.ui.theme.Black
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun IntroduceScreen(
    navController: NavHostController,
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.introduce),
                canBack = true,
                navController = navController,
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = LightColorScheme.background,
    ) {
        Log.d("toantp", it.toString())

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.introduce_description),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Black.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
@Preview
fun PreviewIntroduceScreen() {
    LanguageTranslateTheme {
        IntroduceScreen(navController = NavHostController(LocalContext.current))
    }
}