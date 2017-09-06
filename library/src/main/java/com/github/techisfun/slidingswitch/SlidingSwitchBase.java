package com.github.techisfun.slidingswitch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;


class SlidingSwitchBase extends View {

    public static final int SHAPE_RECT = 1;
    public static final int SHAPE_CIRCLE = 2;
    private static final int RIM_SIZE = 0;
    // 3 attributes
    private boolean isOpen;
    private int shape;
    // varials of drawing
    private Paint paint;
    private Rect mBackRect;
    private Rect mFrontRect;
    private RectF mFrontCircleRect;
    private RectF mBackCircleRect;
    private int mAlpha;
    private int mMaxLeft;
    private int mMinLeft;
    private int mFrontRectLeft;
    private int mFrontRectLeftBegin = RIM_SIZE;
    private int mEventStartX;
    private int mEventLastX;
    private int mDiffX = 0;
    private boolean mSlideable = true;
    private SlideListener mSlideListener;
    private int mBgColor;
    private int mFgColor;
    private View mClippableView;

    public void setClippableView(View clippableView) {
        mClippableView = clippableView;
    }

    public SlidingSwitchBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlideListener = null;
        paint = new Paint();
        paint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SlidingSwitch);
        isOpen = a.getBoolean(R.styleable.SlidingSwitch_isOpen, false);
        mBgColor = a.getColor(R.styleable.SlidingSwitch_backgroundColor, Color.BLACK);
        mFgColor = a.getColor(R.styleable.SlidingSwitch_foregroundColor, Color.GRAY);
        shape = SHAPE_RECT; //a.getInt(R.styleable.slideswitch_shape, SHAPE_RECT);
        a.recycle();
    }

    public SlidingSwitchBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingSwitchBase(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(280, widthMeasureSpec);
        int height = measureDimension(140, heightMeasureSpec);
        if (shape == SHAPE_CIRCLE) {
            if (width < height)
                width = height * 2;
        }
        setMeasuredDimension(width, height);
        initDrawingVal();
    }

    public void initDrawingVal() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mBackCircleRect = new RectF();
        mFrontCircleRect = new RectF();
        mFrontRect = new Rect();
        mBackRect = new Rect(0, 0, width, height);
        mMinLeft = RIM_SIZE;
        if (shape == SHAPE_RECT)
            mMaxLeft = width / 2;
        else
            mMaxLeft = width - (height - 2 * RIM_SIZE) - RIM_SIZE;
        if (isOpen) {
            mFrontRectLeft = mMaxLeft;
            mAlpha = 255;
        } else {
            mFrontRectLeft = RIM_SIZE;
            mAlpha = 0;
        }
        mFrontRectLeftBegin = mFrontRectLeft;
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize; // UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shape == SHAPE_RECT) {
            paint.setColor(mBgColor);
            canvas.drawRect(mBackRect, paint);
            paint.setAlpha(mAlpha);
            canvas.drawRect(mBackRect, paint);
            mFrontRect.set(mFrontRectLeft, RIM_SIZE, mFrontRectLeft
                    + getMeasuredWidth() / 2 - RIM_SIZE, getMeasuredHeight()
                    - RIM_SIZE);
            paint.setColor(mFgColor);
            canvas.drawRect(mFrontRect, paint);

            if (mClippableView != null) {
                ViewCompat.setClipBounds(mClippableView, mFrontRect);
            }
        } else {
            // draw circle
            int radius;
            radius = mBackRect.height() / 2 - RIM_SIZE;
            paint.setColor(mBgColor);
            mBackCircleRect.set(mBackRect);
            canvas.drawRoundRect(mBackCircleRect, radius, radius, paint);
            paint.setColor(mFgColor);
            paint.setAlpha(mAlpha);
            canvas.drawRoundRect(mBackCircleRect, radius, radius, paint);
            mFrontRect.set(mFrontRectLeft, RIM_SIZE, mFrontRectLeft
                    + mBackRect.height() - 2 * RIM_SIZE, mBackRect.height()
                    - RIM_SIZE);
            mFrontCircleRect.set(mFrontRect);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(mFrontCircleRect, radius, radius, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlideable == false)
            return super.onTouchEvent(event);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mEventStartX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                mEventLastX = (int) event.getRawX();
                mDiffX = mEventLastX - mEventStartX;
                int tempX = mDiffX + mFrontRectLeftBegin;
                tempX = (tempX > mMaxLeft ? mMaxLeft : tempX);
                tempX = (tempX < mMinLeft ? mMinLeft : tempX);
                if (tempX >= mMinLeft && tempX <= mMaxLeft) {
                    mFrontRectLeft = tempX;
                    mAlpha = (int) (255 * (float) tempX / (float) mMaxLeft);
                    invalidateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                int wholeX = (int) (event.getRawX() - mEventStartX);
                mFrontRectLeftBegin = mFrontRectLeft;
                boolean toRight;
                toRight = (mFrontRectLeftBegin > mMaxLeft / 2 ? true : false);
                if (Math.abs(wholeX) < 3) {
                    toRight = !toRight;
                }
                moveToDest(toRight);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final ViewParent parent = getParent();

        if (parent != null) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    parent.requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    parent.requestDisallowInterceptTouchEvent(true);
                    break;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * draw again
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.mSlideListener = listener;
    }

    public void moveToDest(final boolean toRight) {
        ValueAnimator toDestAnim = ValueAnimator.ofInt(mFrontRectLeft,
                toRight ? mMaxLeft : mMinLeft);
        toDestAnim.setDuration(500);
        toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        toDestAnim.start();
        toDestAnim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFrontRectLeft = (Integer) animation.getAnimatedValue();
                mAlpha = (int) (255 * (float) mFrontRectLeft / (float) mMaxLeft);
                invalidateView();
            }
        });
        toDestAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (toRight && !isOpen) {
                    isOpen = true;
                    if (mSlideListener != null)
                        mSlideListener.onSecondOptionSelected();
                    mFrontRectLeftBegin = mMaxLeft;
                } else if (!toRight && isOpen) {
                    isOpen = false;
                    if (mSlideListener != null)
                        mSlideListener.onFirstOptionSelected();
                    mFrontRectLeftBegin = mMinLeft;
                }
            }
        });
    }

    public void setState(boolean isOpen) {
        this.isOpen = isOpen;
        initDrawingVal();
        invalidateView();
        if (mSlideListener != null)
            if (isOpen == true) {
                mSlideListener.onSecondOptionSelected();
            } else {
                mSlideListener.onFirstOptionSelected();
            }
    }

    public void setShapeType(int shapeType) {
        this.shape = shapeType;
    }

    public void setSlideable(boolean slideable) {
        this.mSlideable = slideable;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.isOpen = bundle.getBoolean("isOpen");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putBoolean("isOpen", this.isOpen);
        return bundle;
    }

}
