package com.navercorp.android.languagetranslate.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.GrayText
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme


@Composable
fun LanguageSwitcher(
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = LightColorScheme.secondary,
        ),
        border = BorderStroke(1.dp, LightColorScheme.tertiary),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.english),
                    contentDescription = "english",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = stringResource(id = R.string.english),
                    color = GrayText,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.translate),
                contentDescription = "english",
                colorFilter = ColorFilter.tint(color = GrayDarker),
                modifier = Modifier.size(24.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vietnamese),
                    contentDescription = "vietnamese",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = stringResource(id = R.string.vietnamese),
                    color = GrayText,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLanguageSwitcher() {
    LanguageTranslateTheme {
        LanguageSwitcher()
    }
}