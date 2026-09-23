package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val category: String,
    val image: String,
    val author: String,
    val publishedAt: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)
