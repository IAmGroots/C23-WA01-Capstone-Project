package com.example.capstoneproject.ui.articles.detail_article

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.capstoneproject.R
import org.junit.Rule
import org.junit.Test

class DetailArticleActivityTest {

    @get:Rule
    var ruleActivity = ActivityScenarioRule(DetailArticleActivity::class.java)

    @Test
    fun detailArticleTestToolBar() {
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun detailArticleTestCardView() {
        Espresso.onView(ViewMatchers.withId(R.id.card_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.card_image))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.card_desc))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun detailArticleTestTextView() {
        Espresso.onView(ViewMatchers.withId(R.id.title_article))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.publish_date))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.desc_article))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.text_see_more))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun detailArticleTestImageView() {
        Espresso.onView(ViewMatchers.withId(R.id.img_articles))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}