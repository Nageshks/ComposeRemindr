package com.zenhealth.remindrdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.zenhealth.remindr.RemindrLogger
import com.zenhealth.remindrdemo.ui.theme.RemindrDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        RemindrLogger.isLoggingEnabled = true
        setContent {
            RemindrDemoTheme {
                RemindrApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RemindrDemoTheme {
        RemindrApp()
    }
}