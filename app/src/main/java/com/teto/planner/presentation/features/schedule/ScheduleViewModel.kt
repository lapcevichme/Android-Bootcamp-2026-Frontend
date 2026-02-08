package com.teto.planner.presentation.features.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private val loadedMonths = mutableSetOf<YearMonth>()
    private var currentUserId: String? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val userResult = repository.getCurrentUserId()
            userResult.onSuccess { id ->
                currentUserId = id
                loadMeetingsForMonth(LocalDate.now())
            }.onFailure {
                _uiState.update { ScheduleUiState.Error("Не удалось загрузить профиль пользователя") }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        val currentState = _uiState.value
        if (currentState is ScheduleUiState.Success) {
            _uiState.update { currentState.copy(selectedDate = date) }
        }
    }

    fun refresh() {
        val currentState = uiState.value
        if (currentState is ScheduleUiState.Success) {
            val monthToRefresh = YearMonth.from(currentState.selectedDate)
            loadedMonths.remove(monthToRefresh)

            loadMeetingsForMonth(currentState.selectedDate, isPullToRefresh = true)
        } else {
            loadInitialData()
        }
    }

    fun loadMeetingsForMonth(date: LocalDate, isPullToRefresh: Boolean = false) {
        val userId = currentUserId ?: return

        val monthToLoad = YearMonth.from(date)

        if (!isPullToRefresh && loadedMonths.contains(monthToLoad)) {
            return
        }

        viewModelScope.launch {
            val currentState = uiState.value

            val currentMeetings = if (currentState is ScheduleUiState.Success) {
                _uiState.update {
                    currentState.copy(
                        isRefreshing = isPullToRefresh,
                        isLoading = !isPullToRefresh
                    )
                }
                currentState.meetings
            } else {
                _uiState.update { ScheduleUiState.Loading }
                emptyList()
            }

            val currentDate = (currentState as? ScheduleUiState.Success)?.selectedDate ?: date
            val newMeetings = mutableListOf<Meeting>()
            var currentPage = 0
            var hasNextPage = true
            var errorOccurred: String? = null
            val pageSize = 50

            val startDate = date.withDayOfMonth(1)
            val endDate = date.withDayOfMonth(date.lengthOfMonth())

            while (hasNextPage) {
                val result = repository.getMeetings(
                    startDate = startDate,
                    endDate = endDate,
                    page = currentPage,
                    size = pageSize
                )

                result.onSuccess { pagedList ->
                    newMeetings.addAll(pagedList.items)
                    hasNextPage = pagedList.meta.hasNext
                    currentPage++
                }.onFailure { error ->
                    hasNextPage = false
                    errorOccurred = error.message
                }

                if (currentPage > 20) hasNextPage = false
            }

            if (errorOccurred != null && currentMeetings.isEmpty() && newMeetings.isEmpty()) {
                _uiState.update {
                    ScheduleUiState.Error(errorOccurred)
                }
            } else {
                if (errorOccurred == null) {
                    loadedMonths.add(monthToLoad)
                }

                val updatedList = (newMeetings + currentMeetings).distinctBy { it.id }

                _uiState.update {
                    ScheduleUiState.Success(
                        meetings = updatedList,
                        currentUserId = userId,
                        selectedDate = currentDate,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun openMeetingDetails(summaryMeeting: Meeting) {
        val currentState = _uiState.value as? ScheduleUiState.Success ?: return
        _uiState.update {
            currentState.copy(
                selectedMeeting = summaryMeeting,
                isMeetingDetailsLoading = false
            )
        }
    }

    fun closeMeetingDetails() {
        val currentState = _uiState.value as? ScheduleUiState.Success ?: return
        _uiState.update {
            currentState.copy(selectedMeeting = null, isMeetingDetailsLoading = false)
        }
    }

    fun onEditMeetingClick() {
        val currentState = _uiState.value as? ScheduleUiState.Success ?: return
        val meeting = currentState.selectedMeeting ?: return

        _uiState.update {
            currentState.copy(
                selectedMeeting = null,
                meetingToEdit = meeting
            )
        }
    }

    fun closeEditDialog() {
        val currentState = _uiState.value as? ScheduleUiState.Success ?: return
        _uiState.update { currentState.copy(meetingToEdit = null) }
    }

    fun updateMeeting(meetingId: String, title: String, description: String) {
        val currentState = _uiState.value as? ScheduleUiState.Success ?: return

        viewModelScope.launch {
            _uiState.update { currentState.copy(isLoading = true) }

            val result = repository.updateMeeting(
                meetingId = meetingId,
                title = title,
                description = description
            )

            result.onSuccess { updatedMeeting ->
                val updatedList = currentState.meetings.map {
                    if (it.id == updatedMeeting.id) updatedMeeting else it
                }

                _uiState.update {
                    currentState.copy(
                        meetings = updatedList,
                        meetingToEdit = null,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update { currentState.copy(isLoading = false) }
            }
        }
    }
}