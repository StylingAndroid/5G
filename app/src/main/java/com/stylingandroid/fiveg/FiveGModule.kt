package com.stylingandroid.fiveg

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class FiveGModule {

    @Provides
    fun providesTelephonyManager(@ApplicationContext context: Context): TelephonyManager =
        context.getSystemService(TelephonyManager::class.java)

    @Provides
    fun providesTelephonyManagerFlow(telephonyManager: TelephonyManager) =
        FiveGConnectionProvider.getFlow(telephonyManager)

    @Provides
    @Named("FiveG")
    fun providesFiveGFlow(provider: FiveGConnectionProvider) =
        provider.fiveGFlow

    @Provides
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    @Provides
    fun providesNetworkFlow(connectivityManager: ConnectivityManager) =
        NetworkCapabilitiesProvider.getFlow(connectivityManager)

    @Provides
    @Named("Metered")
    fun providesIsMeteredFlow(provider: NetworkCapabilitiesProvider) =
        provider.isMeteredFlow
}
