package com.example.coroutines28_12_22

import kotlinx.coroutines.*


class Utils(val dispatcher: Dispatchers){


    suspend fun getUsername():String{
        delay(1000)
        return "bhola"
    }

    suspend fun getUser():String{
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
        }
        return "Gupta"
    }


    suspend fun getAdress():String{
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
        }
        return "Adress"
    }



}