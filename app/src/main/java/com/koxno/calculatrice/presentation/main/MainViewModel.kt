package com.koxno.calculatrice.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koxno.calculatrice.domain.HistoryRepository
import com.koxno.calculatrice.domain.SettingsDao
import com.koxno.calculatrice.domain.calculateExpression
import com.koxno.calculatrice.domain.entity.HistoryItem
import com.koxno.calculatrice.domain.entity.ResultPanelType
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private var expression: String = ""

    private val _expressionState = MutableLiveData(ExpressionState(expression, 0))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.LEFT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private var _vibrationIntensity = MutableLiveData<Int>()
    val vibrationIntensity = _vibrationIntensity

    private var _precisionResult: Int = 3


    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _vibrationIntensity.value = settingsDao.getVibration()
        }
    }

    fun onNumberClicked(number: Int, selection: Int) {
        expression = putInSelection(number.toString(), selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onClearClicked() {
        expression = ""
        _expressionState.value = ExpressionState("", 0)
        _resultState.value = expression
    }

    fun onOperatorClicked(operator: Operator, selection: Int) {
        expression = putInSelection(operator.symbol, selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onDotClicked(selection: Int) {
        expression = putInSelection(".", selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onSqrtClicked(selection: Int) {
        expression = putInSelection("^(1/2)", selection)
        _expressionState.value = ExpressionState(expression, selection + 5)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onBraceLeftClicked(selection: Int) {
        expression = putInSelection("(", selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onBraceRightClicked(selection: Int) {
        expression = putInSelection(")", selection)
        _expressionState.value = ExpressionState(expression, selection + 1)
        _resultState.value = calculateExpression(expression, _precisionResult)
    }

    fun onBackClicked(selection: Int) {
        if (selection == 0) {
            return
        }
        expression = StringBuilder(expression).deleteAt(selection - 1).toString()
        _expressionState.value = ExpressionState(expression, selection - 1)
    }

    fun onEqualsClicked() {
        val result = calculateExpression(expression, _precisionResult)
        viewModelScope.launch {
            historyRepository.add(HistoryItem(expression, result))
        }
        _resultState.value = result
        _expressionState.value = ExpressionState(result, result.length)
        expression = result
    }

    fun onMemoryClicker() {
        val result = calculateExpression(expression, _precisionResult)
        viewModelScope.launch {
            historyRepository.add(HistoryItem(expression, result))
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "onCleared")
    }

    private fun putInSelection(put: String, selection: Int): String {
        return expression.substring(0, selection) +
                put +
                expression.substring(selection, expression.length)
    }

    fun onStart() {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionResult = settingsDao.getPrecision()
            _vibrationIntensity.value = settingsDao.getVibration()
        }
    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.expression
            _expressionState.value = ExpressionState(expression, expression.length)
            _resultState.value = item.result
        }
    }
}

enum class Operator(val symbol: String) {
    MINUS("-"), PLUS("+"), MULTIPLE("*"), DIVIDE("/"), EXPONENTIATION("^"), POINT(".")
}

class ExpressionState(val expression: String, val selection: Int) {

}

