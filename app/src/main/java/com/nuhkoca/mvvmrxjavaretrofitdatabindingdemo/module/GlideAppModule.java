package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.module;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.R;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.helper.UnsafeOkHttpClient;

import java.io.InputStream;

import okhttp3.OkHttpClient;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

@GlideModule
public class GlideAppModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        /*OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();*/

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);

        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        int memoryCacheSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, memoryCacheSizeBytes));
        builder.setDefaultRequestOptions(requestOptions(context));
        builder.build(context);
    }

    private RequestOptions requestOptions(Context context) {
        return new RequestOptions()
                .signature(new ObjectKey(
                        System.currentTimeMillis() / (24 * 60 * 60 * 1000))) // 24hrs
                .centerCrop()
                .dontAnimate()
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .encodeQuality(100)
                .fallback(ContextCompat.getDrawable(context, R.drawable.not_loaded_image))
                .error(ContextCompat.getDrawable(context, R.drawable.not_loaded_image))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(PREFER_ARGB_8888)
                .skipMemoryCache(false);
    }
}