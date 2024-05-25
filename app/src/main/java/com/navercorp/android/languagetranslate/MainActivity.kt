package com.navercorp.android.languagetranslate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.navercorp.android.languagetranslate.model.Language
import com.navercorp.android.languagetranslate.repository.LanguageRepository
import com.navercorp.android.languagetranslate.repository.LanguageRepositoryImpl
import com.navercorp.android.languagetranslate.screen.ContactScreen
import com.navercorp.android.languagetranslate.screen.ConversationScreen
import com.navercorp.android.languagetranslate.screen.FavoriteScreen
import com.navercorp.android.languagetranslate.screen.HomeScreen
import com.navercorp.android.languagetranslate.screen.IntroduceScreen
import com.navercorp.android.languagetranslate.screen.LanguagePickerScreen
import com.navercorp.android.languagetranslate.screen.PolicyScreen
import com.navercorp.android.languagetranslate.screen.SettingScreen
import com.navercorp.android.languagetranslate.screen.SplashScreen
import com.navercorp.android.languagetranslate.screen.TranslateScreen
import com.navercorp.android.languagetranslate.ui.theme.BottomContainer
import com.navercorp.android.languagetranslate.ui.theme.BottomTextUnSelected
import com.navercorp.android.languagetranslate.ui.theme.LanguageTranslateTheme
import com.navercorp.android.languagetranslate.ui.theme.LightColorScheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LanguageTranslateTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    var shouldShowSplash by remember {
        mutableStateOf(true)
    }

    val languageRepository: LanguageRepository = LanguageRepositoryImpl()

    LaunchedEffect(Unit) {
        languageRepository.downloadLanguage(Language("en"))
        languageRepository.downloadLanguage(Language("vi"))
        delay(1000L)
        shouldShowSplash = false
    }

    if (shouldShowSplash) {
        SplashScreen()
    } else {
        MainNavigation()
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        Screen.Favorite,
        Screen.Home,
        Screen.Setting,
    )

    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigation(
                backgroundColor = BottomContainer,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    BottomNavigationItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.resourceId),
                                fontSize = 12.sp,
                                color = if (selected) LightColorScheme.primary else BottomTextUnSelected,
                            )
                        },
                        selectedContentColor = LightColorScheme.primary,
                        unselectedContentColor = BottomTextUnSelected,
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Favorite.route) { FavoriteScreen(navController) }

            navigation(startDestination = Screen.Home.Main.route, route = Screen.Home.route) {
                composable(Screen.Home.Main.route) {
                    HomeScreen(navController)
                }
                composable(Screen.Home.Translate.route) {
                    TranslateScreen(navController)
                }
                composable(Screen.Home.Conversation.route) {
                    ConversationScreen(navController)
                }
            }

            navigation(startDestination = Screen.Setting.SettingHome.route, route = Screen.Setting.route) {
                composable(Screen.Setting.SettingHome.route) {
                    SettingScreen(navController)
                }
                composable(Screen.Setting.Introduce.route) {
                    IntroduceScreen(navController)
                }
                composable(Screen.Setting.Policy.route) {
                    PolicyScreen(navController)
                }
                composable(Screen.Setting.Contact.route) {
                    ContactScreen(navController)
                }
                composable(Screen.Setting.LanguagePicker.route) {
                    LanguagePickerScreen(navController)
                }
            }

//            composable(Screen.Setting.route) { SettingScreen(navController) }
        }
    }

}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int = R.string.home,
    val icon: ImageVector = Icons.Filled.Home,
) {
    data object Splash : Screen("splash")

    data object Favorite : Screen("favorite", R.string.favorite, Icons.Filled.Star)

    data object Home : Screen("home", R.string.home, Icons.Filled.Home) {

        data object Main: Screen("main")
        data object Translate: Screen("translate")
        data object Conversation: Screen("conversation")

    }

    data object Setting : Screen("setting", R.string.setting, Icons.Filled.Menu) {

        data object SettingHome: Screen("setting_home")
        data object Introduce: Screen("introduce")
        data object Policy: Screen("policy")
        data object Contact: Screen("contact")
        data object LanguagePicker: Screen("language_picker")
    }

}