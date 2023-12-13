package com.example.capstoneproject.ui.login

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

class LoginActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginTestEmailInValid() {
        onView(withId(R.id.etEmail)).perform(ViewActions.typeText("invalidEmail")) // email tidak valid

        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.etEmail)).check(matches(hasErrorText("Invalid e-mail address format"))) // errorText
    }

    @Test
    fun loginTestPasswordInValid() {
        onView(withId(R.id.etPassword)).perform(ViewActions.typeText("1234"))// password tidak valid

        onView(withId(R.id.btnLogin)).perform(click())
        onView(withId(R.id.etPassword)).check(matches(hasErrorText("Password must be at least 8 characters"))) // errorText
    }

    @Test
    fun loginTestValid() {
        onView(withId(R.id.etEmail)).perform(ViewActions.typeText("test@bangkit.acadmy"), closeSoftKeyboard())//email valid
        onView(withId(R.id.etPassword)).perform(ViewActions.typeText("password1234"), closeSoftKeyboard())//password valid

        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun loginTestDisplayUI() {
        onView(withId(R.id.etEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.etPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.textforgotpassword)).check(matches(isDisplayed()))
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
        onView(withId(R.id.signupText)).check(matches(isDisplayed()))
        onView(withId(R.id.regishere)).check(matches(isDisplayed()))
    }

    @Test
    fun loginTestIntentToRegist() {
        onView(withId(R.id.regishere)).perform(click()) //pindah ke halaman registrasi
    }
}