package com.navercorp.android.languagetranslate.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.navercorp.android.languagetranslate.R
import com.navercorp.android.languagetranslate.TranslateViewModel
import com.navercorp.android.languagetranslate.component.AppBar
import com.navercorp.android.languagetranslate.datasource.Favorite
import com.navercorp.android.languagetranslate.ui.theme.CyanIcon
import com.navercorp.android.languagetranslate.ui.theme.GrayDarker
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    viewModel: TranslateViewModel = viewModel(factory = TranslateViewModel.Factory(LocalContext.current)),
) {

    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.favorite)) },
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = LightColorScheme.background,
    ) {
        Log.d("toantp", it.toString())
        if (favorites.isEmpty()) {
            Empty(modifier = Modifier.fillMaxSize())
        } else {
            FavoriteList(
                favorites = favorites,
                onFavoriteClicked = viewModel::deleteFavorite,
                onSpeakerClicked = {
                    viewModel.textToSpeech(it.originalText)
                }
            )
        }
    }
}

@Composable
fun FavoriteList(
    favorites: List<Favorite>,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Favorite) -> Unit = {},
    onSpeakerClicked: (Favorite) -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier
            .padding(horizontal = 16.dp),
    ) {
        items(favorites) { favorite ->
            FavoriteItem(
                favorite = favorite,
                onFavoriteClicked = onFavoriteClicked,
                onSpeakerClicked = onSpeakerClicked,
            )
        }
    }
}

@Composable
fun FavoriteItem(
    favorite: Favorite,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (Favorite) -> Unit = {},
    onSpeakerClicked: (Favorite) -> Unit = {},
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LightColorScheme.secondary)
            .padding(8.dp),
    ) {

        Row {
            Text(
                text = favorite.originalText,
                color = GrayDarker,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "favorite",
                tint = LightColorScheme.primary,
                modifier = Modifier
                    .size(18.dp)
                    .padding(start = 4.dp)
                    .clickable(onClick = {
                        onFavoriteClicked(favorite)
                        Toast.makeText(context, "${favorite.originalText} đã bị loại khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show()
                    }),
            )
        }

        Row(modifier = Modifier.padding(top = 6.dp)) {
            Text(
                text = favorite.translatedText,
                color = LightColorScheme.primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(id = R.drawable.sound),
                contentDescription = "speak",
                tint = LightColorScheme.primary,
                modifier = Modifier
                    .size(18.dp)
                    .padding(start = 4.dp)
                    .clickable(onClick = { onSpeakerClicked(favorite) }),
            )
        }

    }
}

@Composable
fun Empty(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "star",
            tint = CyanIcon,
            modifier = Modifier
                .size(64.dp)
        )
        Text(
            text = stringResource(id = R.string.favorite_empty),
            fontSize = 14.sp,
            color = LightColorScheme.primary,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Preview
@Composable
fun PreviewFavoriteItem() {
    LanguageTranslateTheme {
        FavoriteItem(
            Favorite(0, "Can i help you", "Tôi có thể giúp gì bạn không?"),
            onFavoriteClicked = {},
            onSpeakerClicked = {},
        )
    }
}

@Preview
@Composable
fun PreviewFavoriteList() {
    LanguageTranslateTheme {
        FavoriteList(
            listOf(
                Favorite(0, "Can i help you", "Tôi có thể giúp gì bạn không?"),
                Favorite(0, "Can i help you", "Tôi có thể giúp gì bạn không?"),
                Favorite(0, "Can i help you", "Tôi có thể giúp gì bạn không?"),
                Favorite(0, "Can i help you", "Tôi có thể giúp gì bạn không?"),
            )
        )
    }
}

@Preview
@Composable
fun PreviewEmpty() {
    LanguageTranslateTheme {
        Empty()
    }
}













