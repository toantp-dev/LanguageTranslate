package com.navercorp.android.languagetranslate.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme
import com.navercorp.android.languagetranslate.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    canBack: Boolean = false,
    navController: NavHostController = rememberNavController(),
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = LightColorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (canBack) {
                Icon(
                    painter = painterResource(id = R.drawable.chevron_left),
                    contentDescription = "next",
                    tint = White,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                             navController.popBackStack()
                        },
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = LightColorScheme.primary,
        ),
        modifier = modifier.clip(
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
        )
    )

}


@Composable
@Preview
fun AppBarPreview() {
    LanguageTranslateTheme {
        AppBar("Trang chủ")
    }
}
@Composable
@Preview
fun AppBarPreviewWithBack() {
    LanguageTranslateTheme {
        AppBar("Trang chủ", canBack = true)
    }
}
