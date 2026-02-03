package com.teto.planner.presentation.features.meeting_create

import com.teto.planner.domain.model.common.PageMeta
import com.teto.planner.domain.model.common.PagedList
import com.teto.planner.domain.model.common.Room
import com.teto.planner.domain.model.meeting.IntersectionGrid
import com.teto.planner.domain.model.meeting.Meeting
import com.teto.planner.domain.model.user.UserSummary
import java.time.LocalDate

sealed interface MeetingCreateUiState {
    data object Loading : MeetingCreateUiState

    data class Success(
        val searchQuery: String = "",
        val searchResults: List<UserSummary> = emptyList(),
        val searchMeta: PageMeta? = null,
        val selectedParticipants: List<UserSummary> = emptyList(),
        val isSearching: Boolean = false,

        val selectedDate: LocalDate = LocalDate.now(),
        val intersectionGrid: IntersectionGrid? = null,
        val selectedHour: Int? = null,

        val availableRooms: List<Room> = emptyList(),
        val roomsMeta: PageMeta? = null,
        val selectedRoomId: String? = null,
        val isLoadingRooms: Boolean = false,

        val title: String = "",
        val description: String = "",
        val durationHours: Int = 1,

        val isSubmitting: Boolean = false
    ) : MeetingCreateUiState {
        val canSubmit: Boolean get() = title.isNotBlank() &&
                selectedHour != null &&
                selectedParticipants.isNotEmpty() &&
                !isSubmitting
    }

    data class Error(val message: String) : MeetingCreateUiState
}
