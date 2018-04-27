package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.api.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IRecyclerViewScrollListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.FragmentNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.RecyclerViewScrollHelper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewItemDivider;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private FragmentNewsBinding mFragmentNewsBinding;
    private static IRecyclerViewScrollListener mIRecyclerViewScrollListener;

    public static NewsFragment getInstance(INewsAPI.Endpoints endpoints, IRecyclerViewScrollListener iRecyclerViewScrollListener) {
        NewsFragment newsFragment = new NewsFragment();

        mIRecyclerViewScrollListener = iRecyclerViewScrollListener;

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

        mFragmentNewsBinding.rvNews.addOnScrollListener(new RecyclerViewScrollHelper() {
            @Override
            public void onHide() {
                mIRecyclerViewScrollListener.onHid();
            }

            @Override
            public void onShow() {
                mIRecyclerViewScrollListener.onShown();
            }
        });
    }

    private void setupSourcesRV(List<Sources> sourcesList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mFragmentNewsBinding.rvNews.setHasFixedSize(true);
        mFragmentNewsBinding.rvNews.setLayoutManager(linearLayoutManager);
        mFragmentNewsBinding.rvNews.addItemDecoration(new RecyclerViewItemDivider(getActivity(), LinearLayoutManager.VERTICAL, 16));

        SourcesAdapter sourcesAdapter = new SourcesAdapter();
        sourcesAdapter.swapData(sourcesList);

        mFragmentNewsBinding.rvNews.setAdapter(sourcesAdapter);

        mFragmentNewsBinding.rvNews.addOnScrollListener(new RecyclerViewScrollHelper() {
            @Override
            public void onHide() {
                mIRecyclerViewScrollListener.onHid();
            }

            @Override
            public void onShow() {
                mIRecyclerViewScrollListener.onShown();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NewsFragmentViewModel newsFragmentViewModel;

        int endpointVal = Objects.requireNonNull(getArguments()).getInt(Constants.ENDPOINT_ARGS_KEY);

        switch (endpointVal) {
            case 0:
                newsFragmentViewModel = ViewModelProviders
                        .of(Objects.requireNonNull(getActivity()), new TopHeadlinesModelFactory(getActivity().getApplication(),null, null, "business", null)).get(NewsFragmentViewModel.class);

                newsFragmentViewModel.fetchTopHeadlines().observe(getActivity(), new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                newsFragmentViewModel.getTopHeadlines();

                break;

            case 1:
                newsFragmentViewModel = ViewModelProviders
                        .of(Objects.requireNonNull(getActivity()), new EverythingModelFactory(getActivity().getApplication(),"apple")).get(NewsFragmentViewModel.class);

                newsFragmentViewModel.fetchEverything().observe(getActivity(), new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            setupNewsRV(articlesWrapper.getArticles());
                        }
                    }
                });

                newsFragmentViewModel.getEverything();

                break;

            case 2:
                newsFragmentViewModel = ViewModelProviders
                        .of(Objects.requireNonNull(getActivity()), new SourcesViewModelFactory(getActivity().getApplication(),"en", null)).get(NewsFragmentViewModel.class);

                newsFragmentViewModel.fetchSources().observe(getActivity(), new Observer<SourcesWrapper>() {
                    @Override
                    public void onChanged(@Nullable SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper != null) {
                            setupSourcesRV(sourcesWrapper.getSources());
                        }
                    }
                });

                newsFragmentViewModel.getSources();

                break;

            default:
                break;
        }
    }
}