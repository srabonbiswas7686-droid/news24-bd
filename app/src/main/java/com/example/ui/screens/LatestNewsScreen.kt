package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.StandardNewsCard
import com.example.ui.theme.NewsRedPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.NewsViewModel

@Composable
fun LatestNewsScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()
    var showOnlyBreaking by remember { mutableStateOf(false) }
    var visibleCount by remember { mutableIntStateOf(8) }

    val filteredList = if (showOnlyBreaking) {
        allArticles.filter { it.breaking }
    } else {
        allArticles
    }

    val displayArticles = filteredList.take(visibleCount)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("latest_news_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "সর্বশেষ সংবাদ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "মুহূর্তের দেশ ও বিদেশের তাজা খবর",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilterChip(
                    selected = showOnlyBreaking,
                    onClick = { showOnlyBreaking = !showOnlyBreaking },
                    label = { Text("শুধু ব্রেকিং", fontSize = 12.sp) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(displayArticles) { article ->
            StandardNewsCard(
                article = article,
                onArticleClick = {
                    viewModel.navigateTo(AppScreen.ArticleDetail(article.id))
                }
            )
        }

        if (visibleCount < filteredList.size) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { visibleCount += 6 },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("load_more_latest_news_btn"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "আরও সংবাদ দেখুন",
                        color = NewsRedPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
