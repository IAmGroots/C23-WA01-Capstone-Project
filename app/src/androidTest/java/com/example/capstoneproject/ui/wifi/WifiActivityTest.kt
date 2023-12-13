package com.example.capstoneproject.ui.wifi

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class WifiActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(WifiActivity::class.java)

    @Test
    fun wifiTestToolBar() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun wifiTestButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_current_location))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_current_location)).perform(ViewActions.click())
    }

    @Test
    fun wifiTestMap() {
        Espresso.onView(ViewMatchers.withId(R.id.map))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.map)).perform(ViewActions.click())
    }

    @Test
    fun wifitestTextView() {
        /*Espresso.onView(ViewMatchers.withId(R.id.hotspot))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))*/
        Espresso.onView(ViewMatchers.withId(R.id.tv_empty_hotspot))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_hotspot))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_hotspot)).perform(ViewActions.click())
    }
}