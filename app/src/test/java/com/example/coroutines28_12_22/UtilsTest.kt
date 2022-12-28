package com.example.coroutines28_12_22

import junit.framework.Assert.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before

import org.junit.Test


class UtilsTest {

    val scope = TestScope()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun getAdress() {
        val uti = Utils(Dispatchers)
        runTest {
            uti.getAdress()
        }
    }

    @Test
    fun getUsername() {
        val it = Utils(Dispatchers)
        runTest {
            it.getUsername()
        }
    }

    @Test
    fun testSubject() = scope.runTest {
        val cil = Utils(Dispatchers)
        cil.getUser()
    }


    //launch and async
    @Test
    fun testWithMultipleDelays() = runTest {
        launch {
            delay(1000)
            println("1. $currentTime")
            delay(200)
            println("2. $currentTime")
            delay(2000)
            println("4. $currentTime")
        }
        val deferred = async {
            delay(3000)
            println("3. $currentTime") // 3000
            delay(500)
            println("5. $currentTime") // 3500
        }
        deferred.await()
    }


    //Controlling the virtual time

    @Test
    fun testFoo() = runTest {
        launch {
            println(1)
            delay(1000)
            println(2)
            delay(500)
            println(3)
            delay(5000)
            println(4)
        }

        runCurrent()

        advanceTimeBy(2000)

        advanceUntilIdle()
        assertEquals(6500, currentTime)
    }


    //
    @Test
    fun testEagerlyEnteringChildCoroutines() = runTest(UnconfinedTestDispatcher()) {
        var entered = false
        val deferred = CompletableDeferred<Unit>()
        var completed = false
        launch {
            entered = true
            deferred.await()
            completed = true
        }
        assertTrue(entered)
        assertFalse(completed)
        deferred.complete(Unit)
        assertTrue(completed)
    }



    //Using multiple test dispatchers
    @Test
    fun testWithMultipleDispatchers() = runTest {
        val scheduler = testScheduler // the scheduler used for this test
        val dispatcher1 = StandardTestDispatcher(scheduler, name = "IO dispatcher")
        val dispatcher2 = StandardTestDispatcher(scheduler, name = "Background dispatcher")
        launch(dispatcher1) {
            delay(1000)
            println("1. $currentTime") // 1000
            delay(200)
            println("2. $currentTime") // 1200
            delay(2000)
            println("4. $currentTime") // 3200
        }
        val deferred = async(dispatcher2) {
            delay(3000)
            println("3. $currentTime") // 3000
            delay(500)
            println("5. $currentTime") // 3500
        }
        deferred.await()
    }



}
