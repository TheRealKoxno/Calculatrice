package com.koxno.calculatrice.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koxno.calculatrice.domain.SettingsDao
import com.koxno.calculatrice.domain.entity.ResultPanelType
import com.koxno.calculatrice.presentation.common.SingleLiveEvent
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDao: SettingsDao,
) : ViewModel() {

    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.RIGHT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = SingleLiveEvent<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    private val _precisionResult = MutableLiveData<Int>()
    val precisionResult = _precisionResult

    private val _vibrationIntensity = MutableLiveData<Int>()
    val vibrationIntensity = _vibrationIntensity

    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionResult.value = settingsDao.getPrecision()
            _vibrationIntensity.value = settingsDao.getVibration()
        }
    }

    fun onResultPanelTypeClicked() {
        _openResultPanelAction.value = resultPanelState.value
    }


    fun onResultPanelTypeChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }
    }

    fun onPrecisionChanged(precision: Int) {
        _precisionResult.value = precision
        viewModelScope.launch {
            settingsDao.setPrecision(precision)
        }
    }

    fun onVibrationChanged(vibration: Int) {
        _vibrationIntensity.value = vibration
        viewModelScope.launch {
            settingsDao.setVibration(vibration)
        }
    }

}

