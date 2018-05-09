package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.about;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BuildConfig;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.ActivityAboutBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding activityAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        setTitle(getString(R.string.about_text));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadSimulateFromSharedPreference();

        View aboutPage = createPage();

        activityAboutBinding.aboutFrame.addView(aboutPage, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);

        MenuItem switchItem = menu.findItem(R.id.switch_menu);

        final SwitchCompat switchView = switchItem.getActionView().findViewById(R.id.switchForActionBar);

        switchView.setChecked(loadCheckableFromSharedPreference());

        boolean isChecked = loadCheckableFromSharedPreference();

        if (isChecked) {
            switchView.setText(getString(R.string.night_mode_text));
        } else {
            switchView.setText(getString(R.string.day_mode_text));
        }

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    saveSimulateToSharedPreference(1, true);
                    loadSimulateFromSharedPreference();

                    switchView.setText(getString(R.string.night_mode_text));
                } else {
                    saveSimulateToSharedPreference(0, false);
                    loadSimulateFromSharedPreference();

                    switchView.setText(getString(R.string.day_mode_text));
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        switch (itemThatWasClicked) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_copy_right_icon);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        return copyRightsElement;
    }

    private void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        getDelegate().applyDayNight();
    }

    private void loadSimulateFromSharedPreference() {
        SharedPreferences prefs = getSharedPreferences(Constants.UI_PREF_NAME, MODE_PRIVATE);
        int mode = prefs.getInt(Constants.UI_MODE_PREF, 0);

        simulateDayNight(mode);
    }

    private boolean loadCheckableFromSharedPreference() {
        SharedPreferences prefs = getSharedPreferences(Constants.UI_PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(Constants.CHECKED_MODE_PREF, false);
    }

    private View createPage() {
        return new AboutPage(this)
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .setImage(R.drawable.ic_about_icon)
                .addItem(new Element().setTitle(String.valueOf(String.format(getString(R.string.version), BuildConfig.VERSION_NAME))))
                .addGroup(getString(R.string.connect_with_me))
                .addEmail(getString(R.string.email), getString(R.string.email_me))
                .addWebsite(getString(R.string.personal_website), getString(R.string.visit_my_website))
                .addGroup(getString(R.string.follow_me_on_social_media))
                .addFacebook(getString(R.string.personal_facebook), getString(R.string.add_me_on_facebook))
                .addTwitter(getString(R.string.personal_twitter), getString(R.string.follow_me_on_twitter))
                .addYoutube(getString(R.string.personal_youtube), getString(R.string.follow_me_on_youtube))
                .addPlayStore(getString(R.string.personal_play_store), getString(R.string.rate_me_on_google_play_store))
                .addInstagram(getString(R.string.personal_instagram), getString(R.string.follow_me_on_instagram))
                .addGitHub(getString(R.string.github), getString(R.string.fork_me_on_github))
                .addGroup(getString(R.string.libs_that_we_inspire))
                .addItem(getLibElement())
                .addItem(getCopyRightsElement())
                .create();
    }

    private void saveSimulateToSharedPreference(int mode, boolean isChecked) {
        SharedPreferences.Editor editor =
                getSharedPreferences(Constants.UI_PREF_NAME, MODE_PRIVATE).edit();

        editor.putInt(Constants.UI_MODE_PREF, mode);
        editor.putBoolean(Constants.CHECKED_MODE_PREF, isChecked);

        editor.apply();
    }

    private Element getLibElement() {
        Element libElement = new Element();

        libElement.setTitle(getString(R.string.open_source_libs));
        libElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withActivityTitle(getString(R.string.libs_text))
                        .withAutoDetect(true)
                        .start(getApplicationContext());
            }
        });

        return libElement;
    }
}