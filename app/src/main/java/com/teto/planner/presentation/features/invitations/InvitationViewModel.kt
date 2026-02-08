package com.teto.planner.presentation.features.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.model.user.UserSummary
import com.teto.planner.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ActionError(
    val message: String,
    val organizer: UserSummary?
)

@HiltViewModel
class InvitationViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvitationUiState>(InvitationUiState.Loading)
    val uiState: StateFlow<InvitationUiState> = _uiState.asStateFlow()

    private val _actionError = MutableStateFlow<ActionError?>(null)
    val actionError: StateFlow<ActionError?> = _actionError.asStateFlow()

    init {
        loadInvitations()
    }

    fun loadInvitations() {
        viewModelScope.launch {
            _uiState.value = InvitationUiState.Loading

            repository.listInvitations(ParticipantStatus.PENDING).fold(
                onSuccess = { pagedList ->
                    if (pagedList.items.isEmpty()) {
                        _uiState.value = InvitationUiState.Empty
                    } else {
                        _uiState.value = InvitationUiState.Success(invitations = pagedList.items)
                    }
                },
                onFailure = { error ->
                    _uiState.value = InvitationUiState.Error(error.message ?: "Неизвестная ошибка")
                }
            )
        }
    }

    fun onInvitationResponse(invitation: Invitation, isAccepted: Boolean) {
        val newStatus = if (isAccepted) ParticipantStatus.ACCEPTED else ParticipantStatus.DECLINED

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is InvitationUiState.Success) {
                val updatedList = currentState.invitations.filter { it.meeting.id != invitation.meeting.id }

                if (updatedList.isEmpty()) {
                    _uiState.value = InvitationUiState.Empty
                } else {
                    _uiState.value = InvitationUiState.Success(invitations = updatedList)
                }
            }

            repository.respondToInvitation(
                meetingId = invitation.meeting.id,
                status = newStatus
            ).onFailure { error ->
                _actionError.value = ActionError(
                    message = error.message ?: "Ошибка обновления статуса",
                    organizer = invitation.meeting.organizer
                )
            }
        }
    }

    fun clearErrorAction() {
        _actionError.value = null
        loadInvitations()
    }
}