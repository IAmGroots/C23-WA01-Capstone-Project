package com.example.capstoneproject.ui.splash

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class SplashActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun testSplash() {
        Espresso.onView(ViewMatchers.withId(R.id.splash))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}