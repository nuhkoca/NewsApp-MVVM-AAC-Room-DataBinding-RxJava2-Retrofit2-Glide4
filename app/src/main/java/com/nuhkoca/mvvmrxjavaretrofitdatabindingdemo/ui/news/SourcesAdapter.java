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
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.SourcesItemCardBinding;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private List<Sources> mSourcesList;
    private List<DbSources> mDbSources;

    SourcesAdapter() {
        mSourcesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        SourcesItemCardBinding sourcesItemCardBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.sources_item_card, parent, false);

        return new SourcesAdapter.ViewHolder(sourcesItemCardBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mSourcesList != null) {
            Sources sources = mSourcesList.get(position);

            holder.bindViews(sources);
        } else {
            DbSources current = mDbSources.get(position);

            holder.mSourcesItemCardBinding.tvSourceName.setText(current.getName());
        }
    }

    public void swapData(List<Sources> sourcesList) {
        mSourcesList = sourcesList;

        notifyDataSetChanged();
    }

    public void swapOfflineData(List<DbSources> dbSourcesList) {
        this.mDbSources = dbSourcesList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSourcesList != null) {
            return mSourcesList.size();
        } else {
            return mDbSources.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SourcesItemCardBinding mSourcesItemCardBinding;

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