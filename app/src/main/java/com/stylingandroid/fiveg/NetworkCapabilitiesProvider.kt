package com.stylingandroid.fiveg

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class NetworkCapabilitiesProvider @Inject constructor(
    flow: Flow<NetworkCapabilities>
) {

    private val notMeteredTypes = listOf(
        NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED,
        NetworkCapabilities.NET_CAPABILITY_NOT_METERED
    )

    companion object {
        fun getFlow(connectivityManager: ConnectivityManager) = callbackFlow<NetworkCapabilities> {
            println("Starting Capabilities Status flow")
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    println("Emitting Capabilities Status: $networkCapabilities")
                    offer(networkCapabilities)
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }
    }

    val isMeteredFlow = flow.map { notMeteredTypes.none(it::hasCapability) }
}
