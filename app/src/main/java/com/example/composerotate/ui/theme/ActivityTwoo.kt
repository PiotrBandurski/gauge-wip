package com.example.composerotate.ui.theme

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerotate.Greeting
import kotlinx.coroutines.*
import java.lang.Exception

class ActivityTwoo : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val vm: MyViewModel by viewModels()
            ComposeRotateTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Text(vm.a())
                }
            }
        }
    }
}

var d = 0
class MyViewModel: ViewModel() {

    init {
        with(viewModelScope){
            launch {
                while (true) {
                    Log.d("srag", "Job1")
                    delay(1000)
                }
            }
            launch {
                while (true) {
                    Log.d("srag", "Job2")
                    delay(900)
                }
            }

            launch {
            supervisorScope {
                val d = async {
                    delay(4000)
                    Log.d("srag", "exsc")
                    throw RuntimeException()
                }
                try {
                    d.await()
                } catch (exc: Exception) {
                    Log.d("srag", "catched")
                }
            }
            }

        }
    }

    fun a() = "aaa".toString()

    override fun onCleared() {
        super.onCleared()
        Log.d("vm", "onCleared")
    }
}