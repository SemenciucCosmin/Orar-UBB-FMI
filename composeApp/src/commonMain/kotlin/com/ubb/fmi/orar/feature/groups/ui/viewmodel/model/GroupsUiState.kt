package com.ubb.fmi.orar.feature.groups.ui.viewmodel.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class GroupsUiState(
    val groups: ImmutableList<String> = persistentListOf(),
    val title: String? = null,
    val subtitle: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
