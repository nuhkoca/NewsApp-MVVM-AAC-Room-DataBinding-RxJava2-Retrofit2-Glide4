package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.BR;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.ISourcesItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.Sources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.SourcesItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> implements Filterable {

    private List<Sources> mSourcesList;
    private List<Sources> mSourcesListFiltered;
    private List<DbSources> mDbSourcesListFiltered;

    private List<DbSources> mDbSources;
    private ISourcesItemClickListener mISourcesItemClickListener;

    public SourcesAdapter(ISourcesItemClickListener iSourcesItemClickListener) {
        this.mISourcesItemClickListener = iSourcesItemClickListener;

        mSourcesList = new ArrayList<>();
        mSourcesListFiltered = mSourcesList;
    }

    public SourcesAdapter(List<DbSources> mDbSources, ISourcesItemClickListener iSourcesItemClickListener) {
        this.mDbSources = mDbSources;

        mDbSourcesListFiltered = mDbSources;

        this.mISourcesItemClickListener = iSourcesItemClickListener;
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
        if (mSourcesListFiltered != null) {
            Sources sources = mSourcesListFiltered.get(position);
            holder.bindViews(sources);
        } else {
            DbSources dbSources = mDbSourcesListFiltered.get(position);
            holder.bindViews(dbSources);
        }
    }

    public void swapData(List<Sources> sourcesList) {
        mSourcesList = sourcesList;
        mSourcesListFiltered = sourcesList;

        notifyDataSetChanged();
    }

    public void swapOfflineData(List<DbSources> dbSourcesList) {
        this.mDbSources = dbSourcesList;
        mDbSourcesListFiltered = dbSourcesList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSourcesListFiltered != null) {
            return mSourcesListFiltered.size();
        } else {
            return mDbSourcesListFiltered.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String sourceName = constraint.toString();

                if (sourceName.isEmpty()) {
                    if (mSourcesList != null) {
                        mSourcesListFiltered = mSourcesList;
                    } else {
                        mDbSourcesListFiltered = mDbSources;
                    }
                } else {
                    if (mSourcesList != null) {
                        List<Sources> filteredList = new ArrayList<>();

                        for (Sources sources : mSourcesList) {
                            if (sources.getName().toLowerCase().contains(sourceName.toLowerCase())) {
                                filteredList.add(sources);
                            }
                        }

                        mSourcesListFiltered = filteredList;
                    } else {
                        List<DbSources> filteredList = new ArrayList<>();

                        for (DbSources dbSources : mDbSources) {
                            if (dbSources.getName().toLowerCase().contains(sourceName.toLowerCase())) {
                                filteredList.add(dbSources);
                            }
                        }

                        mDbSourcesListFiltered = filteredList;
                    }
                }


                FilterResults filterResults = new FilterResults();

                if (mSourcesListFiltered != null) {
                    filterResults.values = mSourcesListFiltered;
                } else {
                    filterResults.values = mDbSourcesListFiltered;
                }

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (mSourcesListFiltered != null) {
                    mSourcesListFiltered = (List<Sources>) results.values;

                    notifyDataSetChanged();
                } else {
                    mDbSourcesListFiltered = (List<DbSources>) results.values;

                    notifyDataSetChanged();
                }
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SourcesItemCardBinding mSourcesItemCardBinding;

        ViewHolder(View itemView) {
            super(itemView);

            mSourcesItemCardBinding = DataBindingUtil.getBinding(itemView);
        }

        void bindViews(Sources sources) {
            if (sources != null) {
                mSourcesItemCardBinding.setVariable(BR.sourceName, sources.getName());
                mSourcesItemCardBinding.setVariable(BR.sourceDescription, sources.getDescription());
                mSourcesItemCardBinding.setVariable(BR.sourceCategory, sources.getCategory());
                mSourcesItemCardBinding.setVariable(BR.sourcesId, sources.getId());
                mSourcesItemCardBinding.setVariable(BR.sourceItemClickListener, mISourcesItemClickListener);
                mSourcesItemCardBinding.executePendingBindings();
            }
        }

        void bindViews(DbSources dbSources) {
            if (dbSources != null) {
                mSourcesItemCardBinding.setVariable(BR.sourceName, dbSources.getName());
                mSourcesItemCardBinding.setVariable(BR.sourceDescription, dbSources.getDescription());
                mSourcesItemCardBinding.setVariable(BR.sourceCategory, dbSources.getCategory());
                mSourcesItemCardBinding.setVariable(BR.sourcesId, dbSources.getId());
                mSourcesItemCardBinding.setVariable(BR.sourceItemClickListener, mISourcesItemClickListener);
                mSourcesItemCardBinding.executePendingBindings();
            }
        }
    }
}