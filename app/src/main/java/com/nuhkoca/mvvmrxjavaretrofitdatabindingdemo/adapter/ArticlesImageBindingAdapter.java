package com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.adapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nuhkoca.mvvmrxjavaretrofitdatabindingdemo.module.GlideApp;

public class ArticlesImageBindingAdapter {

    @BindingAdapter(value = {"android:src"})
    public static void bindArticleImage(ImageView articleImage, String url) {
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(articleImage.getContext())
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(articleImage);
        }
    }
}