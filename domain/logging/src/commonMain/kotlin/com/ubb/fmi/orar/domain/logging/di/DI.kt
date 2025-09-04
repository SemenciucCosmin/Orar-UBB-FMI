package com.ubb.fmi.orar.domain.logging.di

import org.koin.core.module.Module

/**
 * Provides the platform-specific Koin module for logging domain operations.
 * This module includes the LoggingDomain for managing logging-related operations.
 */
expect fun loggingDomainModule(): Module