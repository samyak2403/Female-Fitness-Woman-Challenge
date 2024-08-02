package com.utillity.custom;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class KKViewPager extends ViewPager implements ViewPager.PageTransformer {

    public static final String TAG = "KKViewPager";
    private float MAX_SCALE = 0.0f;
    private int mPageMargin;
    private boolean animationEnabled = true;
    private boolean fadeEnabled = false;
    private float fadeFactor = 0.5f;

    public KKViewPager(Context context) {
        this(context, null);
    }

    public KKViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        setClipToPadding(false);
        setOverScrollMode(2);
        setPageTransformer(false, this);
        setOffscreenPageLimit(3);
        mPageMargin = dp2px(context.getResources(), 40);
        setPadding(mPageMargin, mPageMargin, mPageMargin, mPageMargin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int childMeasuredHeight = child.getMeasuredHeight();
                if (childMeasuredHeight > height) {
                    height = childMeasuredHeight - 50;
                }
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }


    @Override
    public void setPageMargin(int marginPixels) {
        mPageMargin = marginPixels;
        setPadding(mPageMargin, 0, mPageMargin, 0);
    }

    @Override
    public void transformPage(View page, float position) {
        if (mPageMargin <= 0 || !animationEnabled)
            return;
        page.setPadding(mPageMargin / 3, mPageMargin / 3, mPageMargin / 3, mPageMargin / 3);

        if (MAX_SCALE == 0.0f && position > 0.0f && position < 1.0f) {
            MAX_SCALE = position;
        }
        position = position - MAX_SCALE;
        float absolutePosition = Math.abs(position);
        if (position <= -1.0f || position >= 1.0f) {
            if (fadeEnabled)
                page.setAlpha(fadeFactor);

        } else if (position == 0.0f) {

            page.setScaleX((1 + MAX_SCALE));
            page.setScaleY((1 + MAX_SCALE));
            page.setAlpha(1);
        } else {
            page.setScaleX(1 + MAX_SCALE * (1 - absolutePosition));
            page.setScaleY(1 + MAX_SCALE * (1 - absolutePosition));
            if (fadeEnabled)
                page.setAlpha(Math.max(fadeFactor, 1 - absolutePosition));
        }
    }

}
