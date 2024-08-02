package com.utillity.transformer;

import android.view.View;

public class ZoomPageTransformer extends BasePageTransformer {
    private float mMinScale = 0.85f;
    private float mMinAlpha = 0.65f;
    private boolean  isShowAlpha = true;

    public ZoomPageTransformer() {
    }



    @Override
    public void handleInvisiblePage(View view, float position) {
        if (isShowAlpha)view.setAlpha(0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        handler(view, position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        handler(view, position);
    }

    private void handler(View view, float position) {

        float scaleFactor = Math.max(mMinScale, 1 - Math.abs(position));


        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);

        if (isShowAlpha){
            view.setAlpha(mMinAlpha + (scaleFactor - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));
        }
    }


}
