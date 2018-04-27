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
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IRecyclerViewScrollListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.NewsWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.FragmentNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.RecyclerViewScrollHelper;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    private FragmentNewsBinding mFragmentNewsBinding;
    private static IRecyclerViewScrollListener mIRecyclerViewScrollListener;

    public static NewsFragment getInstance(IRecyclerViewScrollListener iRecyclerViewScrollListener) {
        mIRecyclerViewScrollListener = iRecyclerViewScrollListener;

        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        mFragmentNewsBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_news, container, false);

        return mFragmentNewsBinding.getRoot();
    }


    private void setupRecyclerView(List<Articles> articlesList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        mFragmentNewsBinding.rvNews.setHasFixedSize(true);
        mFragmentNewsBinding.rvNews.setLayoutManager(linearLayoutManager);

        NewsAdapter newsAdapter = new NewsAdapter();
        newsAdapter.swapData(articlesList);

        mFragmentNewsBinding.rvNews.setAdapter(newsAdapter);

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
        NewsFragmentViewModel newsFragmentViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()), new NewsFragmentViewModelFactory(null, null, null, "trump")).get(NewsFragmentViewModel.class);

        newsFragmentViewModel.mNewsWrapper.observe(getActivity(), new Observer<NewsWrapper>() {
            @Override
            public void onChanged(@Nullable NewsWrapper newsWrapper) {
                setupRecyclerView(Objects.requireNonNull(newsWrapper).getArticles());
            }
        });

        newsFragmentViewModel.fetchTopHeadlines();
    }
}