package com.stylingandroid.fiveg

import android.telephony.TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO
import android.telephony.TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NONE
import android.telephony.TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA
import android.telephony.TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FiveGConnectionProviderTest {

    @Test
    fun testFiveGNetworkFlow() = runBlockingTest {
        val flow = flowOf(
            OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO,
            OVERRIDE_NETWORK_TYPE_NR_NSA,
            OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE,
            null,
            OVERRIDE_NETWORK_TYPE_NONE
        )
        val fiveGConnectionProvider = FiveGConnectionProvider(flow)
        val result = fiveGConnectionProvider.fiveGFlow.toList()
        assertThat(result, equalTo(listOf(true, true, true, null, false)))
    }
}
