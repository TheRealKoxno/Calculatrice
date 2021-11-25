package com.koxno.calculatrice.data

import com.koxno.calculatrice.data.db.history.HistoryItemDao
import com.koxno.calculatrice.data.db.history.HistoryItemEntity
import com.koxno.calculatrice.domain.HistoryRepository
import com.koxno.calculatrice.domain.entity.HistoryItem

class HistoryRepositoryImpl(
    private val historyItemDao: HistoryItemDao,
) : HistoryRepository {

    override suspend fun add(historyItem: HistoryItem) {
        historyItemDao.insert(historyItem.toHistoryItemEntity())
    }

    override suspend fun getAll(): List<HistoryItem> =
        historyItemDao.getAll().map { it.toHistoryItem() }

    private fun HistoryItem.toHistoryItemEntity() = HistoryItemEntity(
        id = 0,
        expression = expression,
        result = result
    )

    private fun HistoryItemEntity.toHistoryItem() = HistoryItem(
        expression = expression,
        result = result
    )

}