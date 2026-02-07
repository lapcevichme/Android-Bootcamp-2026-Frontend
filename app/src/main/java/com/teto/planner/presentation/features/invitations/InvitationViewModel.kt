package com.teto.planner.presentation.features.invitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teto.planner.domain.model.meeting.Invitation
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvitationsUiState>(InvitationsUiState.Loading)
    val uiState: StateFlow<InvitationsUiState> = _uiState.asStateFlow()

    init {
        loadInvitations()
    }

    fun loadInvitations() {
        viewModelScope.launch {
            _uiState.value = InvitationsUiState.Loading

            repository.listInvitations(ParticipantStatus.PENDING).fold(
                onSuccess = { pagedList ->
                    if (pagedList.items.isEmpty()) {
                        _uiState.value = InvitationsUiState.Empty
                    } else {
                        _uiState.value = InvitationsUiState.Success(invitations = pagedList.items)
                    }
                },
                onFailure = { error ->
                    _uiState.value = InvitationsUiState.Error(error.message ?: "Неизвестная ошибка")
                }
            )
        }
    }

    fun onInvitationResponse(invitation: Invitation, isAccepted: Boolean) {
        val newStatus = if (isAccepted) ParticipantStatus.ACCEPTED else ParticipantStatus.DECLINED

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is InvitationsUiState.Success) {
                val updatedList = currentState.invitations.filter { it.meeting.id != invitation.meeting.id }

                if (updatedList.isEmpty()) {
                    _uiState.value = InvitationsUiState.Empty
                } else {
                    _uiState.value = InvitationsUiState.Success(invitations = updatedList)
                }
            }

            repository.respondToInvitation(
                meetingId = invitation.meeting.id,
                status = newStatus
            ).onFailure {
                loadInvitations()
            }
        }
    }
}