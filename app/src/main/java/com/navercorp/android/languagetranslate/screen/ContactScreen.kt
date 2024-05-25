package com.navercorp.android.languagetranslate.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun ContactScreen(
    navController: NavHostController,
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.contact),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.zalo),
                    contentDescription = "zalo",
                )
                Text(
                    text = stringResource(id = R.string.zalo),
                    color = LightColorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.messenger),
                    contentDescription = "messenger",
                )
                Text(
                    text = stringResource(id = R.string.messenger),
                    color = LightColorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.telegram),
                    contentDescription = "telegram",
                )
                Text(
                    text = stringResource(id = R.string.telegram),
                    color = LightColorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

        }
    }
}

@Composable
@Preview
fun PreviewContactScreen() {
    LanguageTranslateTheme {
        ContactScreen(navController = NavHostController(LocalContext.current))
    }
}