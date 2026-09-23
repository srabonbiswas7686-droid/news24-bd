package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.NewsArticle
import com.example.data.NewsCategory
import com.example.data.NewsRepository
import com.example.data.PhotoItem
import com.example.data.SampleNewsData
import com.example.data.VideoNews
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class AppScreen {
    object Home : AppScreen()
    object Latest : AppScreen()
    object Categories : AppScreen()
    object Search : AppScreen()
    object Menu : AppScreen()
    data class ArticleDetail(val articleId: String) : AppScreen()
    data class CategoryDetail(val categoryId: String, val categoryNameBn: String) : AppScreen()
    object Bookmarks : AppScreen()
    object VideoSection : AppScreen()
    object PhotoGallery : AppScreen()
    object UserAccount : AppScreen()
    object AdminDashboard : AppScreen()
}

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NewsRepository
    init {
        val database = AppDatabase.getDatabase(application)
        repository = NewsRepository(database.bookmarkDao(), database.customNewsDao())
    }

    // Navigation state
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf<AppScreen>(AppScreen.Home)

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _navigationStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.size - 1)
            _currentScreen.value = _navigationStack.last()
            return true
        }
        return false
    }

    // All Articles
    val allArticles: StateFlow<List<NewsArticle>> = repository.getAllArticlesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SampleNewsData.sampleArticles
        )

    // Bookmarks
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<NewsArticle>> = combine(allArticles, _searchQuery) { articles, query ->
        repository.searchArticles(query, articles)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Category filter for home tabs
    private val _selectedHomeCategory = MutableStateFlow("সব")
    val selectedHomeCategory: StateFlow<String> = _selectedHomeCategory.asStateFlow()

    fun setSelectedHomeCategory(catName: String) {
        _selectedHomeCategory.value = catName
    }

    // Reading Font scale: 0.9f (Small), 1.0f (Normal), 1.15f (Large), 1.3f (Extra Large)
    private val _articleFontScale = MutableStateFlow(1.0f)
    val articleFontScale: StateFlow<Float> = _articleFontScale.asStateFlow()

    fun increaseFontSize() {
        if (_articleFontScale.value < 1.35f) {
            _articleFontScale.value += 0.1f
        }
    }

    fun decreaseFontSize() {
        if (_articleFontScale.value > 0.85f) {
            _articleFontScale.value -= 0.1f
        }
    }

    // Bookmarking toggle
    fun toggleBookmark(article: NewsArticle) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = bookmarks.value.any { it.id == article.id }
            repository.toggleBookmark(article, isCurrentlyBookmarked)
        }
    }

    fun removeBookmark(articleId: String) {
        viewModelScope.launch {
            repository.removeBookmark(articleId)
        }
    }

    // Active Video Modal
    private val _activeVideo = MutableStateFlow<VideoNews?>(null)
    val activeVideo: StateFlow<VideoNews?> = _activeVideo.asStateFlow()

    fun openVideoPlayer(video: VideoNews) {
        _activeVideo.value = video
    }

    fun closeVideoPlayer() {
        _activeVideo.value = null
    }

    // Active Photo Lightbox
    private val _activePhoto = MutableStateFlow<PhotoItem?>(null)
    val activePhoto: StateFlow<PhotoItem?> = _activePhoto.asStateFlow()

    fun openPhotoViewer(photo: PhotoItem) {
        _activePhoto.value = photo
    }

    fun closePhotoViewer() {
        _activePhoto.value = null
    }

    // Notifications Dialog
    private val _showNotificationsDialog = MutableStateFlow(false)
    val showNotificationsDialog: StateFlow<Boolean> = _showNotificationsDialog.asStateFlow()

    fun toggleNotificationsDialog(show: Boolean) {
        _showNotificationsDialog.value = show
    }

    // Ticker pause/play
    private val _isTickerPlaying = MutableStateFlow(true)
    val isTickerPlaying: StateFlow<Boolean> = _isTickerPlaying.asStateFlow()

    fun toggleTickerPlay() {
        _isTickerPlaying.value = !_isTickerPlaying.value
    }

    // User Profile & Mock Authentication
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow("সদস্য পাঠক")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("reader@news24bd.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    fun login(email: String, name: String) {
        _userEmail.value = email
        _userName.value = if (name.isNotBlank()) name else "সদস্য পাঠক"
        _isUserLoggedIn.value = true
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    // Admin News Management
    fun createNewsArticle(
        title: String,
        summary: String,
        content: String,
        category: String,
        image: String,
        author: String,
        isBreaking: Boolean,
        isFeatured: Boolean,
        tags: String
    ) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale("bn", "BD")).format(Date())
            val newArticle = NewsArticle(
                id = "admin-" + System.currentTimeMillis(),
                title = title,
                slug = title.lowercase().replace(" ", "-").take(40),
                summary = summary,
                content = content,
                category = category,
                image = if (image.isNotBlank()) image else "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?auto=format&fit=crop&w=800&q=80",
                author = if (author.isNotBlank()) author else "স্টাফ রিপোর্টার",
                source = "NEWS24 BD ব্যুরো",
                publishedAt = dateStr,
                views = 1,
                featured = isFeatured,
                breaking = isBreaking,
                tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            )
            repository.addCustomArticle(newArticle)
        }
    }

    fun deleteArticle(articleId: String) {
        viewModelScope.launch {
            repository.deleteArticle(articleId)
        }
    }
}
