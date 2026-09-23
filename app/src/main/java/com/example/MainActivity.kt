package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NewsBottomNavBar
import com.example.ui.components.NewsHeader
import com.example.ui.components.NotificationsDialog
import com.example.ui.components.PhotoLightboxDialog
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LatestNewsScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.PhotoGalleryScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.UserAccountScreen
import com.example.ui.screens.VideoNewsScreen
import com.example.ui.theme.News24BDTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            News24BDTheme {
                News24BDApp()
            }
        }
    }
}

@Composable
fun News24BDApp(viewModel: NewsViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()
    val showNotificationsDialog by viewModel.showNotificationsDialog.collectAsStateWithLifecycle()
    val activeVideo by viewModel.activeVideo.collectAsStateWithLifecycle()
    val activePhoto by viewModel.activePhoto.collectAsStateWithLifecycle()

    val breakingArticles = allArticles.filter { it.breaking }

    // Android Hardware Back button handling
    BackHandler(enabled = currentScreen !is AppScreen.Home) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Only show main header if we are not deep inside an article or admin screen
            if (currentScreen !is AppScreen.ArticleDetail &&
                currentScreen !is AppScreen.CategoryDetail &&
                currentScreen !is AppScreen.AdminDashboard &&
                currentScreen !is AppScreen.UserAccount
            ) {
                NewsHeader(
                    onSearchClick = { viewModel.navigateTo(AppScreen.Search) },
                    onNotificationsClick = { viewModel.toggleNotificationsDialog(true) },
                    onBookmarksClick = { viewModel.navigateTo(AppScreen.Bookmarks) },
                    onAccountClick = { viewModel.navigateTo(AppScreen.UserAccount) },
                    unreadNotificationCount = breakingArticles.size
                )
            }
        },
        bottomBar = {
            NewsBottomNavBar(
                currentScreen = currentScreen,
                onTabSelected = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Home -> HomeScreen(viewModel = viewModel)
                is AppScreen.Latest -> LatestNewsScreen(viewModel = viewModel)
                is AppScreen.Categories -> CategoriesScreen(viewModel = viewModel)
                is AppScreen.Search -> SearchScreen(viewModel = viewModel)
                is AppScreen.Menu -> MenuScreen(viewModel = viewModel)
                is AppScreen.ArticleDetail -> ArticleDetailScreen(
                    articleId = screen.articleId,
                    viewModel = viewModel
                )
                is AppScreen.CategoryDetail -> CategoryDetailScreen(
                    categoryId = screen.categoryId,
                    categoryNameBn = screen.categoryNameBn,
                    viewModel = viewModel
                )
                is AppScreen.Bookmarks -> BookmarksScreen(viewModel = viewModel)
                is AppScreen.VideoSection -> VideoNewsScreen(viewModel = viewModel)
                is AppScreen.PhotoGallery -> PhotoGalleryScreen(viewModel = viewModel)
                is AppScreen.UserAccount -> UserAccountScreen(viewModel = viewModel)
                is AppScreen.AdminDashboard -> AdminDashboardScreen(viewModel = viewModel)
            }
        }
    }

    // Notifications Dialog
    if (showNotificationsDialog) {
        NotificationsDialog(
            breakingArticles = breakingArticles,
            onDismiss = { viewModel.toggleNotificationsDialog(false) },
            onArticleClick = { article ->
                viewModel.navigateTo(AppScreen.ArticleDetail(article.id))
            }
        )
    }

    // Video Player Dialog
    activeVideo?.let { video ->
        VideoPlayerDialog(
            video = video,
            onDismiss = { viewModel.closeVideoPlayer() }
        )
    }

    // Photo Lightbox Dialog
    activePhoto?.let { photo ->
        PhotoLightboxDialog(
            photo = photo,
            onDismiss = { viewModel.closePhotoViewer() }
        )
    }
}
