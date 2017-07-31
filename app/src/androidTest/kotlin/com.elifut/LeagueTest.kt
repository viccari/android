package com.elifut

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.elifut.activity.CurrentTeamDetailsActivity
import com.elifut.activity.MainActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okreplay.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LeagueTest {
  private val activityTestRule = ActivityTestRule(MainActivity::class.java)
  private val configuration = OkReplayConfig.Builder()
      .tapeRoot(AndroidTapeRoot(InstrumentationRegistry.getContext(), javaClass))
      .defaultMode(TapeMode.READ_ONLY)
      .sslEnabled(true)
      .interceptor(OkReplayInterceptorProvider.instance)
      .build()
  @JvmField @Rule val testRule = OkReplayRuleChain(configuration, activityTestRule).get()
  private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

  @Before fun setUp() {
    Intents.init()
    val application = activityTestRule.activity.applicationContext as ElifutApplication
    val okHttpClient = application.component.okHttpClient()
    application.component.appInitializer().clearData()
    okHttp3IdlingResource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
    IdlingRegistry.getInstance().register(okHttp3IdlingResource)
  }

  @After fun tearDown() {
    Intents.release()
    IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
  }

  @Test
  @OkReplay
  fun testLeague() {
    onView(withText("Argentina")).perform(click())
    onView(withId(R.id.fab)).perform(click())
    onView(withText("Loading")).check(matches(isDisplayed()))
    Thread.sleep(3000)
    intended(hasComponent(CurrentTeamDetailsActivity::class.java.name))
    onView(withText("Team Info")).check(matches(isDisplayed()))
    onView(withText("League")).check(matches(isDisplayed()))
  }
}