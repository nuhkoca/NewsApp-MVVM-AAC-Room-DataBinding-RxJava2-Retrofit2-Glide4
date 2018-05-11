package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IOverflowMenuItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.ISourcesItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news.ArticlesAdapter;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news.SourcesAdapter;

import java.util.List;

public class RecyclerViewUtil {

    private static SourcesAdapter sourcesAdapter;

    private static void getLayoutManagerForSources(Context context, RecyclerView sourcesRV) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        sourcesRV.setHasFixedSize(true);
        sourcesRV.setLayoutManager(linearLayoutManager);

        int config = context.getResources().getConfiguration().orientation;

        if (config == Configuration.ORIENTATION_PORTRAIT) {
            sourcesRV.addItemDecoration(new CustomDividerItemDecoration(context, 1,0));
        }
    }

    private static void getLayoutManagerForArticles(Context context, RecyclerView articlesRV) {
        int orientation = context.getResources().getConfiguration().orientation;

        RecyclerView.LayoutManager layoutManager;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(context);
        } else {
            int columnCount = DynamicColumnCalculator.getOptimalNumberOfColumn(context);

            layoutManager = new GridLayoutManager(context, columnCount);
        }

        articlesRV.setHasFixedSize(true);
        articlesRV.setLayoutManager(layoutManager);
    }

    public static void populateOnlineArticles(Context context, RecyclerView articlesRV, List<Articles> articlesList, IOverflowMenuItemClickListener iOverflowMenuItemClickListener) {
        getLayoutManagerForArticles(context, articlesRV);

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(iOverflowMenuItemClickListener);
        articlesAdapter.swapData(articlesList);

        articlesRV.setAdapter(articlesAdapter);

        articlesAdapter.notifyDataSetChanged();
    }

    public static void populateOfflineTopHeadlines(Context context, RecyclerView articlesRV, List<DbTopHeadlines> dbTopHeadlinesList, IOverflowMenuItemClickListener iOverflowMenuItemClickListener) {
        getLayoutManagerForArticles(context, articlesRV);

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(dbTopHeadlinesList, null, iOverflowMenuItemClickListener);
        articlesAdapter.swapOfflineTopHeadlines(dbTopHeadlinesList);

        articlesRV.setAdapter(articlesAdapter);

        articlesAdapter.notifyDataSetChanged();
    }

    public static void populateOfflineEverything(Context context, RecyclerView articlesRV, List<DbEverything> dbEverythingList, IOverflowMenuItemClickListener iOverflowMenuItemClickListener) {
        getLayoutManagerForArticles(context, articlesRV);

        ArticlesAdapter articlesAdapter = new ArticlesAdapter(null, dbEverythingList, iOverflowMenuItemClickListener);
        articlesAdapter.swapOfflineEverything(dbEverythingList);

        articlesRV.setAdapter(articlesAdapter);

        articlesAdapter.notifyDataSetChanged();
    }

    public static void populateOnlineSources(Context context, RecyclerView sourcesRV, List<Sources> sourcesList, ISourcesItemClickListener iSourcesItemClickListener) {
        getLayoutManagerForSources(context, sourcesRV);

        sourcesAdapter = new SourcesAdapter(iSourcesItemClickListener);
        sourcesAdapter.swapData(sourcesList);

        sourcesRV.setAdapter(sourcesAdapter);

        sourcesAdapter.notifyDataSetChanged();
    }

    public static void populateOfflineSources(Context context, RecyclerView sourcesRV, List<DbSources> dbSourcesList, ISourcesItemClickListener iSourcesItemClickListener) {
        getLayoutManagerForSources(context, sourcesRV);

        SourcesAdapter sourcesAdapter = new SourcesAdapter(dbSourcesList, iSourcesItemClickListener);
        sourcesRV.setAdapter(sourcesAdapter);

        sourcesAdapter.swapOfflineData(dbSourcesList);

        sourcesAdapter.notifyDataSetChanged();
    }

    public static SourcesAdapter getSourcesAdapter(){
        return sourcesAdapter;
    }
}