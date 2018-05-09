package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.ui.news;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IOverflowMenuItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.IRecyclerViewScrollListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.callback.ISourcesItemClickListener;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbEverything;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbSources;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.entity.DbTopHeadlines;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.ArticlesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.remote.SourcesWrapper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.repository.INewsAPI;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.databinding.FragmentNewsBinding;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.ObservableHelper;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.test.SimpleIdlingResource;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.ConnectionSniffer;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewScrollUtil;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.util.RecyclerViewUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentNewsBinding mFragmentNewsBinding;
    private NewsFragmentViewModel mNewsFragmentViewModel;
    private SharedPreferences mSharedPreferences;
    private int mEndpointCode;
    private MaterialDialog mMaterialDialog;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    @NonNull
    public static NewsFragment getInstance() {
        return new NewsFragment();
    }

    private static IOverflowMenuItemClickListener sIOverflowMenuItemClickListener;
    private static ISourcesItemClickListener sISourcesItemClickListener;
    private static IRecyclerViewScrollListener sIRecyclerViewScrollListener;

    public static NewsFragment getInstance(INewsAPI.Endpoints endpoints, IOverflowMenuItemClickListener iOverflowMenuItemClickListener, IRecyclerViewScrollListener iRecyclerViewScrollListener) {
        NewsFragment newsFragment = new NewsFragment();

        sIOverflowMenuItemClickListener = iOverflowMenuItemClickListener;
        sIRecyclerViewScrollListener = iRecyclerViewScrollListener;

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ENDPOINT_ARGS_KEY, endpoints.getValue());
        newsFragment.setArguments(bundle);

        return newsFragment;
    }

    public static NewsFragment getInstance(INewsAPI.Endpoints endpoints, ISourcesItemClickListener iSourcesItemClickListener, IRecyclerViewScrollListener iRecyclerViewScrollListener) {
        NewsFragment newsFragment = new NewsFragment();

        sISourcesItemClickListener = iSourcesItemClickListener;
        sIRecyclerViewScrollListener = iRecyclerViewScrollListener;

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

        mNewsFragmentViewModel = ViewModelProviders
                .of(this, new NewsFragmentViewModelFactory(Objects.requireNonNull(getActivity()).getApplication(),
                        ObservableHelper.getInstance())).get(NewsFragmentViewModel.class);

        return mFragmentNewsBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getIdlingResource();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        sIRecyclerViewScrollListener = (IRecyclerViewScrollListener) getActivity();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.search_menu);

        if (mEndpointCode == Constants.TOP_NEWS_ID
                || mEndpointCode == Constants.SOURCES_ID) {
            menuItem.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.search_menu);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();

                MaterialDialog.Builder builder = new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                        .content(getString(R.string.progress_dialog_wait))
                        .progress(true, 0)
                        .cancelable(false)
                        .widgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                mMaterialDialog = builder.build();
                mMaterialDialog.show();

                saveQueryToSharedPreference(query);
                loadEverythingFromQuery();

                searchView.setIconified(true);
                searchView.clearFocus();
                menu.findItem(R.id.search_menu).collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       mFragmentNewsBinding.rvNews.addOnScrollListener(new RecyclerViewScrollUtil() {
           @Override
           public void onHide() {
               sIRecyclerViewScrollListener.onViewsHide();
           }

           @Override
           public void onShow() {
                sIRecyclerViewScrollListener.onViewsShow();
           }
       });

        setupUI();
    }

    private void setupUI() {
        mEndpointCode = Objects.requireNonNull(getArguments()).getInt(Constants.ENDPOINT_ARGS_KEY);

        showLoadingBar();
        showError(mEndpointCode);
        createPages(mEndpointCode);
    }

    private void loadTopHeadlineParametersFromPreferences(SharedPreferences sharedPreferences) {
        Set<String> sourceSet = new HashSet<>();
        sourceSet.add(getString(R.string.pref_sources_all_value));

        List<String> entries = new ArrayList<>(Objects.requireNonNull(
                sharedPreferences.getStringSet(getString(R.string.pref_source_key), sourceSet)));
        StringBuilder selectedSources = new StringBuilder();

        for (int i = 0; i < entries.size(); i++) {
            selectedSources.append(entries.get(i)).append(",");
        }

        if (selectedSources.length() > 0) {
            selectedSources.deleteCharAt(selectedSources.length() - 1);
        }

        mNewsFragmentViewModel.getTopHeadlines(
                sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key),
                        getString(R.string.pref_country_us_value)),
                selectedSources.toString(),
                sharedPreferences.getString(getString(R.string.pref_category_key), null), null);

        if (Objects.equals(sharedPreferences.getString(getString(R.string.pref_top_headlines_country_key), ""), "")
                && Objects.equals(sharedPreferences.getString(getString(R.string.pref_category_key), ""), "")
                && entries.size() == 0) {

            Toast.makeText(getContext(), getString(R.string.pref_screen_no_parameter_error), Toast.LENGTH_LONG).show();
        }
    }

    private void loadNewsSourceParametersFromPreferences(SharedPreferences sharedPreferences) {
        mNewsFragmentViewModel.getSources
                (sharedPreferences.getString(getString(R.string.pref_language_key), null),
                        sharedPreferences.getString(getString(R.string.pref_source_country_key), null),
                        sharedPreferences.getString(getString(R.string.pref_source_category_key), null));
    }

    private void loadEverythingFromQuery() {
        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String query = prefs.getString(Constants.QUERY_PREF, "news");

        mNewsFragmentViewModel.getEverything(query);
    }

    private void saveQueryToSharedPreference(String query) {
        SharedPreferences.Editor editor =
                Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.QUERY_PREF, query);

        editor.apply();
    }

    private void createPages(int endpointCode) {
        switch (endpointCode) {
            case Constants.TOP_NEWS_ID:
                mNewsFragmentViewModel.fetchTopHeadlines().observe(this, new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            RecyclerViewUtil.populateOnlineArticles(Objects.requireNonNull(getContext()),
                                    mFragmentNewsBinding.rvNews,
                                    articlesWrapper.getArticles(),
                                    sIOverflowMenuItemClickListener);

                            if (mIdlingResource != null) {
                                mIdlingResource.setIdleState(true);
                            }
                        }
                    }
                });

                loadTopHeadlineParametersFromPreferences(mSharedPreferences);
                break;

            case Constants.EVERYTHING_ID:
                mNewsFragmentViewModel.fetchEverything().observe(this, new Observer<ArticlesWrapper>() {
                    @Override
                    public void onChanged(@Nullable ArticlesWrapper articlesWrapper) {
                        if (articlesWrapper != null) {
                            RecyclerViewUtil.populateOnlineArticles(Objects.requireNonNull(getContext()),
                                    mFragmentNewsBinding.rvNews,
                                    articlesWrapper.getArticles(),
                                    sIOverflowMenuItemClickListener);

                            if (mIdlingResource != null) {
                                mIdlingResource.setIdleState(true);
                            }
                        }

                        if (mMaterialDialog != null) {
                            mMaterialDialog.dismiss();
                        }
                    }
                });

                loadEverythingFromQuery();

                break;

            case Constants.SOURCES_ID:
                mNewsFragmentViewModel.fetchSources().observe(this, new Observer<SourcesWrapper>() {
                    @Override
                    public void onChanged(@Nullable SourcesWrapper sourcesWrapper) {
                        if (sourcesWrapper != null) {
                            RecyclerViewUtil.populateOnlineSources(getContext(),
                                    mFragmentNewsBinding.rvNews,
                                    sourcesWrapper.getSources(),
                                    sISourcesItemClickListener);

                            if (mIdlingResource != null) {
                                mIdlingResource.setIdleState(true);
                            }
                        }
                    }
                });

                loadNewsSourceParametersFromPreferences(mSharedPreferences);
                break;

            default:
                break;
        }
    }

    private void populateOfflineSources() {
        mNewsFragmentViewModel.getAllSources().observe(this, new Observer<List<DbSources>>() {
            @Override
            public void onChanged(@Nullable List<DbSources> dbSourcesList) {
                RecyclerViewUtil.populateOfflineSources(getContext(),
                        mFragmentNewsBinding.rvNews,
                        dbSourcesList,
                        sISourcesItemClickListener);

                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateOfflineTopHeadlines() {
        mNewsFragmentViewModel.getAllTopHeadlines().observe(this, new Observer<List<DbTopHeadlines>>() {
            @Override
            public void onChanged(@Nullable List<DbTopHeadlines> dbTopHeadlinesList) {
                RecyclerViewUtil.populateOfflineTopHeadlines(getContext(),
                        mFragmentNewsBinding.rvNews,
                        dbTopHeadlinesList,
                        sIOverflowMenuItemClickListener);

                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);
            }
        });
    }

    private void populateOfflineEverything() {
        mNewsFragmentViewModel.getAllEverything().observe(this, new Observer<List<DbEverything>>() {
            @Override
            public void onChanged(@Nullable List<DbEverything> dbEverythingList) {
                RecyclerViewUtil.populateOfflineEverything(getContext(),
                        mFragmentNewsBinding.rvNews,
                        dbEverythingList,
                        sIOverflowMenuItemClickListener);

                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showLoadingBar() {
        mNewsFragmentViewModel.mTopHeadlinesLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        mFragmentNewsBinding.pbNews.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.pbNews.setVisibility(View.GONE);
                    }
                }
            }
        });

        mNewsFragmentViewModel.mEverythingLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        mFragmentNewsBinding.pbNews.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.pbNews.setVisibility(View.GONE);
                    }
                }
            }
        });

        mNewsFragmentViewModel.mSourcesLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        mFragmentNewsBinding.pbNews.setVisibility(View.VISIBLE);
                    } else {
                        mFragmentNewsBinding.pbNews.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void showError(int endpointCode) {
        final boolean isAvailableConnection = ConnectionSniffer.sniff();

        switch (endpointCode) {
            case Constants.TOP_NEWS_ID:
                mNewsFragmentViewModel.mTopHeadlinesError.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isError) {
                        if (isError != null) {
                            if (isError) {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.VISIBLE);
                                mFragmentNewsBinding.tvErrorView.setText(getString(R.string.top_headlines_error_text));
                                mFragmentNewsBinding.rvNews.setVisibility(View.GONE);

                                if (!isAvailableConnection) {
                                    populateOfflineTopHeadlines();
                                }

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(false);
                                }
                            } else {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(true);
                                }
                            }
                        }
                    }
                });

                break;

            case Constants.EVERYTHING_ID:
                mNewsFragmentViewModel.mEverythingError.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isError) {
                        if (isError != null) {
                            if (isError) {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.VISIBLE);
                                mFragmentNewsBinding.tvErrorView.setText(getString(R.string.everything_error_text));
                                mFragmentNewsBinding.rvNews.setVisibility(View.GONE);

                                if (!isAvailableConnection) {
                                    populateOfflineEverything();
                                }

                                if (mMaterialDialog != null) {
                                    mMaterialDialog.dismiss();
                                }

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(false);
                                }

                            } else {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(true);
                                }
                            }
                        }
                    }
                });

                break;

            case Constants.SOURCES_ID:
                mNewsFragmentViewModel.mSourcesError.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean isError) {
                        if (isError != null) {
                            if (isError) {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.VISIBLE);
                                mFragmentNewsBinding.tvErrorView.setText(getString(R.string.sources_error_text));
                                mFragmentNewsBinding.rvNews.setVisibility(View.GONE);

                                if (!isAvailableConnection) {
                                    populateOfflineSources();
                                }

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(false);
                                }

                            } else {
                                mFragmentNewsBinding.tvErrorView.setVisibility(View.GONE);
                                mFragmentNewsBinding.rvNews.setVisibility(View.VISIBLE);

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(true);
                                }
                            }
                        }
                    }
                });

                break;

            default:
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_top_headlines_country_key))
                || key.equals(getString(R.string.pref_category_key))
                || key.equals(getString(R.string.pref_source_key))) {
            loadTopHeadlineParametersFromPreferences(sharedPreferences);
        }
        if (key.equals(getString(R.string.pref_language_key))
                || key.equals(getString(R.string.pref_source_country_key))
                || key.equals(getString(R.string.pref_source_category_key))) {
            loadNewsSourceParametersFromPreferences(sharedPreferences);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}