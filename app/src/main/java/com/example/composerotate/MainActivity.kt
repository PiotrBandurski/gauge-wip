package com.example.composerotate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerotate.ui.theme.ActivityTwoo
import com.example.composerotate.ui.theme.ComposeRotateTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRotateTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                }
            }
        }
    }
}

var i = 0
@Composable
fun Greeting(
    name: String
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit){
        i++
        scope.launch {
            while (true){
                Log.d("compose", "Test life $i")
                delay(1000)
            }
        }

    }
    Text(text = "Hello $name!")
    Button(onClick = {context.startActivity(Intent(context, ActivityTwoo::class.java))}) {
        Text(text = "click me")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeRotateTheme {
        Greeting("Android")
    }
}

