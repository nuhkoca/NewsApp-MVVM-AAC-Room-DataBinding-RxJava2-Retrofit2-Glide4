package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback;

import android.widget.ImageView;

public interface IOverflowMenuItemClickListener {
    void onOverflowMenuItemClick(String articlesUrl, String articlesTitle, ImageView view);
}
