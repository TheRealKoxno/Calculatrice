package com.koxno.calculatrice.presentation.settings

import android.app.AlertDialog
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.koxno.calculatrice.R
import com.koxno.calculatrice.databinding.SettingsActivityBinding
import com.koxno.calculatrice.di.SettingsDaoProvider
import com.koxno.calculatrice.domain.entity.ResultPanelType
import com.koxno.calculatrice.presentation.common.BaseActivity

class SettingsActivity : BaseActivity() {

    private val viewBinding by viewBinding(SettingsActivityBinding::bind)
    private val viewModel by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SettingsViewModel(SettingsDaoProvider.getDao(this@SettingsActivity)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        viewBinding.settingsBack.setOnClickListener {
            finish()
        }

        viewBinding.resultPanelContainer.setOnClickListener {
            viewModel.onResultPanelTypeClicked()
        }

        viewBinding.precisionValue.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewBinding.precisionValueText.text = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onPrecisionChanged(seekBar.progress)
            }
        })

        viewBinding.vibrationValue.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                viewBinding.vibrationValueText.text = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.onVibrationChanged(seekBar.progress)
            }
        })


        viewModel.resultPanelState.observe(this) { state ->
            viewBinding.resultPanelDescription.text =
                resources.getStringArray(R.array.result_panel_types)[state.ordinal]
        }

        viewModel.precisionResult.observe(this) {
            viewBinding.precisionValue.progress = it
        }

        viewModel.vibrationIntensity.observe(this) {
            viewBinding.vibrationValue.progress = it
        }

        viewModel.openResultPanelAction.observe(this) { type ->
            showDialog(type)
        }
    }

    private fun showDialog(type: ResultPanelType) {
        var result: Int? = null
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(getString(R.string.settings_result_panel_title))
            setPositiveButton("ОК") { dialog, id ->
                result?.let { viewModel.onResultPanelTypeChanged(ResultPanelType.values()[it]) }
            }
            setNegativeButton("Отмена") { dialog, id ->
            }
            setSingleChoiceItems(R.array.result_panel_types, type.ordinal) { dialog, id ->
                result = id
            }
                .show()
        }
    }
}

