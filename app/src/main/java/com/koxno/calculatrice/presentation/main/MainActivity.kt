package com.koxno.calculatrice.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.koxno.calculatrice.R
import com.koxno.calculatrice.databinding.MainActivityBinding
import com.koxno.calculatrice.di.HistoryRepositoryProvider
import com.koxno.calculatrice.di.SettingsDaoProvider
import com.koxno.calculatrice.domain.entity.ResultPanelType.*
import com.koxno.calculatrice.presentation.common.BaseActivity
import com.koxno.calculatrice.presentation.history.HistoryResult
import com.koxno.calculatrice.presentation.settings.SettingsActivity

class MainActivity : BaseActivity() {

    private val viewBinding by viewBinding(MainActivityBinding::bind)
    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(
                    SettingsDaoProvider.getDao(this@MainActivity),
                    HistoryRepositoryProvider.get(this@MainActivity)
                ) as T
            }
        }
    }

    private val resultLauncher = registerForActivityResult(HistoryResult()) { item ->
        viewModel.onHistoryResult(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        viewBinding.mainInput.apply {
            showSoftInputOnFocus = false
        }
        viewBinding.mainActivitySettings.setOnClickListener {
            openSettings()
        }

        viewBinding.mainHistory?.setOnClickListener {
            openHistory()
        }

        viewModel.resultPanelState.observe(this) {
            with(viewBinding.mainResult) {
                gravity = when (it) {
                    LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    RIGHT -> Gravity.END.or(Gravity.CENTER_VERTICAL)
                    HIDE -> gravity
                }
                isVisible = it != HIDE
            }
        }

        listOf(
            viewBinding.mainZero,
            viewBinding.mainOne,
            viewBinding.mainTwo,
            viewBinding.mainThree,
            viewBinding.mainFour,
            viewBinding.mainFive,
            viewBinding.mainSix,
            viewBinding.mainSeven,
            viewBinding.mainEight,
            viewBinding.mainNine,
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewModel.onNumberClicked(index, viewBinding.mainInput.selectionStart)
                vibrate()
            }
        }

        mapOf(
            Operator.PLUS to viewBinding.mainPlus,
            Operator.MINUS to viewBinding.mainMinus,
            Operator.MULTIPLE to viewBinding.mainMultiple,
            Operator.DIVIDE to viewBinding.mainDivide,
            Operator.EXPONENTIATION to viewBinding.mainExponentiation,
            Operator.POINT to viewBinding.mainPoint
        ).forEach { (operator, textView) ->
            textView?.setOnClickListener {
                viewModel.onOperatorClicked(operator, viewBinding.mainInput.selectionStart)
                vibrate()
            }
        }

        viewBinding.mainSqrt?.setOnClickListener {
            viewModel.onSqrtClicked(viewBinding.mainInput.selectionStart)
            vibrate()
        }

        viewBinding.mainBraceLeft?.setOnClickListener {
            viewModel.onBraceLeftClicked(viewBinding.mainInput.selectionStart)
            vibrate()
        }

        viewBinding.mainBraceRight?.setOnClickListener {
            viewModel.onBraceRightClicked(viewBinding.mainInput.selectionStart)
            vibrate()
        }

        viewBinding.mainBack.setOnClickListener {
            viewModel.onBackClicked(viewBinding.mainInput.selectionStart)
            vibrate()
        }
        viewBinding.mainClear.setOnClickListener {
            viewModel.onClearClicked()
            vibrate()
        }
        viewBinding.mainEquals.setOnClickListener {
            viewModel.onEqualsClicked()
            vibrate()
        }
        viewBinding.mainMemory.setOnClickListener {
            viewModel.onMemoryClicker()
            vibrate()
        }

        viewBinding.mainPoint.setOnClickListener {
            viewModel.onDotClicked(viewBinding.mainInput.selectionStart)
        }

        viewModel.expressionState.observe(this) { state ->
            viewBinding.mainInput.setText(state.expression)
            viewBinding.mainInput.setSelection(state.selection)
        }
        viewModel.resultState.observe(this) { state ->
            viewBinding.mainResult.text = state.toString()
        }
        viewBinding.mainMinus.setOnClickListener {
            viewModel.onOperatorClicked(Operator.MINUS, viewBinding.mainInput.selectionStart)
        }
        viewBinding.mainPlus.setOnClickListener {
            viewModel.onOperatorClicked(Operator.PLUS, viewBinding.mainInput.selectionStart)
        }
        viewBinding.mainDivide.setOnClickListener {
            viewModel.onOperatorClicked(Operator.DIVIDE, viewBinding.mainInput.selectionStart)
        }
        viewBinding.mainPlus.setOnClickListener {
            viewModel.onOperatorClicked(Operator.PLUS, viewBinding.mainInput.selectionStart)
        }
        viewBinding.mainMultiple.setOnClickListener {
            viewModel.onOperatorClicked(Operator.MULTIPLE, viewBinding.mainInput.selectionStart)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))

    }

    private fun openHistory() {
        resultLauncher.launch()
    }

    private fun vibrate() {
        if (viewModel.vibrationIntensity.value ?: 0 > 0) {
            val vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibe.vibrate(
                VibrationEffect.createOneShot(
                    (viewModel.vibrationIntensity.value ?: 1).toLong(), 1
                )
            )
        }
    }
}


