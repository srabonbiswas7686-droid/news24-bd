package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SampleNewsData
import com.example.ui.components.toBengaliDigits
import com.example.ui.theme.NewsRedPrimary
import com.example.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Add News, 2: Manage Articles

    // Add News Form State
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("বাংলাদেশ") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("স্টাফ রিপোর্টার") }
    var isBreaking by remember { mutableStateOf(false) }
    var isFeatured by remember { mutableStateOf(false) }
    var tags by remember { mutableStateOf("") }

    val totalViews = allArticles.sumOf { it.views }
    val breakingCount = allArticles.count { it.breaking }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ফিরে যান"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = NewsRedPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "নিউজ অ্যাডমিন ড্যাশবোর্ড",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "NEWS24 BD কন্টেন্ট ম্যানেজমেন্ট সিস্টেম",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Tabs
        item {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("ওভারভিউ", fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("সংবাদ যোগ", fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("সংবাদ তালিকা", fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Dashboard Overview Stats
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Icon(Icons.Default.Newspaper, contentDescription = null, tint = NewsRedPrimary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = toBengaliDigits(allArticles.size), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "মোট প্রকাশিত সংবাদ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF2E7D32))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = toBengaliDigits(totalViews), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "সর্বমোট পাঠক ভিউ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color(0xFFD50000))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = toBengaliDigits(breakingCount), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "ব্রেকিং নিউজ সক্রিয়", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Icon(Icons.Default.Category, contentDescription = null, tint = Color(0xFF1976D2))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = toBengaliDigits(SampleNewsData.categories.size), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "নিউজ ক্যাটাগরি", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "ডাটাবেজ ও ক্লাউড ইন্টিগ্রেশন নির্দেশনা:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "এই সিস্টেমটি সম্পূর্ণ মোবাইল ও অফলাইন সমর্থিত Room ডাটাবেজের সাথে সংযুক্ত। প্রজেক্টটিতে সরাসরি Firebase বা Supabase ব্যাকএন্ড প্লাগ-ইন করার আর্কিটেকচার প্রস্তুত রয়েছে।",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // Add News Form
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "নতুন সংবাদ তৈরি করুন",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("সংবাদের শিরোনাম (Headline)*") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_news_title_input")
                            )

                            // Category Selector Dropdown
                            ExposedDropdownMenuBox(
                                expanded = categoryExpanded,
                                onExpandedChange = { categoryExpanded = !categoryExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("ক্যাটাগরি নির্বাচন করুন") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false }
                                ) {
                                    SampleNewsData.categories.filter { it.id != "latest" }.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat.nameBn) },
                                            onClick = {
                                                selectedCategory = cat.nameBn
                                                categoryExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = summary,
                                onValueChange = { summary = it },
                                label = { Text("সংক্ষিপ্ত সারসংক্ষেপ (Summary)*") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_news_summary_input"),
                                maxLines = 3
                            )

                            OutlinedTextField(
                                value = content,
                                onValueChange = { content = it },
                                label = { Text("পূর্ণাঙ্গ সংবাদ প্রতিবেদন (Full Body)*") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .testTag("admin_news_content_input"),
                                maxLines = 8
                            )

                            OutlinedTextField(
                                value = imageUrl,
                                onValueChange = { imageUrl = it },
                                label = { Text("ছবির লিংক URL (ঐচ্ছিক)") },
                                placeholder = { Text("https://images.unsplash.com/...") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = author,
                                onValueChange = { author = it },
                                label = { Text("প্রতিবেদকের নাম") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = tags,
                                onValueChange = { tags = it },
                                label = { Text("ট্যাগ (কমা দিয়ে লিখুন)") },
                                placeholder = { Text("বাংলাদেশ, অর্থনীতি, উন্নয়ন") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Toggles
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isBreaking,
                                    onCheckedChange = { isBreaking = it },
                                    colors = CheckboxDefaults.colors(checkedColor = NewsRedPrimary)
                                )
                                Text(text = "ব্রেকিং নিউজ টিকারে দেখান", fontSize = 13.sp)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isFeatured,
                                    onCheckedChange = { isFeatured = it },
                                    colors = CheckboxDefaults.colors(checkedColor = NewsRedPrimary)
                                )
                                Text(text = "হোম পেজের প্রধান সংবাদ (Featured) হিসেবে চিহ্নিত করুন", fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (title.isNotBlank() && summary.isNotBlank() && content.isNotBlank()) {
                                        viewModel.createNewsArticle(
                                            title = title,
                                            summary = summary,
                                            content = content,
                                            category = selectedCategory,
                                            image = imageUrl,
                                            author = author,
                                            isBreaking = isBreaking,
                                            isFeatured = isFeatured,
                                            tags = tags
                                        )
                                        Toast.makeText(context, "সংবাদ সফলভাবে প্রকাশিত হয়েছে!", Toast.LENGTH_SHORT).show()
                                        // Reset fields
                                        title = ""
                                        summary = ""
                                        content = ""
                                        imageUrl = ""
                                        tags = ""
                                        isBreaking = false
                                        isFeatured = false
                                        selectedTab = 2 // Switch to list tab
                                    } else {
                                        Toast.makeText(context, "শিরোনাম, সারসংক্ষেপ ও প্রতিবেদন পূরণ করুন", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("admin_publish_news_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = NewsRedPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "সংবাদ প্রকাশ করুন", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            2 -> {
                // Manage Articles List
                items(allArticles) { article ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = article.category,
                                        color = NewsRedPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    if (article.breaking) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "• ব্রেকিং",
                                            color = Color(0xFFD50000),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = article.title.replace("[নমুনা] ", ""),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${article.publishedAt} • ${toBengaliDigits(article.views)} ভিউ",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (article.id.startsWith("admin-")) {
                                IconButton(
                                    onClick = {
                                        viewModel.deleteArticle(article.id)
                                        Toast.makeText(context, "সংবাদ মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "মুছুন",
                                        tint = NewsRedPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
