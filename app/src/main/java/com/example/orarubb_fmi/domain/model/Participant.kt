package com.example.orarubb_fmi.domain.model

import androidx.core.text.isDigitsOnly

enum class Participant {
    SEMIGROUP_1,
    SEMIGROUP_2,
    WHOLE_GROUP,
    WHOLE_YEAR;

    companion object {
        fun getParticipantType(participant: String): Participant {
            return when  {
                participant.contains("/1") -> SEMIGROUP_1
                participant.contains("/2") -> SEMIGROUP_2
                participant.isDigitsOnly() -> WHOLE_GROUP
                else -> WHOLE_YEAR
            }
        }
    }
}
