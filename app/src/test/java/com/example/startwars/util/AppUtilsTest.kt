package com.example.startwars.util

import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppUtilsTest {
    @MockK
    lateinit var context: Context

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkObject(AppUtils)
    }

    @Test
    fun testInternetAvailableTrue(){
        every { AppUtils.hasInternetConnection(context) } returns true
        assertTrue(AppUtils.hasInternetConnection(context))
    }

    @Test
    fun testInternetAvailableFalse(){
        every { AppUtils.hasInternetConnection(context) } returns false
        assertFalse(AppUtils.hasInternetConnection(context))
    }
}