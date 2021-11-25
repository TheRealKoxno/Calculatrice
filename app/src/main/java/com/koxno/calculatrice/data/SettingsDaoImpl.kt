package com.koxno.calculatrice.data

import android.content.SharedPreferences
import com.koxno.calculatrice.domain.SettingsDao
import com.koxno.calculatrice.domain.entity.ResultPanelType
import com.koxno.calculatrice.domain.entity.ResultPanelType.LEFT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsDaoImpl(
    private val preferences: SharedPreferences,
) : SettingsDao {

    override suspend fun setResultPanelType(resultPanelType: ResultPanelType) =
        withContext(Dispatchers.IO) {
            preferences.edit().putString(RESULT_PANEL_TYPE_KEY, resultPanelType.name).apply()
        }

    override suspend fun getResultPanelType(): ResultPanelType = withContext(Dispatchers.IO) {
        preferences.getString(RESULT_PANEL_TYPE_KEY, null)
            ?.let { ResultPanelType.valueOf(it) } ?: LEFT
    }

    override suspend fun getPrecision(): Int {
        return preferences.getInt(PRECISION_NUMBER_KEY, 3)
    }

    override suspend fun setPrecision(precisionNumber: Int) {
        preferences.edit().putInt(PRECISION_NUMBER_KEY, precisionNumber).apply()
    }

    override suspend fun getVibration(): Int {
        return preferences.getInt(VIBRATION_KEY, 0)
    }

    override suspend fun setVibration(vibration: Int) {
        preferences.edit().putInt(VIBRATION_KEY, vibration).apply()
    }

    companion object {

        private const val RESULT_PANEL_TYPE_KEY = "RESULT_PANEL_TYPE_KEY"
        private const val PRECISION_NUMBER_KEY = "PRECISION_NUMBER_KEY"
        private const val VIBRATION_KEY = "VIBRATION_KEY"
    }
}

