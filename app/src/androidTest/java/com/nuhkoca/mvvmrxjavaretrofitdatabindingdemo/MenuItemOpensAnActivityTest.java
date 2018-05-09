package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.main.NewsActivity;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.settings.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MenuItemOpensAnActivityTest {

    @Rule
    public IntentsTestRule<NewsActivity> intentsTestRule = new IntentsTestRule<>(NewsActivity.class);

    @SuppressWarnings("unchecked")
    @Test
    public void menuItemOpensAnActivity(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText(R.string.settings_text)).perform(click());

        intended(hasComponent(hasClassName(SettingsActivity.class.getName())));
    }
}
