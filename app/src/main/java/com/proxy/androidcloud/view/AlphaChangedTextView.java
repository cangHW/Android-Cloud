package com.proxy.androidcloud.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.proxy.androidcloud.R;

/**
 * @author : cangHX
 * on 2020/07/16  6:40 PM
 */
public class AlphaChangedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int mAlphaBgColorStart;
    private int mAlphaBgColorEnd;
    private int mAlphaTextColorStart;
    private int mAlphaTextColorEnd;
    private Rect mRect;
    private Paint mBgPaint;
    private Paint mTextPaint;

    public AlphaChangedTextView(Context context) {
        this(context, null);
    }

    public AlphaChangedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaChangedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        mRect = new Rect();

        mBgPaint = new Paint();
        mBgPaint.setColor(mAlphaBgColorStart);

        mTextPaint = getPaint();
        mTextPaint.setColor(mAlphaTextColorStart);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.AlphaChangedTextView);
        mAlphaBgColorStart = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaBgColorStart, Color.WHITE);
        mAlphaBgColorEnd = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaBgColorEnd, Color.BLUE);
        mAlphaTextColorStart = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaTextColorStart, Color.BLUE);
        mAlphaTextColorEnd = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaTextColorEnd, Color.WHITE);
        typeArray.recycle();
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        mRect.left = l;
        mRect.top = t;
        mRect.right = r;
        mRect.bottom = b;
    }

    @Override
    public void setAlpha(float alpha) {
        int r = (int) Math.ceil(255 * alpha);
        if (r < 10) {
            r = 0;
        } else if (r > 230) {
            r = 255;
        }

        int g = (int) Math.ceil(255 * alpha);
        if (g < 10) {
            g = 0;
        } else if (g > 230) {
            g = 255;
        }

        int b = 255;
        int textColor = Color.rgb(r, g, b);
        int bgColor = Color.rgb(255 - r, 255 - g, b);
        mBgPaint.setColor(bgColor);
        mTextPaint.setColor(textColor);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBgPaint);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = mRect.centerY() + distance;
        canvas.drawText(getText().toString(), mRect.centerX(), baseline, mTextPaint);
    }
}
