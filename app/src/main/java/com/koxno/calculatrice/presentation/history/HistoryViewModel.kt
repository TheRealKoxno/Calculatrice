package com.koxno.calculatrice.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koxno.calculatrice.domain.HistoryRepository
import com.koxno.calculatrice.domain.entity.HistoryItem
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private val _historyItemsState = MutableLiveData<List<HistoryItem>>()
    val historyItemsState: LiveData<List<HistoryItem>> = _historyItemsState

    private val _closeWithResult = MutableLiveData<HistoryItem>()
    val closeWithResult: LiveData<HistoryItem> = _closeWithResult

    init {
        viewModelScope.launch {
            _historyItemsState.value = historyRepository.getAll()
        }
    }

    fun onItemClicked(historyItem: HistoryItem) {
        _closeWithResult.value = historyItem
    }
}

