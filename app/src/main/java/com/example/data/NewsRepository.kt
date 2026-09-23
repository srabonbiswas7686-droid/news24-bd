package com.example.data

import com.example.data.local.BookmarkDao
import com.example.data.local.BookmarkEntity
import com.example.data.local.CustomNewsDao
import com.example.data.local.CustomNewsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val bookmarkDao: BookmarkDao,
    private val customNewsDao: CustomNewsDao
) {
    // Combine built-in sample articles with user-added/admin custom articles
    fun getAllArticlesFlow(): Flow<List<NewsArticle>> {
        return customNewsDao.getAllCustomNews().map { customEntities ->
            val customArticles = customEntities.map { entity ->
                NewsArticle(
                    id = entity.id,
                    title = entity.title,
                    slug = entity.slug,
                    summary = entity.summary,
                    content = entity.content,
                    category = entity.category,
                    image = entity.image,
                    author = entity.author,
                    source = entity.source,
                    publishedAt = entity.publishedAt,
                    updatedAt = entity.updatedAt,
                    views = entity.views,
                    featured = entity.featured,
                    breaking = entity.breaking,
                    tags = entity.tagsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                )
            }
            // Custom articles first, then sample articles
            customArticles + SampleNewsData.sampleArticles
        }
    }

    fun getArticleById(id: String, allArticles: List<NewsArticle>): NewsArticle? {
        return allArticles.find { it.id == id }
    }

    fun getBreakingArticles(allArticles: List<NewsArticle>): List<NewsArticle> {
        return allArticles.filter { it.breaking }
    }

    fun getFeaturedArticle(allArticles: List<NewsArticle>): NewsArticle? {
        return allArticles.find { it.featured } ?: allArticles.firstOrNull()
    }

    fun getPopularArticles(allArticles: List<NewsArticle>): List<NewsArticle> {
        return allArticles.sortedByDescending { it.views }.take(6)
    }

    fun getArticlesByCategory(category: String, allArticles: List<NewsArticle>): List<NewsArticle> {
        if (category == "latest" || category == "সর্বশেষ" || category == "সব") {
            return allArticles
        }
        return allArticles.filter {
            it.category.equals(category, ignoreCase = true) ||
            SampleNewsData.categories.find { cat -> cat.id == category }?.nameBn == it.category
        }
    }

    fun searchArticles(query: String, allArticles: List<NewsArticle>): List<NewsArticle> {
        if (query.isBlank()) return emptyList()
        val q = query.trim().lowercase()
        return allArticles.filter { article ->
            article.title.lowercase().contains(q) ||
            article.summary.lowercase().contains(q) ||
            article.content.lowercase().contains(q) ||
            article.category.lowercase().contains(q) ||
            article.tags.any { it.lowercase().contains(q) }
        }
    }

    // Bookmarks Flow
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    fun isBookmarked(id: String): Flow<Boolean> = bookmarkDao.isBookmarked(id)

    suspend fun toggleBookmark(article: NewsArticle, currentlyBookmarked: Boolean) {
        if (currentlyBookmarked) {
            bookmarkDao.deleteBookmarkById(article.id)
        } else {
            bookmarkDao.insertBookmark(
                BookmarkEntity(
                    id = article.id,
                    title = article.title,
                    summary = article.summary,
                    category = article.category,
                    image = article.image,
                    author = article.author,
                    publishedAt = article.publishedAt
                )
            )
        }
    }

    suspend fun removeBookmark(id: String) {
        bookmarkDao.deleteBookmarkById(id)
    }

    // Admin Operations
    suspend fun addCustomArticle(article: NewsArticle) {
        customNewsDao.insertNews(
            CustomNewsEntity(
                id = article.id,
                title = article.title,
                slug = article.slug,
                summary = article.summary,
                content = article.content,
                category = article.category,
                image = article.image,
                author = article.author,
                source = article.source,
                publishedAt = article.publishedAt,
                updatedAt = article.updatedAt,
                views = article.views,
                featured = article.featured,
                breaking = article.breaking,
                tagsCsv = article.tags.joinToString(",")
            )
        )
    }

    suspend fun deleteArticle(id: String) {
        customNewsDao.deleteNewsById(id)
    }
}
