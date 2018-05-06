package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Articles;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.NewsItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> mArticlesList;

    ArticlesAdapter() {
        mArticlesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        NewsItemCardBinding newsItemCardBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.news_item_card, parent, false);

        return new ViewHolder(newsItemCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Articles articles = mArticlesList.get(position);

        holder.bindViews(articles);
    }

    public void swapData(List<Articles> articlesList) {
        mArticlesList = articlesList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mArticlesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private NewsItemCardBinding mNewsItemCardBinding;

        ViewHolder(View itemView) {
            super(itemView);

            mNewsItemCardBinding = DataBindingUtil.getBinding(itemView);
        }

        void bindViews(Articles articles) {
            mNewsItemCardBinding.setVariable(BR.articlesImage, articles.getUrlToImage());
            mNewsItemCardBinding.setVariable(BR.articlesTitle, articles.getTitle());
            mNewsItemCardBinding.setVariable(BR.articlesDescription, articles.getDescription());
            mNewsItemCardBinding.setVariable(BR.articlesSourceName, articles.getSource().getName());

            mNewsItemCardBinding.executePendingBindings();
        }
    }
}