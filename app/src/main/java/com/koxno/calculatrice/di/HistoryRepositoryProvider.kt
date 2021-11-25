package com.koxno.calculatrice.di

import android.content.Context
import com.koxno.calculatrice.data.HistoryRepositoryImpl
import com.koxno.calculatrice.domain.HistoryRepository

object HistoryRepositoryProvider {

    private var repository: HistoryRepository? = null

    fun get(context: Context): HistoryRepository =
        repository ?: HistoryRepositoryImpl(DatabaseProvider.get(context).historyItemDao)
            .also { repository = it }
}