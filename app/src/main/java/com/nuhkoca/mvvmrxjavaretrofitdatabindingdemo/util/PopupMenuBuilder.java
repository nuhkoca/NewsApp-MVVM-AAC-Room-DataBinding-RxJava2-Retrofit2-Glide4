package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;

public class PopupMenuBuilder implements PopupMenu.OnMenuItemClickListener {

    private String mShareIntentExtraText;
    private String mShareIntentTitle;

    private String mOpenIntentExtraText;
    private String mOpenIntentTitle;

    public PopupMenuBuilder(String mShareIntentExtraText, String mShareIntentTitle, String mOpenIntentExtraText, String mOpenIntentTitle) {
        this.mShareIntentExtraText = mShareIntentExtraText;
        this.mShareIntentTitle = mShareIntentTitle;
        this.mOpenIntentExtraText = mOpenIntentExtraText;
        this.mOpenIntentTitle = mOpenIntentTitle;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_browser_menu:
                Intent viewIntent = new Intent();
                viewIntent.setAction(Intent.ACTION_VIEW);
                viewIntent.setData(Uri.parse(mOpenIntentExtraText));
                NewsApp.getInstance().startActivity(Intent.createChooser(viewIntent,
                        mOpenIntentTitle));
                return true;
            case R.id.share_menu:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mShareIntentExtraText);
                sendIntent.setType("text/plain");
                NewsApp.getInstance().startActivity(Intent.createChooser(sendIntent,
                        mShareIntentTitle));
                return true;
            default:
        }
        return false;
    }
}