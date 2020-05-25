package com.talk2us.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talk2us.R
import com.talk2us.utils.PrefManager


@Entity(tableName = "messages")
data class Message(
    @PrimaryKey @ColumnInfo(name = "message")
    val word: String,
    val timeStamp: String,
    var sent: Boolean,
    val seen: Boolean,
    val sentFrom: String,
    var messageId: String = PrefManager.getString(
        R.string.counsellor_id,
        "not_defined"
    ) + PrefManager.getString(R.string.client_id, "not_defined")

) {
    constructor() : this(
        "",
        "",
        false,
        false,
        "Client",
        PrefManager.getString(
            R.string.counsellor_id,
            "not_defined"
        ) + PrefManager.getString(R.string.client_id, "not_defined")
    )
}
