package com.utillity.transformer;


import android.view.View;

import androidx.core.view.ViewCompat;


public class AlphaPageTransformer extends BasePageTransformer {
    private float mMinScale = 0.5f;

    public AlphaPageTransformer() {
    }


    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewCompat.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setAlpha(mMinScale + (1 - mMinScale) * (1 + position));
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setAlpha(mMinScale + (1 - mMinScale) * (1 - position));
    }

}