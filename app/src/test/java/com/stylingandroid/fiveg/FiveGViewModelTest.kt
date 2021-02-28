package com.stylingandroid.fiveg

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FiveGViewModelTest {

    lateinit var viewModel: FiveGViewModel

    @Test
    fun givenNullFiveGState_whenWeCollect_ThenStatusIsNoPermission() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(null)
        }
        val netFlow = flow {
            emit(false)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results, equalTo(listOf(Status.NoPermission)))
    }

    @Test
    fun givenFalseFiveGState_whenWeCollect_ThenStatusIsNoFiveG() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(false)
        }
        val netFlow = flow {
            emit(false)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results, equalTo(listOf(Status.FiveGStatus(is5G = false, isMetered = false))))
    }

    @Test
    fun givenTrueFiveGState_whenWeCollect_ThenStatusIsFiveG() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(true)
        }
        val netFlow = flow {
            emit(false)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results, equalTo(listOf(Status.FiveGStatus(is5G = true, isMetered = false))))
    }

    @Test
    fun givenFalseMeteredGState_whenWeCollect_ThenStatusIsNotMetered() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(true)
        }
        val netFlow = flow {
            emit(false)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results, equalTo(listOf(Status.FiveGStatus(is5G = true, isMetered = false))))
    }

    @Test
    fun givenTrueMeteredGState_whenWeCollect_ThenStatusIsMetered() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(true)
        }
        val netFlow = flow {
            emit(true)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results, equalTo(listOf(Status.FiveGStatus(is5G = true, isMetered = true))))
    }

    @Test
    fun givenMultipleEmissions_whenWeCollect_ThenFinalStatusIsCorrect() = runBlockingTest {
        val fiveGFlow = flow<Boolean?> {
            emit(null)
            emit(false)
            emit(true)
        }
        val netFlow = flow {
            emit(false)
            emit(true)
            emit(false)
        }

        viewModel = FiveGViewModel(fiveGFlow, netFlow)
        val results = viewModel.statusFlow.toList()

        assertThat(results.last(), equalTo(Status.FiveGStatus(is5G = true, isMetered = false)))
    }
}
