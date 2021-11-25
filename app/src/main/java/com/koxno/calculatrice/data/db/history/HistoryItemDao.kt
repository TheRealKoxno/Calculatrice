package com.koxno.calculatrice.data.db.history

import androidx.room.*
import com.koxno.calculatrice.data.db.typeConverters.LocalDateTimeConverter

@Dao
@TypeConverters(LocalDateTimeConverter::class)
interface HistoryItemDao {

    @Insert
    suspend fun insert(historyItemEntity: HistoryItemEntity)

    @Delete
    suspend fun delete(historyItemEntity: HistoryItemEntity)

    @Delete
    suspend fun delete(historyItemEntities: List<HistoryItemEntity>)

    @Query("SELECT * FROM history_item_entity")
    suspend fun getAll(): List<HistoryItemEntity>
}