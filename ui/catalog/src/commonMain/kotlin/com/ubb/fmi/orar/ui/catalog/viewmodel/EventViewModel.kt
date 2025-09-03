package com.ubb.fmi.orar.ui.catalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.seconds

/**
 * View model wrapper for ui event driven management.
 * The ui state of the view model that is extending this abstract class should
 * contain an enum (or other preferred model type) that implements interface [Event].
 * https://developer.android.com/topic/architecture/ui-layer/events
 */
abstract class EventViewModel<T : Event> : ViewModel() {
    private val _events: MutableStateFlow<ImmutableList<T>> = MutableStateFlow(
        persistentListOf()
    )

    val events = _events.asStateFlow()
        .onStart { onStart() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = _events.value
        )

    /**
     *  Registers another event into the queue.
     *  @param duplicateCondition - Lambda for preventing the addition of
     *  an already existing duplicate event.
     */
    protected fun registerEvent(event: T, duplicateCondition: (T) -> Boolean = { false }) {
        _events.update { events ->
            when {
                events.any(duplicateCondition) -> events

                else -> {
                    val newEvents = events.toMutableList().also { it.add(event) }
                    newEvents.toImmutableList()
                }
            }
        }
    }

    /**
     * Removes the first occurrence of the provided event from the queue.
     */
    fun unregisterEvent(event: T) {
        _events.update { events ->
            val newEvents = events.toMutableList().also { it.remove(event) }
            newEvents.toImmutableList()
        }
    }

    /**
     * Method to be implemented that is called before the flow of events starts being collected.
     */
    open fun onStart() { }
}
