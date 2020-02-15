package com.ju.wyhouse.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ju.wyhouse.R;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author:gaoju
 * Date:2020/1/18 22:19
 * Path:com.ju.wyhouse.utils
 * Desc: Glide封装
 */
public class GlideUtil {

    public static Object OnGlideBitmapResultListener;

    /**
     * 加载图片Url
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadUrl(Context mContext, String url, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext.getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_square_p)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }




    /**
     * 加载图片Url
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadSmollUrl(Context mContext, String url, int w, int h, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext.getApplicationContext())
                    .load(url)
                    .override(w, h)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_glide_load_error)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    /**
     * 加载图片File
     *
     * @param mContext
     * @param file
     * @param imageView
     */
    public static void loadFile(Context mContext, File file, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext.getApplicationContext())
                    .load(file)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_glide_load_error)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    public static void loadUrlToCir(Context mContext, String url, ImageView imageView){}


    /**
     * 加载头像
     *
     * @param mContext
     * @param url
     * @param listener
     */
    public static void loadUrlToBitmap(Context mContext, String url, final OnGlideBitmapResultListener listener) {
        if (mContext != null) {
            Glide.with(mContext.getApplicationContext()).asBitmap().load(url).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (null != listener) {
                                listener.onResourceReady(resource);
                            }
                        }
                    });
        }
    }

    /**
     * 加载本地gif
     * @param context
     * @param drawableId
     * @param view
     */
    public static void loadGifToDrawable(Context context,int drawableId,ImageView view){
        Glide.with(context).load(drawableId).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    ((GifDrawable) resource).start();
                }
                return false;
            }
        }).into(view);
    }

    public interface OnGlideBitmapResultListener {
        void onResourceReady(Bitmap resource);
    }

}
