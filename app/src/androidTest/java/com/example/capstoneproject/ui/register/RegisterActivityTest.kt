package com.example.capstoneproject.ui.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class RegisterActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerTestEditText() {
        Espresso.onView(ViewMatchers.withId(R.id.etFirstName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etLastName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etMobile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etPassword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.etConfirmPassword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun registerTestButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnRegister))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnBack))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btnRegister)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btnBack)).perform(ViewActions.click())
    }

    @Test
    fun registerTestTextView() {
        Espresso.onView(ViewMatchers.withId(R.id.signupText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.loginhere))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.loginhere)).perform(ViewActions.click())
    }
}