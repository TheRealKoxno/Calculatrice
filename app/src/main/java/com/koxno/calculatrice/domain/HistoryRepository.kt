package com.koxno.calculatrice.domain

import com.koxno.calculatrice.domain.entity.HistoryItem

interface HistoryRepository {

    suspend fun add(historyItem: HistoryItem)

    suspend fun getAll(): List<HistoryItem>
}