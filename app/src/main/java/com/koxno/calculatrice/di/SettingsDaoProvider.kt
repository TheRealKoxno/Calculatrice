package com.koxno.calculatrice.di

import android.content.Context
import com.koxno.calculatrice.data.SettingsDaoImpl
import com.koxno.calculatrice.domain.SettingsDao

object SettingsDaoProvider {

    private var dao: SettingsDao? = null

    fun getDao(context: Context): SettingsDao {
        return dao ?: run {
            val dao = SettingsDaoImpl(
                context.getSharedPreferences(
                    "settings",
                    Context.MODE_PRIVATE
                )
            )
            this.dao = dao
            dao
        }
    }
}

