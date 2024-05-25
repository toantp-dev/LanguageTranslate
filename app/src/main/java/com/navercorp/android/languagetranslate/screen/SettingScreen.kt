package com.navercorp.android.languagetranslate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.Screen
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun SettingScreen(
    navController: NavHostController,
) {
    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.setting)) },
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = LightColorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp, horizontal = 8.dp)
            ) {

                Item(
                    text = stringResource(id = R.string.display_language),
                    onClick = { navController.navigate(Screen.Setting.LanguagePicker.route)},
                )

                Item(
                    text = stringResource(id = R.string.introduce),
                    onClick = { navController.navigate(Screen.Setting.Introduce.route)},
                    modifier = Modifier.padding(top = 16.dp)
                )

                Item(
                    text = stringResource(id = R.string.policy),
                    onClick = { navController.navigate(Screen.Setting.Policy.route)},
                    modifier = Modifier.padding(top = 16.dp)
                )

                Item(
                    text = stringResource(id = R.string.contact),
                    onClick = { navController.navigate(Screen.Setting.Contact.route)},
                    modifier = Modifier.padding(top = 16.dp)
                )


            }
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = LightColorScheme.primary,
                    contentColor = LightColorScheme.secondary
                ),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = stringResource(id = R.string.exit))
            }
        }
    }
}

@Composable
fun Item(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(LightColorScheme.secondary)
                .clickable {
                    onClick()
                }
                .padding(16.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.chervon_right),
                contentDescription = "next",
                tint = GrayDarker,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingScreen() {
    LanguageTranslateTheme {
        SettingScreen(navController = NavHostController(LocalContext.current))
    }
}