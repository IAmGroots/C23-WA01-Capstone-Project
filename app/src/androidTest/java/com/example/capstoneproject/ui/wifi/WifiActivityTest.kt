package com.example.capstoneproject.ui.wifi

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class WifiActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(WifiActivity::class.java)

    @Test
    fun wifiTestToolBar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun wifiTestButton() {
        onView(withId(R.id.btn_current_location)).check(matches(isDisplayed()))

        /*onView(withId(R.id.btn_current_location)).perform(ViewActions.click())*/
    }

    @Test
    fun wifiTestMap() {
        onView(withId(R.id.map)).check(matches(isDisplayed()))

        /*onView(withId(R.id.map)).perform(ViewActions.click())*/
    }

    @Test
    fun wifitestTextView() {
        onView(withId(R.id.tv_empty_hotspot)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_hotspot)).check(matches(isDisplayed()))

        /*onView(withId(R.id.rv_hotspot)).perform(ViewActions.click())*/
    }
}