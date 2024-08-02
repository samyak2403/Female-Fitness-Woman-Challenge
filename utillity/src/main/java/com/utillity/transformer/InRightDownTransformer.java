package com.utillity.transformer;

import android.view.View;

public class InRightDownTransformer extends BasePageTransformer {

    @Override
    public void handleInvisiblePage(View view, float position) {
        view.setTranslationY(0);
        view.setAlpha(1);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        view.setTranslationY(view.getHeight() * position);
        view.setAlpha(1 + position);
    }

    @Override
    public void handleRightPage(View view, float position) {
        view.setTranslationY(view.getHeight() * position);
        view.setAlpha(1 - position);
    }

}
