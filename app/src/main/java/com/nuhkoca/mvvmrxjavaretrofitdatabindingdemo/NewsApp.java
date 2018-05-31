package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.Constants;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.messaging.FirestoreDatabaseHandler;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.InternetSnifferService;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class NewsApp extends Application {

    private static NewsApp newsApp;

    public static synchronized NewsApp getInstance() {
        return newsApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        provideLeakCanary();
        provideTimber();
        provideStetho();

        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();

        if (!TextUtils.isEmpty(token)) {
            addTokenToSharedPreference(token);

            if (TextUtils.isEmpty(getTokenFromSharedPreference())) {
                FirestoreDatabaseHandler firestoreDatabaseHandler = new FirestoreDatabaseHandler(token, 1, getBaseContext());
                firestoreDatabaseHandler.checkAndSaveToken();
            }
        }

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
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.addInterceptor(new StethoInterceptor());
        httpClient.interceptors().add(logging);

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .client(httpClient.build())
                .build();
    }

    private void provideTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void provideStetho() {
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));

        Stetho.Initializer initializer = initializerBuilder.build();
        Stetho.initialize(initializer);
    }

    private void provideLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
    }

    public static FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }

    public void setConnectivityListener(InternetSnifferService.ConnectivityReceiverListener listener) {
        InternetSnifferService.connectivityReceiverListener = listener;
    }

    private void addTokenToSharedPreference(String token) {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.TOKEN_PREF_NAME, MODE_PRIVATE).edit();

        editor.putString(Constants.TOKEN_PREF, token);

        Timber.d("Token " + token);

        editor.apply();
    }

    private String getTokenFromSharedPreference() {
        SharedPreferences prefs = getSharedPreferences(Constants.TOKEN_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(Constants.TOKEN_PREF, "");
    }
}