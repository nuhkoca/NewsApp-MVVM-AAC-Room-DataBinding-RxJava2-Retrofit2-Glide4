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
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewItemDivider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentNewsBinding mFragmentNewsBinding;
    private NewsFragmentViewModel mNewsFragmentViewModel;
    private SharedPreferences mSharedPreferences;
    private ArticlesWrapper mArticlesWrapperList;

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
    }

    private void loadTopHeadlineParametersFromPreferences(SharedPreferences sharedPreferences) {
        Set<String> sourceSet = new HashSet<>();
        sourceSet.add(getString(R.string.pref_sources_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)));
        StringBuilder selectedSources = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedSources.append(entries.get(i)).append(",");
        }

        if (selectedSources.length() > 0) {
            selectedSources.deleteCharAt(selectedSources.length() - 1);
        }

        mNewsFragmentViewModel.getTopHeadlines(
                sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key), getString(R.string.pref_country_us_value)),
                selectedSources.toString(),
                sharedPreferences.getString(getString(R.string.pref_category_key), null), null);
    }

    private void loadNewsSourceParametersFromPreferences(SharedPreferences sharedPreferences) {
        mNewsFragmentViewModel.getSources
                (sharedPreferences.getString(getString(R.string.pref_language_key), null),
                        sharedPreferences.getString(getString(R.string.pref_source_country_key), null),
                        sharedPreferences.getString(getString(R.string.pref_source_category_key), null));
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
                        mFragmentNewsBinding.rvNews.setVisibility(View.GONE);
                        mFragmentNewsBinding.tvErrorView.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                        mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void showErrorText(){
        mNewsFragmentViewModel.mErrorText.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mFragmentNewsBinding.tvErrorView.setText(s);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_top_headlines_country_key))
                || key.equals(getString(R.string.pref_category_key))
                || key.equals(getString(R.string.pref_source_key))) {
            loadTopHeadlineParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_language_key))
                || key.equals(getString(R.string.pref_source_country_key))
                || key.equals(getString(R.string.pref_source_category_key))) {
            loadNewsSourceParametersFromPreferences(sharedPreferences);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}