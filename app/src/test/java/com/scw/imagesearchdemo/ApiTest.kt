package com.scw.imagesearchdemo

import com.scw.imagesearchdemo.di.module.apiModule
import com.scw.imagesearchdemo.network.api.PixabayApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class ApiTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(listOf(apiModule))
    }

    private val pixabayApi: PixabayApi by inject()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test() = runTest {
        pixabayApi.fetch("Yellow Flower")
            .catch { e -> Assert.fail(e.message) }
            .collect {
                Assert.assertTrue(it.total != 0)
                Assert.assertTrue(it.totalHits != 0)
                Assert.assertTrue(it.hits.isNotEmpty())
                it.hits.forEach { image ->
                    Assert.assertTrue(image.id != 0)
                    Assert.assertTrue(image.webformatURL.isNotEmpty())
                }
            }
    }
}