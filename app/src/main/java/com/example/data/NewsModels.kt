package com.example.data

data class NewsArticle(
    val id: String,
    val title: String,
    val slug: String,
    val summary: String,
    val content: String,
    val category: String,
    val image: String,
    val author: String,
    val source: String = "NEWS24 BD ব্যুরো",
    val publishedAt: String,
    val updatedAt: String = publishedAt,
    val views: Int = 1200,
    val featured: Boolean = false,
    val breaking: Boolean = false,
    val tags: List<String> = emptyList()
)

data class VideoNews(
    val id: String,
    val title: String,
    val duration: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val publishedAt: String,
    val views: Int = 3400
)

data class PhotoItem(
    val id: String,
    val title: String,
    val caption: String,
    val imageUrl: String,
    val photographer: String,
    val date: String
)

data class NewsCategory(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val isPrimary: Boolean = true
)
