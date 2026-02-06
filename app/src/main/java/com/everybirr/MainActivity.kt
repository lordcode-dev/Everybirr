package com.everybirr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.everybirr.ui.navigation.EveryBirrNavHost
import com.everybirr.ui.theme.EveryBirrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EveryBirrTheme {
                EveryBirrNavHost()
            }
        }
    }
}
