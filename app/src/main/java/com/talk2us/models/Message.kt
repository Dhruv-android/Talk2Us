package com.talk2us.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp


@Entity(tableName = "messages")
data class Message(
    @PrimaryKey @ColumnInfo(name = "message")
    val word: String,
    val timestamp: String,
    var sent: Boolean,
    val seen: Boolean
)
