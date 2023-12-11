package com.example.capstoneproject.ui.otp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class OTPEmailActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(OTPEmailActivity::class.java)

    @Test
    fun otpEmailTestEditText() {
        Espresso.onView(ViewMatchers.withId(R.id.etOtp))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun otpEmailTestButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnverifydotp))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnverifydotp)).perform(ViewActions.click())
    }

    @Test
    fun otpEmailTestTextView() {
        Espresso.onView(ViewMatchers.withId(R.id.showEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        /*Espresso.onView(ViewMatchers.withId(R.id.signupText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.reSendHere))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))*/
        /*Espresso.onView(ViewMatchers.withId(R.id.reSendHere)).perform(ViewActions.click())*/
    }
}