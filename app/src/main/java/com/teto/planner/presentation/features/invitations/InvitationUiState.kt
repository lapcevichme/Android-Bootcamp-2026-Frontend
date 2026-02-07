package com.teto.planner.presentation.features.invitations

import com.teto.planner.domain.model.meeting.Invitation

sealed interface InvitationsUiState {
    object Loading : InvitationsUiState

    data class Success(
        val invitations: List<Invitation> = emptyList()
    ) : InvitationsUiState

    data class Error(val message: String) : InvitationsUiState

    object Empty : InvitationsUiState
}