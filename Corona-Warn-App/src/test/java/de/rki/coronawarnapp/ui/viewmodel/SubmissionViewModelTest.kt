package de.rki.coronawarnapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.rki.coronawarnapp.storage.LocalData
import de.rki.coronawarnapp.ui.submission.ScanStatus
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SubmissionViewModelTest {
    private var viewModel: SubmissionViewModel = SubmissionViewModel()

    @JvmField @Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockkObject(LocalData)
        every { LocalData.testGUID(any()) } just Runs
    }

    @Test
    fun scanStatusValid() {

        // start
        viewModel.scanStatus.value?.getContent().let { Assert.assertEquals(ScanStatus.STARTED, it) }

        // valid guid
        val guid = "123456-12345678-1234-4DA7-B166-B86D85475064"
        viewModel.validateAndStoreTestGUID("https://bs-sd.de/covid-19/?$guid")
        viewModel.scanStatus.value?.getContent().let { Assert.assertEquals(ScanStatus.SUCCESS, it) }

        // invalid guid
        viewModel.validateAndStoreTestGUID("https://no-guid-here")
        viewModel.scanStatus.value?.getContent().let { Assert.assertEquals(ScanStatus.INVALID, it) }

    }
}