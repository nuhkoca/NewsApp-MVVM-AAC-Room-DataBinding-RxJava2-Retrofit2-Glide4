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
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.NewsItemCardBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.SourcesItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private List<Sources> mSourcesList;
    private SourcesItemCardBinding mSourcesItemCardBinding;

    public SourcesAdapter() {
        mSourcesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        mSourcesItemCardBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.sources_item_card, parent, false);

        return new SourcesAdapter.ViewHolder(mSourcesItemCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sources sources = mSourcesList.get(position);

        holder.bindViews(sources);
    }

    public void swapData(List<Sources> sourcesList) {
        mSourcesList = sourcesList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSourcesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);

            mSourcesItemCardBinding = DataBindingUtil.getBinding(itemView);
        }

        void bindViews(Sources sources) {
            mSourcesItemCardBinding.setVariable(BR.sources, sources);

            mSourcesItemCardBinding.executePendingBindings();
        }
    }
}