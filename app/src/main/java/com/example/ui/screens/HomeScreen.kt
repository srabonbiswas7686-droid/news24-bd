package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.NewsArticle
import com.example.data.PhotoItem
import com.example.data.SampleNewsData
import com.example.data.VideoNews
import com.example.ui.components.BreakingNewsTicker
import com.example.ui.components.FeaturedHeroNewsCard
import com.example.ui.components.PopularNewsCard
import com.example.ui.components.StandardNewsCard
import com.example.ui.components.toBengaliDigits
import com.example.ui.theme.NewsRedContainer
import com.example.ui.theme.NewsRedPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.NewsViewModel

@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val isTickerPlaying by viewModel.isTickerPlaying.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedHomeCategory.collectAsStateWithLifecycle()

    val breakingArticles = allArticles.filter { it.breaking }
    val featuredArticle = allArticles.firstOrNull { it.featured } ?: allArticles.firstOrNull()

    // Filtered articles according to selected category chip
    val filteredArticles = if (selectedCategory == "সব") {
        allArticles
    } else {
        allArticles.filter { it.category == selectedCategory }
    }

    val popularArticles = allArticles.sortedByDescending { it.views }.take(5)

    fun shareArticle(article: NewsArticle) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, article.title)
            putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.summary}\n\nপড়ুন NEWS24 BD তে: https://news24bd.com/news/${article.slug}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "খবরটি শেয়ার করুন")
        context.startActivity(shareIntent)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Breaking News Ticker
        item {
            BreakingNewsTicker(
                breakingArticles = breakingArticles,
                isPlaying = isTickerPlaying,
                onTogglePlay = { viewModel.toggleTickerPlay() },
                onArticleClick = { article ->
                    viewModel.navigateTo(AppScreen.ArticleDetail(article.id))
                }
            )
        }

        // 2. Category Quick Filters
        item {
            val filterOptions = listOf("সব") + SampleNewsData.categories.map { it.nameBn }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { catName ->
                    val isSelected = selectedCategory == catName
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedHomeCategory(catName) },
                        label = {
                            Text(
                                text = catName,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NewsRedPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            labelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }

        // 3. Featured Hero News Card
        if (featuredArticle != null && selectedCategory == "সব") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    FeaturedHeroNewsCard(
                        article = featuredArticle,
                        isBookmarked = bookmarks.any { it.id == featuredArticle.id },
                        onArticleClick = {
                            viewModel.navigateTo(AppScreen.ArticleDetail(featuredArticle.id))
                        },
                        onBookmarkToggle = { viewModel.toggleBookmark(featuredArticle) },
                        onShareClick = { shareArticle(featuredArticle) }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }

        // 4. Latest News Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(width = 4.dp, height = 18.dp)
                            .background(NewsRedPrimary, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedCategory == "সব") "সর্বশেষ সংবাদ" else "$selectedCategory এর খবর",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = { viewModel.navigateTo(AppScreen.Latest) }
                ) {
                    Text(
                        text = "আরও দেখুন",
                        color = NewsRedPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = NewsRedPrimary
                    )
                }
            }
        }

        // 5. Latest News Items
        val displayArticles = filteredArticles
            .filter { if (selectedCategory == "সব" && featuredArticle != null) it.id != featuredArticle.id else true }
            .take(5)

        items(displayArticles) { article ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                StandardNewsCard(
                    article = article,
                    onArticleClick = {
                        viewModel.navigateTo(AppScreen.ArticleDetail(article.id))
                    }
                )
            }
        }

        // 6. Popular News Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = NewsRedPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "জনপ্রিয় সংবাদ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                popularArticles.forEachIndexed { index, article ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        PopularNewsCard(
                            rank = index + 1,
                            article = article,
                            onArticleClick = {
                                viewModel.navigateTo(AppScreen.ArticleDetail(article.id))
                            }
                        )
                    }
                }
            }
        }

        // 7. Video News Section
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = NewsRedPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ভিডিও সংবাদ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = { viewModel.navigateTo(AppScreen.VideoSection) }
                ) {
                    Text(
                        text = "সব ভিডিও",
                        color = NewsRedPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(SampleNewsData.sampleVideos) { video ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .clickable { viewModel.openVideoPlayer(video) }
                            .testTag("home_video_card_${video.id}")
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(125.dp)
                            ) {
                                AsyncImage(
                                    model = video.thumbnailUrl,
                                    contentDescription = video.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.matchParentSize()
                                )
                                // Play badge
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(40.dp)
                                        .background(NewsRedPrimary.copy(alpha = 0.85f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "প্লে",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                // Duration tag
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                        .background(Color(0xCC000000), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = video.duration,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = video.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${toBengaliDigits(video.views)} ভিউ • ${video.publishedAt}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. Photo Gallery Section
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = NewsRedPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ছবিতে বাংলাদেশ ও বিশ্ব",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = { viewModel.navigateTo(AppScreen.PhotoGallery) }
                ) {
                    Text(
                        text = "গ্যালারি",
                        color = NewsRedPrimary,
                        fontSize = 13.sp
                    )
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(SampleNewsData.samplePhotos) { photo ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .clickable { viewModel.openPhotoViewer(photo) }
                            .testTag("home_photo_card_${photo.id}")
                    ) {
                        Column {
                            AsyncImage(
                                model = photo.imageUrl,
                                contentDescription = photo.caption,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = photo.title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = photo.photographer,
                                    fontSize = 11.sp,
                                    color = NewsRedPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 9. Clean Disclaimer & Footer
        item {
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NEWS24 BD",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = NewsRedPrimary
                )
                Text(
                    text = "সত্যের সন্ধানে সবসময় • মোবাইল ও ওয়েব সংস্করণ",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "© ২০২৬ NEWS24 BD। সর্বস্বত্ব সংরক্ষিত। এটি একটি উন্মুক্ত ডেমো নিউজ চ্যানেল অ্যাপ্লিকেশন।",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
