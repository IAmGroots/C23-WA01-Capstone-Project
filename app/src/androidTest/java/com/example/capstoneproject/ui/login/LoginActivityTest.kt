package com.example.capstoneproject.ui.login

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginTestEditText() {
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
    }

    @Test
    fun loginTestButton() {
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun loginTestTextView() {
        onView(withId(R.id.textforgotpassword)).check(matches(isDisplayed()))
        onView(withId(R.id.signupText)).check(matches(isDisplayed()))
        onView(withId(R.id.regishere)).check(matches(isDisplayed()))
        onView(withId(R.id.regishere)).perform(click())
    }
}