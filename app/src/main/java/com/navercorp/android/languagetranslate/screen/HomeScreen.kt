@file:OptIn(ExperimentalMaterial3Api::class)

package com.navercorp.android.languagetranslate.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.Screen
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.component.LanguageSwitcher
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightColorScheme.background)
    ) {
        AppBar(title = stringResource(id = R.string.home))
        TranslateInput(
            modifier = Modifier.padding(8.dp),
            onInputCLicked = {
                navController.navigate(Screen.Home.Translate.route)
            }
        )
        MainFunction(
            onConversationClicked = {
                navController.navigate(Screen.Home.Conversation.route)
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun MainFunction(
    onConversationClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = LightColorScheme.secondary,
            ),
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "vietnamese",
                    modifier = Modifier
                        .size(52.dp)
                )
                Text(
                    text = stringResource(id = R.string.camera),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LightColorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(start = 8.dp)
        ) {
            ElevatedCard(
                onClick = onConversationClicked,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = LightColorScheme.secondary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.conversation),
                        contentDescription = "vietnamese",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.conversation),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LightColorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = LightColorScheme.secondary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.scan),
                        contentDescription = "vietnamese",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.translate_with_ar),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LightColorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TranslateInput(
    onInputCLicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = LightColorScheme.secondary,
        ),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        LanguageSwitcher(
            modifier = Modifier.padding(6.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable(onClick = onInputCLicked)
        ) {
            Text(
                text = stringResource(id = R.string.input),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LightColorScheme.tertiary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewTranslateInput() {
    LanguageTranslateTheme {
        TranslateInput(onInputCLicked = {})
    }
}

@Composable
@Preview
fun PreviewHomeScreen() {
    LanguageTranslateTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}