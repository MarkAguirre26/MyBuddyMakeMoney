package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MyPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private int[] layouts;

        public MyPagerAdapter(Context context, int[] layouts) {
                if (context == null || layouts == null) {
                        throw new IllegalArgumentException("Context or layouts array cannot be null.");
                }
                this.layoutInflater = LayoutInflater.from(context);
                this.layouts = layouts;
        }

        @Override
        public int getCount() {
                return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View itemView = layoutInflater.inflate(layouts[position], container, false);
                container.addView(itemView);
                return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
        }
}
