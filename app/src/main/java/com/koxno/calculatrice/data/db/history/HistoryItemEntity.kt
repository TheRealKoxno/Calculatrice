package com.koxno.calculatrice.data.db.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "history_item_entity")
class HistoryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val expression: String,
    val result: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)