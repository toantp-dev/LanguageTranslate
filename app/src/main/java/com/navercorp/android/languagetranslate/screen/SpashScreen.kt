package com.navercorp.android.languagetranslate.screen

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
        )
        Image(
            painter = painterResource(id = R.drawable.launcher),
            contentDescription = "",
            modifier = Modifier.size(200.dp),
        )


    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    LanguageTranslateTheme {
        SplashScreen()
    }
}