package com.koxno.calculatrice.domain.entity

data class SettingsBody(
    val resultPanelType: ResultPanelType,
    val vibrationIntensity: Int,
    val precision: Int,
)