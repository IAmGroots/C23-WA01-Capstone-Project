package com.example.capstoneproject.ui.otp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class OTPMobileActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(OTPMobileActivity::class.java)

    @Test
    fun otpMobileTestEditText() {
        onView(withId(R.id.etOtp)).check(matches(isDisplayed()))
    }

    @Test
    fun otpMobileTestButton() {
        onView(withId(R.id.btnverifydotp)).check(matches(isDisplayed()))

        onView(withId(R.id.btnverifydotp)).perform(click())
    }
}