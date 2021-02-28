package com.stylingandroid.fiveg

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED
import android.net.NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class NetworkCapabilitiesProviderTest {

    @Test
    fun testMeteredFlow() = runBlockingTest {
        val flow: Flow<NetworkCapabilities> = flowOf(
            createPresentCapabilities(NET_CAPABILITY_NOT_METERED),
            createPresentCapabilities(NET_CAPABILITY_TEMPORARILY_NOT_METERED),
            createPresentCapabilities(NET_CAPABILITY_NOT_METERED),
            createMissingCapabilities(
                NET_CAPABILITY_NOT_METERED,
                NET_CAPABILITY_TEMPORARILY_NOT_METERED
            )
        )
        val networkCapabilitiesProvider = NetworkCapabilitiesProvider(flow)
        val result = networkCapabilitiesProvider.isMeteredFlow.toList()
        assertThat(result, equalTo(listOf(false, false, false, true)))
    }

    private fun createPresentCapabilities(vararg present: Int) =
        mockk<NetworkCapabilities>().apply {
            every { hasCapability(any()) } returns false
            present.forEach {
                every { hasCapability(it) } returns true
            }
        }

    private fun createMissingCapabilities(vararg missing: Int) =
        mockk<NetworkCapabilities>().apply {
            every { hasCapability(any()) } returns true
            missing.forEach {
                every { hasCapability(it) } returns false
            }
        }
}
