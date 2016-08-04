package com.jym.galleryview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.jym.galleryview.R;

/**
 * Created by Jimmy on 2016/8/2.
 */
public class GalleryView extends HorizontalScrollView {

    private LinearLayout llContent;
    private int width;
    private int spacing; // dp
    private float scrollX;
    private int colCount;
    private boolean autoCut;

    public GalleryView(Context context) {
        this(context, null);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GalleryView);
        autoCut = array.getBoolean(R.styleable.GalleryView_autoCut, true);
        colCount = array.getInteger(R.styleable.GalleryView_colCount, 2);
        spacing = (int) array.getDimension(R.styleable.GalleryView_spacing, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAttr();
    }

    private void initAttr() {
        View firstChild = getChildAt(0);
        if (firstChild instanceof LinearLayout) {
            llContent = (LinearLayout) firstChild;
            if (llContent.getChildCount() > 0) {
                llContent.setVisibility(INVISIBLE);
            } else {
                llContent.setVisibility(VISIBLE);
            }
        } else {
            throw new ClassCastException("GalleryView first child must be LinearLayout");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrollX = getScrollX();
                break;
            case MotionEvent.ACTION_UP:
                if (llContent == null) return super.onTouchEvent(ev);
                int movePosition = 0;
                for (int i = 0, childCount = llContent.getChildCount(); i < childCount; i++) {
                    View child = llContent.getChildAt(i);
                    if (getScrollX() < (child.getLeft() + child.getRight()) / 2) {
                        if (scrollX >= llContent.getChildAt(childCount - 2).getLeft() &&
                                getScrollX() > llContent.getChildAt(childCount - 2).getLeft()) {
                            movePosition = llContent.getChildAt(childCount - 1).getLeft();
                        } else {
                            movePosition = child.getLeft();
                        }
                        break;
                    } else if (getScrollX() < child.getRight()) {
                        if (i < childCount - 1) {
                            movePosition = llContent.getChildAt(i + 1).getLeft();
                        } else {
                            movePosition = child.getRight() + spacing;
                        }
                        break;
                    } else {
                        continue;
                    }
                }
                smoothScrollTo(movePosition, 0);
                return true;
        }
        return super.onTouchEvent(ev);
    }


    public void resetAllChildParams() {
        if (llContent == null) return;
        if (autoCut) {
            for (int i = 0, childCount = llContent.getChildCount(); i < childCount; i++) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getChildAt(i).getLayoutParams();
                params.width = (width - spacing) / colCount;
                llContent.getChildAt(i).setLayoutParams(params);
            }
        }
        llContent.setVisibility(VISIBLE);
    }

    public void showPrevious() {
        if (llContent == null) return;
        int movePosition = 0;
        for (int i = 0, childCount = llContent.getChildCount(); i < childCount; i++) {
            View child = llContent.getChildAt(i);
            if (getScrollX() >= child.getLeft() && getScrollX() < child.getRight()) {
                if (i > 0) {
                    movePosition = llContent.getChildAt(i - 1).getLeft();
                }
            }
        }
        smoothScrollTo(movePosition, 0);
    }

    public void showNext() {
        if (llContent == null) return;
        int movePosition = 0;
        for (int i = 0, childCount = llContent.getChildCount(); i < childCount; i++) {
            View child = llContent.getChildAt(i);
            if (getScrollX() >= child.getLeft() && getScrollX() < child.getRight()) {
                if (i < childCount - 1) {
                    movePosition = llContent.getChildAt(i + 1).getLeft();
                }
            }
        }
        smoothScrollTo(movePosition, 0);
    }

    public void addItemView(View view) {
        if (llContent == null) return;
        llContent.addView(view);
        if (autoCut) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = (width - spacing) / colCount;
            view.setLayoutParams(params);
        }
    }

    public void removeItemViewAt(int index) {
        if (llContent == null) return;
        llContent.removeViewAt(index);
    }

}
