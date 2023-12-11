package com.example.capstoneproject.ui.order

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class OrderActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(OrderActivity::class.java)

    @Test
    fun orderTestToolBar() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun orderTestCardView() {
        Espresso.onView(ViewMatchers.withId(R.id.cardPackageFrom))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.cardPackageTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun orderTestTextView() {
        Espresso.onView(ViewMatchers.withId(R.id.tvPackageFrom))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvSpeedFrom))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvServiceDateFrom))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvLocationFrom))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tv_changeTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tv_period))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tvPackageTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvSpeedTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvServiceDateTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tvLocationTo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(ViewMatchers.withId(R.id.tv_total))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_price))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun orderTestButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_payment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.btn_payment)).perform(ViewActions.click())
    }
}