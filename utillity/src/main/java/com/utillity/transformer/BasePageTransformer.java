package com.utillity.transformer;


import android.view.View;

import androidx.viewpager.widget.ViewPager;


public abstract class BasePageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if (position < -1.0f) {
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            handleRightPage(view, position);
        } else {
            handleInvisiblePage(view, position);
        }
    }

    public abstract void handleInvisiblePage(View view, float position);

    public abstract void handleLeftPage(View view, float position);

    public abstract void handleRightPage(View view, float position);
}
