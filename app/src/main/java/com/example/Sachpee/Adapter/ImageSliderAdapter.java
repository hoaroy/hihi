package com.example.Sachpee.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.Sachpee.Model.ImageSlider;
import com.example.Sachpee.R;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private List<ImageSlider> imageSliderList;


    private LayoutInflater inflater;

    public ImageSliderAdapter(Context context, List<ImageSlider> imageSliderList) {
        this.imageSliderList = imageSliderList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageSliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_slider, container, false);
        ImageView imageView = view.findViewById(R.id.image_slider);
        imageView.setImageResource(imageSliderList.get(position).getImage());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

