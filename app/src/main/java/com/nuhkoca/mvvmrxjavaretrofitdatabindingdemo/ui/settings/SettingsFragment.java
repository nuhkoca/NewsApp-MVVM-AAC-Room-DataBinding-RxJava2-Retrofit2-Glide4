package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference mCountryPref;
    private ListPreference mCategoryPref;
    private MultiSelectListPreference mSourcePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        PreferenceManager.setDefaultValues(Objects.requireNonNull(getActivity()), R.xml.pref_general, false);

        PreferenceGroup preferenceGroup = (PreferenceGroup) getPreferenceScreen().getPreference(0);

        mCountryPref = (ListPreference) preferenceGroup.getPreference(0);
        mSourcePref = (MultiSelectListPreference) preferenceGroup.getPreference(1);
        mCategoryPref = (ListPreference) preferenceGroup.getPreference(2);

        initSummary(getPreferenceScreen());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv = view.findViewById(android.R.id.list);
        if (lv != null)
            ViewCompat.setNestedScrollingEnabled(lv, true);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(getString(R.string.pref_source_key))) {
            updateSummary(findPreference(key));

        } else {
            updateMultiSummary(findPreference(key),
                    sharedPreferences.getStringSet(getString(R.string.pref_source_key), null));
        }
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updateSummary(p);
            updateMultiSummary(p, PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()))
                    .getStringSet(getString(R.string.pref_source_key), null));
        }
    }

    private void updateSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());

            if (p.getKey().equals(getString(R.string.pref_top_headlines_country_key))
                    || p.getKey().equals(getString(R.string.pref_category_key))) {
                if (p.getSharedPreferences().getString(getString(R.string.pref_top_headlines_country_key),
                        getString(R.string.pref_country_all_value)).equals("")
                        && p.getSharedPreferences().getString(getString(R.string.pref_category_key),
                        getString(R.string.pref_category_all_value)).equals("")) {
                    mSourcePref.setEnabled(true);
                } else if (p.getSharedPreferences().getString(getString(R.string.pref_top_headlines_country_key),
                        getString(R.string.pref_country_all_value)).equals("")
                        || p.getSharedPreferences().getString(getString(R.string.pref_category_key),
                        getString(R.string.pref_category_all_value)).equals("")) {
                    mSourcePref.setEnabled(false);
                } else {
                    mSourcePref.setEnabled(false);
                }
            }
        }
    }

    private void updateMultiSummary(Preference p, Set<String> value) {
        if (p instanceof MultiSelectListPreference) {
            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) p;

            List<String> entries = new ArrayList<>(value);
            StringBuilder allEntries = new StringBuilder();

            for (int i = 0; i < entries.size(); i++) {
                allEntries.append(multiSelectListPreference.getEntries()[multiSelectListPreference.findIndexOfValue(entries.get(i))])
                        .append(", ");
            }

            if (allEntries.length() > 0) {
                allEntries.deleteCharAt(allEntries.length() - 2);
            }

            p.setSummary(allEntries.toString());

            if (value.size() == 0) {
                mCategoryPref.setEnabled(true);
                mCountryPref.setEnabled(true);
            } else {
                mCategoryPref.setEnabled(false);
                mCategoryPref.setValue(getString(R.string.pref_category_all_value));

                mCountryPref.setEnabled(false);
                mCountryPref.setValue(getString(R.string.pref_country_all_value));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onStop() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }
}