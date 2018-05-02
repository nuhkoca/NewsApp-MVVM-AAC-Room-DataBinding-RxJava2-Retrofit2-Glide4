package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.FragmentNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.ConnectionSniffer;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewItemDivider;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentNewsBinding mFragmentNewsBinding;
    private NewsFragmentViewModel mNewsFragmentViewModel;
    private SharedPreferences mSharedPreferences;

    public static NewsFragment getInstance(INewsAPI.Endpoints endpoints) {
        NewsFragment newsFragment = new NewsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ENDPOINT_ARGS_KEY, endpoints.getValue());
        newsFragment.setArguments(bundle);


        return newsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        mFragmentNewsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_news, container, false);

        mNewsFragmentViewModel = ViewModelProviders
                .of(this, new NewsFragmentViewModelFactory(ObservableHelper.getInstance())).get(NewsFragmentViewModel.class);


        return mFragmentNewsBinding.getRoot();
    }

    private void setupNewsRV(List<Articles> articlesList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mFragmentNewsBinding.rvNews.setHasFixedSize(true);
        mFragmentNewsBinding.rvNews.setLayoutManager(linearLayoutManager);

        ArticlesAdapter articlesAdapter = new ArticlesAdapter();
        articlesAdapter.swapData(articlesList);

        mFragmentNewsBinding.rvNews.setAdapter(articlesAdapter);
    }

    private void setupSourcesRV(List<Sources> sourcesList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mFragmentNewsBinding.rvNews.setHasFixedSize(true);
        mFragmentNewsBinding.rvNews.setLayoutManager(linearLayoutManager);
        mFragmentNewsBinding.rvNews.addItemDecoration(new RecyclerViewItemDivider(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL, 0));

        SourcesAdapter sourcesAdapter = new SourcesAdapter();
        sourcesAdapter.swapData(sourcesList);

        mFragmentNewsBinding.rvNews.setAdapter(sourcesAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupUI();
    }

    public void setupUI() {
        int endpointVal = Objects.requireNonNull(getArguments()).getInt(Constants.ENDPOINT_ARGS_KEY);
        showLoadingBar();
        showError();

        switch (endpointVal) {
            case Constants.TOP_NEWS_ID:
                mNewsFragmentViewModel.fetchTopHeadlines().observe(this, new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                loadTopHeadlineParametersFromPreferences(mSharedPreferences);

                break;

            case Constants.EVERYTHING_ID:
                mNewsFragmentViewModel.fetchEverything().observe(this, new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                mNewsFragmentViewModel.getEverything("bayern");

                break;

            case Constants.SOURCES_ID:
                mNewsFragmentViewModel.fetchSources().observe(this, new Observer<SourcesWrapper>() {
                    @Override
                    public void onChanged(@Nullable SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper != null) {
                            setupSourcesRV(sourcesWrapper.getSources());
                        }
                    }
                });

                loadNewsSourceParametersFromPreferences(mSharedPreferences);

                break;

            default:
                break;
        }

        boolean isConnectionAvailable = ConnectionSniffer.isActiveConnection();

        if (!isConnectionAvailable) {
            showError();
        }
    }

    private void loadTopHeadlineParametersFromPreferences(SharedPreferences sharedPreferences) {
        boolean isCategoryNull = sharedPreferences.getString(getString(R.string.pref_category_key), getString(R.string.pref_category_all_value)).equals(getString(R.string.pref_category_all_value));

        boolean isCountryNull = sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key), getString(R.string.pref_country_us_value)).equals(getString(R.string.pref_country_all_value));

        String country = sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key), getString(R.string.pref_country_us_value));

        String category = sharedPreferences.getString(getString(R.string.pref_category_key), getString(R.string.pref_category_all_value));

        if (isCountryNull) {
            mNewsFragmentViewModel.getTopHeadlines(null, null, category, null);
        }

        if (isCategoryNull) {
            mNewsFragmentViewModel.getTopHeadlines(country, null, null, null);
        }

        if (!isCountryNull && !isCategoryNull) {
            mNewsFragmentViewModel.getTopHeadlines(country,
                    null, category, null);
        }
    }

    private void loadNewsSourceParametersFromPreferences(SharedPreferences sharedPreferences) {
        boolean isLangNull = sharedPreferences.getString(getString(R.string.pref_language_key),
                getString(R.string.pref_language_all_value)).equals(getString(R.string.pref_language_all_value));

        boolean isCountryNull = sharedPreferences.getString(getString(R.string.pref_source_country_key),
                getString(R.string.pref_source_country_all_value)).equals(getString(R.string.pref_source_country_all_value));

        String language = sharedPreferences.getString(getString(R.string.pref_language_key),
                getString(R.string.pref_language_all_value));

        String country = sharedPreferences.getString(getString(R.string.pref_source_country_key),
                getString(R.string.pref_source_country_all_value));

        if (isLangNull) {
            mNewsFragmentViewModel.getSources(
                    null,
                    country);
        }
        if (isCountryNull) {
            mNewsFragmentViewModel.getSources(
                    language,
                    null);
        }
        if (isLangNull && isCountryNull) {
            mNewsFragmentViewModel.getSources(null, null);
        }
        if (!isLangNull && !isCountryNull) {
            mNewsFragmentViewModel.getSources(language, country);
        }
    }

    private void showLoadingBar() {
        mNewsFragmentViewModel.mIsLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        mFragmentNewsBinding.pbNews.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.pbNews.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showError() {
        mNewsFragmentViewModel.mIsErrorShown.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isEnabled) {
                if (isEnabled != null) {
                    if (isEnabled) {
                        mFragmentNewsBinding.tvErrorView.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_top_headlines_country_key))) {
            loadTopHeadlineParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_language_key))) {
            loadNewsSourceParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_source_country_key))) {
            loadNewsSourceParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_category_key))) {
            loadTopHeadlineParametersFromPreferences(sharedPreferences);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}