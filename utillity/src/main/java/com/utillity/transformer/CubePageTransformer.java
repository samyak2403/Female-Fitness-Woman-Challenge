package com.utillity.transformer;

import android.view.View;


public class CubePageTransformer extends BasePageTransformer {
    private float mMaxRotation = 90.0f;

    public CubePageTransformer() {
    }


    @Override
    public void handleInvisiblePage(View view, float position) {
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setPivotX(view.getMeasuredWidth());
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(mMaxRotation * position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setPivotX(0);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setRotationY(mMaxRotation * position);
    }



}
