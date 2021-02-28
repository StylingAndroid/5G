package com.stylingandroid.fiveg

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@HiltViewModel
class FiveGViewModel @Inject constructor(
    @Named("FiveG") fiveGFlow: Flow<Boolean?>,
    @Named("Metered") isMeteredFlow: Flow<Boolean>
) : ViewModel() {

    val statusFlow =
        fiveGFlow
            .combine(isMeteredFlow) { connection, isMetered ->
                println("Collecting 5G Status: $connection, $isMetered")
                if (connection == null) {
                    Status.NoPermission
                } else {
                    Status.FiveGStatus(
                        is5G = connection,
                        isMetered = isMetered
                    )
                }
            }
}
