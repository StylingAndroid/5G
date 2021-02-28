package com.stylingandroid.fiveg

import android.telephony.PhoneStateListener
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class FiveGConnectionProvider @Inject constructor(flow: Flow<Int?>) {

    private val fiveGTypes = listOf(
        TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO,
        TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA,
        TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE
    )

    companion object {
        fun getFlow(telephonyManager: TelephonyManager) = callbackFlow {
            println("Starting 5G Status flow")
            val callback = object : PhoneStateListener() {
                override fun onDisplayInfoChanged(displayInfo: TelephonyDisplayInfo) {
                    try {
                        super.onDisplayInfoChanged(displayInfo)
                        println("Emitting 5G Status: ${displayInfo.networkType}")

                        offer(displayInfo.networkType)
                    } catch (e: SecurityException) {
                        println("Emitting 5G Status: <null>")
                        offer(null)
                    }
                }
            }
            try {
                offer(telephonyManager.dataNetworkType)
            } catch (e: SecurityException) {
                println("Emitting 5G Status: <null>")
                offer(null)
            }
            telephonyManager.listen(callback, PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED)
            awaitClose { telephonyManager.listen(callback, PhoneStateListener.LISTEN_NONE) }
        }
    }

    val fiveGFlow = flow.map { it?.let { fiveGTypes.contains(it) } }
}
