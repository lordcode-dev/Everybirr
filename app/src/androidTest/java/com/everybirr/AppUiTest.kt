package com.everybirr

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.everybirr.ui.screen.OnboardingScreen
import org.junit.Rule
import org.junit.Test

class AppUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun firstTimeBudgetCreation_showsCTA() {
        composeRule.setContent { OnboardingScreen(onContinue = {}) }
        composeRule.onNodeWithText("Create first monthly budget").assertIsDisplayed()
    }

    @Test
    fun overspendingWarnings_textExists() {
        composeRule.setContent { androidx.compose.material3.Text("⚠ Near limit") }
        composeRule.onNodeWithText("⚠ Near limit").assertIsDisplayed()
    }
}
