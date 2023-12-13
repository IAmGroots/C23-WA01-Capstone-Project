package com.example.capstoneproject.ui.otp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class OTPEmailActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(OTPEmailActivity::class.java)

    @Test
    fun otpEmailTestInValid() {
        onView(withId(R.id.etOtp)).perform(ViewActions.typeText("1234")) // otp tidak valid

        onView(withId(R.id.btnverifydotp)).perform(click())
        onView(withId(R.id.etOtp)).check(matches(hasErrorText("OTP code must be 6 numbers!"))) // errorText
    }

    @Test
    fun otpEmailTestValid() {
        onView(withId(R.id.etOtp)).perform(ViewActions.typeText("123456"), closeSoftKeyboard())//otp valid

        onView(withId(R.id.btnverifydotp)).perform(click())
    }

    @Test
    fun otpEmailTestDisplayUI() {
        onView(withId(R.id.etOtp)).check(matches(isDisplayed()))
        onView(withId(R.id.btnverifydotp)).check(matches(isDisplayed()))
    }
}