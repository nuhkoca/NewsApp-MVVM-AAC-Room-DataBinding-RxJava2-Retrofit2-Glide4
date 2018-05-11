package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.custom_news;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.NewsApp;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IOverflowMenuItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.ActivityCustomNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.InternetSnifferService;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.PopupMenuBuilder;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewUtil;

public class CustomNewsActivity extends AppCompatActivity implements IOverflowMenuItemClickListener, InternetSnifferService.ConnectivityReceiverListener {

    private CustomNewsActivityViewModel mCustomNewsActivityViewModel;
    private ActivityCustomNewsBinding mActivityCustomNewsBinding;
    private String mSourceName;
    private String mSourceId;

    private static Integer mItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCustomNewsBinding = DataBindingUtil.setContentView(this, R.layout.activity_custom_news);

        mCustomNewsActivityViewModel =
                ViewModelProviders.of(this,
                        new CustomNewsActivityViewModelFactory(getApplication(), ObservableHelper.getInstance()))
                        .get(CustomNewsActivityViewModel.class);

        setupUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();

        switch (itemThatWasClicked) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        mSourceName = getIntent().getStringExtra(Constants.CUSTOM_NEWS_SOURCE_NAME);
        mSourceId = getIntent().getStringExtra(Constants.CUSTOM_NEWS_SOURCE_ID);

        String title = String.format(getString(R.string.source_name_news), mSourceName);

        setTitle(title);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showLoadingBar();
        createPage();
        showErrorText();
        observeItemCount();
    }

    private void createPage() {
        mCustomNewsActivityViewModel.fetchTopHeadlines().observe(this, new Observer<ArticlesWrapper>() {
            @Override
            public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                if (articlesWrapper != null) {
                    RecyclerViewUtil.populateOnlineArticles(CustomNewsActivity.this,
                            mActivityCustomNewsBinding.rvCustomNews,
                            articlesWrapper.getArticles(),
                            CustomNewsActivity.this);
                }
            }
        });

        mCustomNewsActivityViewModel.getTopHeadlines(mSourceId);
    }

    private void showLoadingBar() {
        mCustomNewsActivityViewModel.mTopHeadlinesLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        mActivityCustomNewsBinding.pbCustomNews.setVisibility(View.VISIBLE);
                    } else {
                        mActivityCustomNewsBinding.pbCustomNews.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showErrorText() {
        mCustomNewsActivityViewModel.mTopHeadlinesError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isError) {
                if (isError != null) {
                    if (isError) {
                        mActivityCustomNewsBinding.tvCustomErrorView.setVisibility(View.VISIBLE);
                        mActivityCustomNewsBinding.tvCustomErrorView.setText(
                                String.format(getString(R.string.custom_sources_error_text), mSourceName));
                        mActivityCustomNewsBinding.rvCustomNews.setVisibility(View.GONE);
                    } else {
                        mActivityCustomNewsBinding.tvCustomErrorView.setVisibility(View.GONE);
                        mActivityCustomNewsBinding.rvCustomNews.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void observeItemCount() {
        mCustomNewsActivityViewModel.mItemCount.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer itemCount) {
                mItemCount = itemCount;
            }
        });
    }

    @Override
    public void onOverflowMenuItemClick(String articlesUrl, String articlesTitle, ImageView view) {
        String openTitle = String.format(getString(R.string.openIntentTitle), articlesTitle);
        String shareTitle = String.format(getString(R.string.openIntentTitle), articlesTitle);
        String shareExtraText = String.format(getString(R.string.shareIntentExtraText), articlesTitle, articlesUrl);

        PopupMenu popup = new PopupMenu(CustomNewsActivity.this, view);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenuBuilder(
                shareExtraText,
                shareTitle,
                articlesUrl,
                openTitle));

        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        NewsApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {

            if (mItemCount == 0) {
                setupUI();

                Toast.makeText(CustomNewsActivity.this, getString(R.string.urgent_internet_info), Toast.LENGTH_SHORT).show();
            }
        }
    }
}