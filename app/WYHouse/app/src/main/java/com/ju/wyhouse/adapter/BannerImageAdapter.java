package com.ju.wyhouse.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ju.wyhouse.R;
import com.ju.wyhouse.utils.GlideUtil;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * Author:gaoju
 * Date:2020/1/27 19:37
 * Path:com.ju.wyhouse.adapter
 * Desc:
 */
public class BannerImageAdapter extends BannerAdapter<Integer, BannerImageAdapter.BannerViewHolder> {

    private Context context;


    public BannerImageAdapter(List<Integer> datas, Context context) {
        super(datas);
        this.context = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Integer data, int position, int size) {
        GlideUtil.loadGifToDrawable(context, data, holder.imageView);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }


    public void stopGif() {

    }
}
