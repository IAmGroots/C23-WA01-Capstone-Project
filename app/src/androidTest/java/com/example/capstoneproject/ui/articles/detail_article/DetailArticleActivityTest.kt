package com.example.capstoneproject.ui.articles.detail_article

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test


class DetailArticleActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(DetailArticleActivity::class.java)

    @Test
    fun detailArticleTestToolBar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun detailArticleTestCardView() {
        onView(withId(R.id.card_title)).check(matches(isDisplayed()))
        onView(withId(R.id.card_image)).check(matches(isDisplayed()))
        onView(withId(R.id.card_desc)).check(matches(isDisplayed()))
    }

    @Test
    fun detailArticleTestTextView() {
        onView(withId(R.id.title_article)).check(matches(isDisplayed()))
        onView(withId(R.id.publish_date)).check(matches(isDisplayed()))
        onView(withId(R.id.desc_article)).check(matches(isDisplayed()))
        onView(withId(R.id.text_see_more)).check(matches(isDisplayed()))
    }

    @Test
    fun detailArticleTestImageView() {
        onView(withId(R.id.img_articles)).check(matches(isDisplayed()))
    }
}