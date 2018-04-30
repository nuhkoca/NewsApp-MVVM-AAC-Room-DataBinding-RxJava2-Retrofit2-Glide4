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
        mNewsFragmentViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()), new NewsFragmentViewModelFactory(ObservableHelper.getInstance())).get(NewsFragmentViewModel.class);

        int endpointVal = Objects.requireNonNull(getArguments()).getInt(Constants.ENDPOINT_ARGS_KEY);

        switch (endpointVal) {
            case Constants.TOP_NEWS_ID:
                mNewsFragmentViewModel.fetchTopHeadlines().observe(getActivity(), new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                loadTopHeadlineParametersFromPreferences(mSharedPreferences);
                showLoading();

                break;

            case Constants.EVERYTHING_ID:
                mNewsFragmentViewModel.fetchEverything().observe(getActivity(), new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                mNewsFragmentViewModel.getEverything("apple");
                showLoading();

                break;

            case Constants.SOURCES_ID:
                mNewsFragmentViewModel.fetchSources().observe(getActivity(), new Observer<SourcesWrapper>() {
                    @Override
                    public void onChanged(@Nullable SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper != null) {
                            setupSourcesRV(sourcesWrapper.getSources());
                        }
                    }
                });

                loadNewsSourceParametersFromPreferences(mSharedPreferences);
                showLoading();

                break;

            default:
                break;
        }
    }

    private void loadTopHeadlineParametersFromPreferences(SharedPreferences sharedPreferences){
        mNewsFragmentViewModel.getTopHeadlines(
                sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key),getString(R.string.pref_country_us_value)),
                null, null, null);
    }

    private void loadNewsSourceParametersFromPreferences(SharedPreferences sharedPreferences) {
        mNewsFragmentViewModel.getSources(
                sharedPreferences.getString(getString(R.string.pref_language_key),
                        getString(R.string.pref_language_english_value)),
                null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_top_headlines_country_key))) {
            loadTopHeadlineParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_language_key))){
            loadNewsSourceParametersFromPreferences(sharedPreferences);
        }
    }

    private void showLoading(){
        mNewsFragmentViewModel.isLoading.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean){
                    mFragmentNewsBinding.pbNews.setVisibility(View.VISIBLE);
                }else {
                    mFragmentNewsBinding.pbNews.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}