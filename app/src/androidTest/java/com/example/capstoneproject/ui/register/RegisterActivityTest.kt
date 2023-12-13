package com.example.capstoneproject.ui.register

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class RegisterActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerTestFirstnameInValid() {
        onView(withId(R.id.etFirstName)).perform(typeText("a"), closeSoftKeyboard())// firstname tidak valid


        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etFirstName)).check(matches(hasErrorText("Name must be a minimum of 2 characters and a maximum of 150 characters"))) // errorText
    }

    @Test
    fun registerTestLastnameInValid() {
        onView(withId(R.id.etLastName)).perform(typeText("a"), closeSoftKeyboard())// lastname tidak valid

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etLastName)).check(matches(hasErrorText("Name must be a minimum of 2 characters and a maximum of 150 characters"))) // errorText
    }

    @Test
    fun loginTestEmailInValid() {
        onView(withId(R.id.etEmail)).perform(typeText("invalidEmail"), closeSoftKeyboard()) // email tidak valid

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Invalid e-mail address format"))) // errorText
    }

    @Test
    fun loginTestPasswordInValid() {
        onView(withId(R.id.etPassword)).perform(typeText("1234"), closeSoftKeyboard())// password tidak valid

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password must be at least 8 characters"))) // errorText
    }

    @Test
    fun loginTestConfirmPasswordInValid() {
        onView(withId(R.id.etConfirmPassword)).perform(typeText("1234"), closeSoftKeyboard())// confirmpassword tidak valid

        onView(withId(R.id.btnRegister)).perform(click())
        onView(withId(R.id.etConfirmPassword)).check(matches(hasErrorText("Password must be at least 8 characters"))) // errorText
    }

    @Test
    fun registerTestValid() {
        onView(withId(R.id.etFirstName)).perform(typeText("John"), closeSoftKeyboard()) //firstname valid
        onView(withId(R.id.etLastName)).perform(typeText("Doe"), closeSoftKeyboard()) //lastname valid
        onView(withId(R.id.etEmail)).perform(typeText("john.doe@example.com"), closeSoftKeyboard()) //email valid
        onView(withId(R.id.etPassword)).perform(typeText("password123"), closeSoftKeyboard())// password valid
        onView(withId(R.id.etConfirmPassword)).perform(typeText("password456"), closeSoftKeyboard())// confirmpassword valid

        onView(withId(R.id.btnRegister)).perform(click())
    }

    @Test
    fun registerDisplayUI() {
        onView(withId(R.id.etFirstName)).check(matches(isDisplayed()))
        onView(withId(R.id.etLastName)).check(matches(isDisplayed()))
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.etConfirmPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnRegister)).check(matches(isDisplayed()))
        onView(withId(R.id.btnBack)).check(matches(isDisplayed()))
        onView(withId(R.id.signupText)).check(matches(isDisplayed()))
        onView(withId(R.id.loginhere)).check(matches(isDisplayed()))
    }

    @Test
    fun registerTestIntentToLogin() {
        onView(withId(R.id.loginhere)).perform(click()) //pindah ke halaman login
    }

    @Test
    fun registerTestBackToLogin() {
        onView(withId(R.id.btnBack)).perform(click()) // kembali ke halaman login
    }
}