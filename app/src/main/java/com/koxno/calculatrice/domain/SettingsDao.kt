package com.koxno.calculatrice.domain

import com.koxno.calculatrice.domain.entity.ResultPanelType

interface SettingsDao {

    suspend fun getResultPanelType(): ResultPanelType
    suspend fun setResultPanelType(resultPanelType: ResultPanelType)

    suspend fun getPrecision(): Int
    suspend fun setPrecision(precision: Int)

    suspend fun getVibration(): Int
    suspend fun setVibration(vibration: Int)

}

