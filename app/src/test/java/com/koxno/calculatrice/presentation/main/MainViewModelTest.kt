package com.koxno.calculatrice.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.koxno.calculatrice.domain.SettingsDao
import com.koxno.calculatrice.domain.entity.ResultPanelType
import com.koxno.calculatrice.presentation.main.Operator.DIVIDE
import com.koxno.calculatrice.presentation.main.Operator.PLUS
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val settingsDao: SettingsDao = SettingsDaoFake()

    @Test
    fun testPlus() {
        val viewModel = MainViewModel(settingsDao)

        viewModel.onNumberClicked(2, 0)
        viewModel.onOperatorClicked(PLUS, 1)
        viewModel.onNumberClicked(2, 2)

        Assert.assertEquals("2+2", viewModel.expressionState.value?.expression)
        Assert.assertEquals("4", viewModel.resultState.value)
    }

    @Test
    fun testDivide() {
        val viewModel = MainViewModel(settingsDao)

        viewModel.onNumberClicked(1, 0)
        viewModel.onNumberClicked(0, 1)
        viewModel.onOperatorClicked(DIVIDE, 2)
        viewModel.onNumberClicked(2, 3)

        Assert.assertEquals("10/2", viewModel.expressionState.value?.expression)
        Assert.assertEquals("5", viewModel.resultState.value)
    }
}

class SettingsDaoFake : SettingsDao {

    private var resultPanelType: ResultPanelType = ResultPanelType.LEFT

    override suspend fun setResultPanelType(resultPanelType: ResultPanelType) {
        this.resultPanelType = resultPanelType
    }

    override suspend fun getResultPanelType(): ResultPanelType {
        return resultPanelType
    }
}