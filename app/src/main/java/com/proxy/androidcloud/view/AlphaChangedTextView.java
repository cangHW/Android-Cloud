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
        if (alpha >= 0.05) {
            int alphas = (int) Math.ceil(255 * alpha);
            mBgPaint.setColor(mAlphaBgColorEnd);
            mBgPaint.setAlpha(alphas);

            mTextPaint.setColor(mAlphaTextColorEnd);
            mTextPaint.setAlpha(alphas);
        } else {
            mBgPaint.setColor(mAlphaBgColorStart);
            mBgPaint.setAlpha(255);

            mTextPaint.setColor(mAlphaTextColorStart);
            mTextPaint.setAlpha(255);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBgPaint);
//        mTextPaint.setColor(mColor);
//        mTextPaint.setAlpha(alpha);
        int x = mRect.width() / 2;
        int y = mRect.height() / 2;
        canvas.drawText(getText().toString(), x, y, mTextPaint);
    }
}
