package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.duan1.R;
import com.example.duan1.model.photosSlider;

import java.util.List;

public class slidersAdapter extends PagerAdapter {
    private Context context;
    private List<photosSlider> mListPhoto;

    public slidersAdapter(Context context, List<photosSlider> mListPhoto) {
        this.context = context;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slider , container , false);
        ImageView imgPhoto = view.findViewById(R.id.img_slide);

        photosSlider photosSlider = mListPhoto.get(position);

        if(photosSlider != null) {
            Glide.with(context).load(photosSlider.getImgSlider()).into(imgPhoto);
        }

//        add view to viewGroup
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto != null) {
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

