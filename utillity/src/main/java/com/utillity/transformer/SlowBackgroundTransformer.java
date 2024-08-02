package com.utillity.transformer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class SlowBackgroundTransformer extends BasePageTransformer {

    private float speed = 0.5f;

    public SlowBackgroundTransformer() {

    }


    @Override
    public void handleInvisiblePage(View view, float position) {

    }

    @Override
    public void handleLeftPage(View view, float position) {
        handlerImageView(view, position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        handlerImageView(view, position);
    }

    private void handlerImageView(View view, float position) {
        ImageView imageView = null;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof ImageView) {
                    imageView = (ImageView) childView;
                }
            }
        }
        if (imageView != null) {
            imageView.setTranslationX(-view.getWidth() * position * speed);
        }
    }
}
