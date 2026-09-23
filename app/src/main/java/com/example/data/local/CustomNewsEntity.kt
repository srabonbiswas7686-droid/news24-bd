package com.example.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "custom_news")
data class CustomNewsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val slug: String,
    val summary: String,
    val content: String,
    val category: String,
    val image: String,
    val author: String,
    val source: String,
    val publishedAt: String,
    val updatedAt: String,
    val views: Int,
    val featured: Boolean,
    val breaking: Boolean,
    val tagsCsv: String
)

@Dao
interface CustomNewsDao {
    @Query("SELECT * FROM custom_news ORDER BY publishedAt DESC")
    fun getAllCustomNews(): Flow<List<CustomNewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: CustomNewsEntity)

    @Query("DELETE FROM custom_news WHERE id = :newsId")
    suspend fun deleteNewsById(newsId: String)
}
