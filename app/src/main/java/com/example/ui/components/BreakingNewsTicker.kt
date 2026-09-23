package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NewsArticle
import com.example.ui.theme.NewsRedDark
import com.example.ui.theme.NewsRedPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BreakingNewsTicker(
    breakingArticles: List<NewsArticle>,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    if (breakingArticles.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlaying, breakingArticles.size) {
        if (isPlaying && breakingArticles.isNotEmpty()) {
            while (true) {
                delay(4000)
                currentIndex = (currentIndex + 1) % breakingArticles.size
            }
        }
    }

    val currentArticle = breakingArticles.getOrNull(currentIndex) ?: breakingArticles.first()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NewsRedPrimary)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red Breaking News Pill Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(NewsRedDark, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ব্রেকিং নিউজ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = NewsRedPrimary
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Headline Text with smooth vertical scroll animation
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onArticleClick(currentArticle) }
                .testTag("breaking_ticker_headline")
        ) {
            AnimatedContent(
                targetState = currentArticle,
                transitionSpec = {
                    slideInVertically { height -> height } togetherWith
                            slideOutVertically { height -> -height }
                },
                label = "TickerTransition"
            ) { article ->
                Text(
                    text = article.title.replace("[নমুনা] ", ""),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp
                )
            }
        }

        // Pause/Play Button
        IconButton(
            onClick = onTogglePlay,
            modifier = Modifier
                .size(28.dp)
                .testTag("ticker_pause_play_btn")
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "স্থগিত করুন" else "চালু করুন",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
