package com.navercorp.android.languagetranslate.datasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "original_text")
    val originalText: String,

    @ColumnInfo(name = "translated_text")
    val translatedText: String,
)
