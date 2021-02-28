package com.stylingandroid.fiveg

sealed class Status {

    data class FiveGStatus(
        val is5G: Boolean,
        val isMetered: Boolean
    ) : Status()

    object NoPermission : Status()
}
