package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NewsRedPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.NewsViewModel

data class MenuItemData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val screen: AppScreen,
    val testTag: String
)

@Composable
fun MenuScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        MenuItemData(
            title = "সংরক্ষিত সংবাদ",
            subtitle = "আপনার পছন্দের সেভ করা প্রতিবেদন ও বুকমার্ক",
            icon = Icons.Default.Bookmark,
            iconTint = NewsRedPrimary,
            screen = AppScreen.Bookmarks,
            testTag = "menu_item_bookmarks"
        ),
        MenuItemData(
            title = "ভিডিও সংবাদ ও বুলেটিন",
            subtitle = "NEWS24 BD এর ভিডিও প্রতিবেদন ও বিশেষ সাক্ষাৎকার",
            icon = Icons.Default.VideoLibrary,
            iconTint = Color(0xFFD50000),
            screen = AppScreen.VideoSection,
            testTag = "menu_item_videos"
        ),
        MenuItemData(
            title = "ছবি গ্যালারি",
            subtitle = "ক্যামেরার লেন্সে বাংলাদেশ ও বিশ্বের নানা প্রান্ত",
            icon = Icons.Default.PhotoLibrary,
            iconTint = Color(0xFF1976D2),
            screen = AppScreen.PhotoGallery,
            testTag = "menu_item_photos"
        ),
        MenuItemData(
            title = "সংবাদ বিভাগসমূহ",
            subtitle = "বাংলাদেশ, রাজনীতি, অর্থনীতি, খেলাধুলা ও অন্যান্য",
            icon = Icons.Default.Category,
            iconTint = Color(0xFF388E3C),
            screen = AppScreen.Categories,
            testTag = "menu_item_categories"
        ),
        MenuItemData(
            title = "পাঠক অ্যাকাউন্ট ও প্রোফাইল",
            subtitle = "লগ ইন, নোটিফিকেশন অ্যালার্ট ও রিডিং পছন্দ",
            icon = Icons.Default.Person,
            iconTint = Color(0xFF7B1FA2),
            screen = AppScreen.UserAccount,
            testTag = "menu_item_account"
        ),
        MenuItemData(
            title = "অ্যাডমিন ম্যানেজমেন্ট প্যানেল",
            subtitle = "সংবাদ প্রকাশ, ব্রেকিং নিউজ সম্পাদন ও ড্যাশবোর্ড",
            icon = Icons.Default.AdminPanelSettings,
            iconTint = Color(0xFFF57C00),
            screen = AppScreen.AdminDashboard,
            testTag = "menu_item_admin"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("menu_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = "অ্যাপ মেনু ও এক্সপ্লোর",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "NEWS24 BD প্ল্যাটফর্মের সকল ফিচার ও সেটিংস",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(menuItems.size) { index ->
            val item = menuItems[index]
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(item.screen) }
                    .testTag(item.testTag)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(item.iconTint.copy(alpha = 0.12f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "খুলুন",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // About & Version Card
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = NewsRedPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NEWS24 BD সম্পর্কে",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "NEWS24 BD একটি আধুনিক ও স্বাধীন বাংলা সংবাদ প্ল্যাটফর্ম। এটি মোবাইল ও ওয়েব উভয়ের জন্য সম্পূর্ণ রেসপনসিভভাবে প্রস্তুত।\nভার্সন ১.০.০ (রিলিজ)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
