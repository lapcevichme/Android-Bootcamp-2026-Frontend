package com.teto.planner.domain.model.meeting

import com.teto.planner.domain.model.user.ParticipantRole
import com.teto.planner.domain.model.user.ParticipantStatus
import com.teto.planner.domain.model.user.UserSummary

data class MeetingParticipant(
    val user: UserSummary,
    val role: ParticipantRole,
    val status: ParticipantStatus
)