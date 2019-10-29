package com.example.repo.utils

import org.mockito.Mockito

/**
 * A kotlin friendly mock that handles generics
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)