package com.teto.planner.presentation.features.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        loadMeetingsForMonth(LocalDate.now())
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = _uiState.value
        if (currentState is ScheduleUiState.Success) {
            _uiState.update { currentState.copy(selectedDate = date) }
        }
    }

    fun loadMeetingsForMonth(date: LocalDate) {
        viewModelScope.launch {
            val currentDate = (uiState.value as? ScheduleUiState.Success)?.selectedDate ?: date

            _uiState.update { ScheduleUiState.Loading }

            val result = repository.getMeetings(
                startDate = date.withDayOfMonth(1),
                endDate = date.withDayOfMonth(date.lengthOfMonth())
            )

            result.onSuccess { pagedList ->
                _uiState.update {
                    ScheduleUiState.Success(
                        meetings = pagedList.items,
                        selectedDate = currentDate
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    ScheduleUiState.Error(error.message ?: "Неизвестная ошибка")
                }
            }
        }
    }
}
