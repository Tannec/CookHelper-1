package ru.tech.cookhelper.presentation.home_screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.AnimatedNavHost
import dev.olshevski.navigation.reimagined.rememberNavController
import ru.tech.cookhelper.presentation.app.components.Placeholder
import ru.tech.cookhelper.presentation.feed.FeedScreen
import ru.tech.cookhelper.presentation.forum_screen.ForumScreen
import ru.tech.cookhelper.presentation.home_screen.components.BottomNavigationBar
import ru.tech.cookhelper.presentation.ui.theme.ScaleCrossfadeTransitionSpec
import ru.tech.cookhelper.presentation.ui.utils.compose.PaddingUtils.setPadding
import ru.tech.cookhelper.presentation.ui.utils.compose.UIText
import ru.tech.cookhelper.presentation.ui.utils.navigation.Screen
import ru.tech.cookhelper.presentation.ui.utils.navigation.navBarList
import ru.tech.cookhelper.presentation.ui.utils.provider.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onTitleChange: (newTitle: UIText) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val topAppBarActions = LocalTopAppBarActions.current
    val bottomNavigationController =
        rememberNavController<Screen.Home>(startDestination = Screen.Home.Feed)
    LaunchedEffect(bottomNavigationController.currentDestination) {
        topAppBarActions.clearActions()
    }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = bottomNavigationController.currentDestination
                    ?: Screen.Home.Feed,
                items = navBarList,
                onClick = { screen ->
                    bottomNavigationController.navigateAndPopAll(screen)
                    onTitleChange(screen.title)
                }
            )
        },
        snackbarHost = { SnackbarHost(LocalSnackbarHost.current) }
    ) { contentPadding ->
        Box(Modifier.padding(contentPadding.setPadding(top = 0.dp))) {
            AnimatedNavHost(
                controller = bottomNavigationController,
                transitionSpec = ScaleCrossfadeTransitionSpec
            ) { bottomNavScreen ->
                when (bottomNavScreen) {
                    is Screen.Home.Feed -> {
                        FeedScreen()
                    }
                    is Screen.Home.Fridge -> {
                        Placeholder(
                            icon = bottomNavScreen.baseIcon,
                            text = bottomNavScreen.title.asString(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        )
                    }
                    is Screen.Home.Forum -> {
                        ForumScreen(scrollBehavior = scrollBehavior)
                    }
                    else -> {}
                }
            }
        }
    }
}