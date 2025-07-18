package com.zenhealth.remindrdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zenhealth.remindr.Remindr
import com.zenhealth.remindr.utils.RemindrLogger
import com.zenhealth.remindrdemo.theme.RemindrDemoTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RemindrLogger.isLoggingEnabled = true
        setContent {
            RemindrDemoTheme {
                RemindrDemoApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RemindrDemoTheme {
        RemindrDemoApp()
    }
}