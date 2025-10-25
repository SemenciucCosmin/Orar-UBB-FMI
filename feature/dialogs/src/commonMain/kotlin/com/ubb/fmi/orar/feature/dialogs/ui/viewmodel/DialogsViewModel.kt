package com.ubb.fmi.orar.feature.dialogs.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.domain.feedback.usecase.GetFeedbackLoopReadinessUseCase
import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.model.DialogsUiEvent
import com.ubb.fmi.orar.ui.catalog.viewmodel.EventViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * ViewModel for displaying dialogs in main activity so that they overlay any screen
 */
class DialogsViewModel(
    private val getFeedbackLoopReadinessUseCase: GetFeedbackLoopReadinessUseCase,
) : EventViewModel<DialogsUiEvent>() {

    init {
        getFeedbackLoopReadiness()
    }

    private fun getFeedbackLoopReadiness() {
        viewModelScope.launch {
            getFeedbackLoopReadinessUseCase().collectLatest { isReady ->
                if (isReady) registerEvent(DialogsUiEvent.FEEDBACK_LOOP)
            }
        }
    }
}
