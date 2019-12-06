package com.github.techisfun.slidingswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea Maglie on 18/08/17.
 */

public class SlidingSwitch extends FrameLayout {

    private SlidingSwitchBase mSlidingSwitchBase;
    private LinearLayout mTextLayerSelected;
    private List<TextView> mOption1 = new ArrayList<>(2);
    private List<TextView> mOption2 = new ArrayList<>(2);
    private int mPaddingVertical;
    private int mPaddingHorizontal;
    private int mTextSize;
    private String mFirstOption;
    private String mSecondOption;
    private int mTextColor;

    public SlidingSwitch(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SlidingSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SlidingSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingSwitch, defStyleAttr, 0);
        mPaddingVertical = a.getDimensionPixelSize(R.styleable.SlidingSwitch_paddingVertical, 0);
        mPaddingHorizontal = a.getDimensionPixelSize(R.styleable.SlidingSwitch_paddingHorizontal, 0);
        mTextSize = a.getDimensionPixelSize(R.styleable.SlidingSwitch_textSize, 17);
        mTextColor = a.getColor(R.styleable.SlidingSwitch_textColor, Color.WHITE);
        mFirstOption = a.getString(R.styleable.SlidingSwitch_firstOption);
        mSecondOption = a.getString(R.styleable.SlidingSwitch_secondOption);
        a.recycle();

        mSlidingSwitchBase = new SlidingSwitchBase(context, attrs, defStyleAttr);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mSlidingSwitchBase.setLayoutParams(params);
        mSlidingSwitchBase.setId(View.generateViewId());
        addView(mSlidingSwitchBase);

        addTextLayer(context, attrs);
        mTextLayerSelected = addTextLayer(context, attrs);

        mSlidingSwitchBase.setClippableView(mTextLayerSelected);
    }

    private LinearLayout addTextLayer(Context context, AttributeSet attrs) {
        LinearLayout linearLayout = new LinearLayout(context, attrs);
        linearLayout.setId(View.generateViewId());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(linearLayout);

        TextView option1 = buildTextView(context, mFirstOption);
        linearLayout.addView(option1);
        mOption1.add(option1);

        TextView option2 = buildTextView(context, mSecondOption);
        linearLayout.addView(option2);
        mOption2.add(option2);
        return linearLayout;
    }

    private TextView buildTextView(Context context, CharSequence text) {
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        textView.setLayoutParams(params);
        textView.setTextColor(mTextColor);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        textView.setMaxLines(3);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(mPaddingHorizontal, mPaddingVertical, mPaddingHorizontal, mPaddingVertical);
        return textView;
    }

    public void setSlideListener(SlideListener slideListener) {
        mSlidingSwitchBase.setSlideListener(slideListener);
    }

    public void setFirstOption(String option1) {
        for (TextView textView : mOption1) {
            textView.setText(option1);
        }
    }

    public void setSecondOption(String option2) {
        for (TextView textView : mOption2) {
            textView.setText(option2);
        }
    }

    public void setState(boolean isOpen) {
        mSlidingSwitchBase.setState(isOpen, false);
    }

    public boolean getState() {
        return mSlidingSwitchBase.getState();
    }
}
