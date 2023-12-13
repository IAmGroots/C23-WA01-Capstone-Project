package com.example.capstoneproject.ui.change_plan

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class ChangePlanActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(ChangePlanActivity::class.java)

    @Test
    fun changePlanTestToolBar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun changePlanTestCardView() {
        onView(withId(R.id.card_plan_gold)).check(matches(isDisplayed()))
        onView(withId(R.id.btnChangePlanGold)).check(matches(isDisplayed()))
        onView(withId(R.id.card_plan_gold)).perform(ViewActions.click())

        onView(withId(R.id.card_plan_silver)).check(matches(isDisplayed()))
        onView(withId(R.id.btnChangePlanSilver)).check(matches(isDisplayed()))
        onView(withId(R.id.card_plan_silver)).perform(ViewActions.click())

        onView(withId(R.id.card_plan_bronze)).check(matches(isDisplayed()))
        onView(withId(R.id.btnChangePlanBronze)).check(matches(isDisplayed()))
        onView(withId(R.id.card_plan_bronze)).perform(ViewActions.click())
    }

    @Test
    fun changePlantestTextView() {
        onView(withId(R.id.tvPackageFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSpeedFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.tvServiceDateFrom)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLocationFrom)).check(matches(isDisplayed()))

        onView(withId(R.id.tvPackageTo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSpeedTo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvServiceDateTo)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLocationTo)).check(matches(isDisplayed()))

        onView(withId(R.id.tvPackageBronze)).check(matches(isDisplayed()))
        onView(withId(R.id.tvSpeedBronze)).check(matches(isDisplayed()))
        onView(withId(R.id.tvServiceDateBronze)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLocationBronze)).check(matches(isDisplayed()))
    }
}