package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NewsRedPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewsHeader(
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onAccountClick: () -> Unit,
    unreadNotificationCount: Int = 3,
    modifier: Modifier = Modifier
) {
    val bengaliDateStr = remember {
        val bengaliDays = mapOf(
            "Sunday" to "রবিবার",
            "Monday" to "সোমবার",
            "Tuesday" to "মঙ্গলবার",
            "Wednesday" to "বুধবার",
            "Thursday" to "বৃহস্পতিবার",
            "Friday" to "শুক্রবার",
            "Saturday" to "শনিবার"
        )
        val bengaliMonths = mapOf(
            "January" to "জানুয়ারি",
            "February" to "ফেব্রুয়ারি",
            "March" to "মার্চ",
            "April" to "এপ্রিল",
            "May" to "মে",
            "June" to "জুন",
            "July" to "জুলাই",
            "August" to "আগস্ট",
            "September" to "সেপ্টেম্বর",
            "October" to "অক্টোবর",
            "November" to "নভেম্বর",
            "December" to "ডিসেম্বর"
        )
        val dayEn = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date())
        val monthEn = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Date())
        val dayNum = SimpleDateFormat("dd", Locale.ENGLISH).format(Date())
        val yearNum = SimpleDateFormat("yyyy", Locale.ENGLISH).format(Date())

        fun toBnDigits(input: String): String {
            val en = "0123456789"
            val bn = "০১২৩৪৫৬৭৮৯"
            return input.map { ch ->
                val idx = en.indexOf(ch)
                if (idx != -1) bn[idx] else ch
            }.joinToString("")
        }

        "${bengaliDays[dayEn] ?: dayEn}, ${toBnDigits(dayNum)} ${bengaliMonths[monthEn] ?: monthEn} ${toBnDigits(yearNum)}"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Top Date Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bengaliDateStr,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(Color(0xFF00C853), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "লাইভ সংস্করণ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF00C853),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Branding Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Tagline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("news_logo_badge")
                ) {
                    // Custom Brand Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NewsRedPrimary)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NEWS24",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BD",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFFFB300))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "বাংলা",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                        Text(
                            text = "সত্যের সন্ধানে নির্ভীক",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action Icons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.testTag("header_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "খবর অনুসন্ধান করুন",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onBookmarksClick,
                        modifier = Modifier.testTag("header_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = "সংরক্ষিত খবর",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.testTag("header_notifications_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Badge(containerColor = NewsRedPrimary) {
                                        Text(text = "$unreadNotificationCount", color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "জরুরি বিজ্ঞপ্তি",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(
                        onClick = onAccountClick,
                        modifier = Modifier.testTag("header_account_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "প্রোফাইল ও মেনু",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
