package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.data.local.AppDatabase;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class NewsApp extends Application {

    private static NewsApp newsApp;

    public static NewsApp getInstance() {
        return newsApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        provideTimber();
        provideStetho();

        newsApp = this;
    }

    public static Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();
    }

    public static Retrofit provideRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new StethoInterceptor());
        httpClient.interceptors().add(logging);

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .client(httpClient.build())
                .build();
    }

    public static AppDatabase provideAppDatabase() {
        return AppDatabase.getInstance(getInstance().getApplicationContext());
    }

    public void provideTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void provideStetho() {
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));

        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }
}