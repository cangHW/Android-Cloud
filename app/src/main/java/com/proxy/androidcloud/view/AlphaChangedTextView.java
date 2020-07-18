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

import java.util.ArrayList;
import java.util.List;

/**
 * @author : cangHX
 * on 2020/07/16  6:40 PM
 */
public class AlphaChangedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TYPE_BG = "bg";
    private static final String TYPE_TEXT = "text";

    private static class ColorInfo {
        private String type;

        private int red = 0;
        private int redDiff = 0;

        private int green = 0;
        private int greenDiff = 0;

        private int blue = 0;
        private int blueDiff = 0;

    }

    private List<ColorInfo> mColorInfoList = new ArrayList<>();
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

        mRect = new Rect();
        mBgPaint = new Paint();
        mTextPaint = getPaint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.AlphaChangedTextView);
        int alphaBgColorStart = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaBgColorStart, Color.WHITE);
        int alphaBgColorEnd = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaBgColorEnd, Color.BLUE);
        int alphaTextColorStart = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaTextColorStart, Color.BLUE);
        int alphaTextColorEnd = typeArray.getColor(R.styleable.AlphaChangedTextView_AlphaTextColorEnd, Color.WHITE);
        typeArray.recycle();

        mBgPaint.setColor(alphaBgColorStart);
        mTextPaint.setColor(alphaTextColorStart);

        mColorInfoList.clear();
        createColorInfo(
                TYPE_BG,
                (alphaBgColorStart & 0xff0000) >> 16,
                (alphaBgColorEnd & 0xff0000) >> 16,
                (alphaBgColorStart & 0x00ff00) >> 8,
                (alphaBgColorEnd & 0x00ff00) >> 8,
                (alphaBgColorStart & 0x0000ff),
                (alphaBgColorEnd & 0x0000ff)
        );
        createColorInfo(
                TYPE_TEXT,
                (alphaTextColorStart & 0xff0000) >> 16,
                (alphaTextColorEnd & 0xff0000) >> 16,
                (alphaTextColorStart & 0x00ff00) >> 8,
                (alphaTextColorEnd & 0x00ff00) >> 8,
                (alphaTextColorStart & 0x0000ff),
                (alphaTextColorEnd & 0x0000ff)
        );
    }

    private void createColorInfo(String type, int redStart, int redEnd, int greenStart, int greenEnd, int blueStart, int blueEnd) {
        ColorInfo info = new ColorInfo();
        info.type = type;

        info.red = redStart;
        info.redDiff = redEnd - redStart;

        info.green = greenStart;
        info.greenDiff = greenEnd - greenStart;

        info.blue = blueStart;
        info.blueDiff = blueEnd - blueStart;

        mColorInfoList.add(info);
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
        for (ColorInfo info : mColorInfoList) {
            int redDiff = (int) Math.ceil(info.redDiff * alpha);
            int greenDiff = (int) Math.ceil(info.greenDiff * alpha);
            int blueDiff = (int) Math.ceil(info.blueDiff * alpha);

            int r = info.red + redDiff;
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }

            int g = info.green + greenDiff;
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }

            int b = info.blue + blueDiff;
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }

            int color = Color.rgb(r, g, b);

            if (info.type.equals(TYPE_BG)) {
                mBgPaint.setColor(color);
            } else {
                mTextPaint.setColor(color);
            }
        }

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
