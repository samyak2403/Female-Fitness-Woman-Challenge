package com.utillity.compactcalender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import androidx.core.view.GestureDetectorCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CompactCalendarView extends View {

    public static final int FILL_LARGE_INDICATOR = 1;
    public static final int NO_FILL_LARGE_INDICATOR = 2;
    public static final int SMALL_INDICATOR = 3;

    private final AnimationHandler animationHandler;
    private CompactCalendarController compactCalendarController;
    private GestureDetectorCompat gestureDetector;
    private boolean horizontalScrollEnabled = true;

    public interface CompactCalendarViewListener {
        public void onDayClick(Date dateClicked);
        public void onMonthScroll(Date firstDayOfNewMonth);
    }

    public interface CompactCalendarAnimationListener {
        public void onOpened();
        public void onClosed();
    }

    private final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            compactCalendarController.onSingleTapUp(e);
            invalidate();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(horizontalScrollEnabled) {
                if (Math.abs(distanceX) > 0) {
                    getParent().requestDisallowInterceptTouchEvent(true);

                    compactCalendarController.onScroll(e1, e2, distanceX, distanceY);
                    invalidate();
                    return true;
                }
            }

            return false;
        }
    };

    public CompactCalendarView(Context context) {
        this(context, null);
    }

    public CompactCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompactCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        compactCalendarController = new CompactCalendarController(new Paint(), new OverScroller(getContext()),
                new Rect(), attrs, getContext(),  Color.argb(255, 233, 84, 81),
                Color.argb(255, 64, 64, 64),
                Color.argb(255, 219, 219, 219),
                VelocityTracker.obtain(),
                Color.argb(255, 100, 68, 65), new EventsContainer(Calendar.getInstance()),
                Locale.getDefault(), TimeZone.getDefault());

        gestureDetector = new GestureDetectorCompat(getContext(), gestureListener);
        animationHandler = new AnimationHandler(compactCalendarController, this);
    }


    public void setListener(CompactCalendarViewListener listener){
        compactCalendarController.setListener(listener);
    }


    public void addEvent(Event event){
        addEvent(event, true);
    }


    public void addEvent(Event event, boolean shouldInvalidate){
        compactCalendarController.addEvent(event);
        if(shouldInvalidate){
            invalidate();
        }
    }


    public void removeAllEvents() {
        compactCalendarController.removeAllEvents();
        invalidate();
    }


    public void scrollRight(){
        compactCalendarController.scrollRight();
        invalidate();
    }

    public void scrollLeft(){
        compactCalendarController.scrollLeft();
        invalidate();
    }


    @Override
    protected void onMeasure(int parentWidth, int parentHeight) {
        super.onMeasure(parentWidth, parentHeight);
        int width = MeasureSpec.getSize(parentWidth);
        int height = MeasureSpec.getSize(parentHeight);
        if(width > 0 && height > 0) {
            compactCalendarController.onMeasure(width, height, getPaddingRight(), getPaddingLeft());
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        compactCalendarController.onDraw(canvas);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(compactCalendarController.computeScroll()){
            invalidate();
        }
    }

    public void shouldScrollMonth(boolean enableHorizontalScroll){
        this.horizontalScrollEnabled = enableHorizontalScroll;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (horizontalScrollEnabled) {
            compactCalendarController.onTouch(event);
            invalidate();
        }

        if((event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) && horizontalScrollEnabled) {
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (this.getVisibility() == View.GONE) {
            return false;
        }
        return this.horizontalScrollEnabled;
    }

}
