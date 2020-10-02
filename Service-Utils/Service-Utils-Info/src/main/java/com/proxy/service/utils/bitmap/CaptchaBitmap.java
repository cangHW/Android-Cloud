package com.proxy.service.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * @author : cangHX
 * on 2020/09/23  10:23 PM
 */
public class CaptchaBitmap {

    private static final String[] KEY_CODE_CHARS = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    private static final int KEY_CODE_LENGTH = 4;
    private static final int POINT_COUNT = 255;
    private static final Random RANDOM = new Random();

    /**
     * 图片宽度
     */
    private int mWidth;
    /**
     * 图片高度
     */
    private int mHeight;

    /**
     * 验证码内容
     */
    private String mKeyCode = "";

    private CaptchaBitmap() {
    }

    public static CaptchaBitmap create() {
        return new CaptchaBitmap();
    }

    public Bitmap createBitmap(int width, int height, String keyCode, float textSize) {
        this.mWidth = width;
        this.mHeight = height;
        this.mKeyCode = keyCode;
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.BLUE);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float y = (bitmap.getHeight() >> 1) + distance;
        for (int i = 0; i < mKeyCode.length(); i++) {
            paint.setColor(randomColor());
            paint.setFakeBoldText(RANDOM.nextBoolean());
            float skewX = RANDOM.nextInt(11);
            skewX = skewX / 10;
            paint.setTextSkewX(RANDOM.nextBoolean() ? skewX : -skewX);
            int x = mWidth / mKeyCode.length() * i + RANDOM.nextInt(10);
            canvas.drawText(String.valueOf(mKeyCode.charAt(i)), x, y, paint);
        }
        int lineCount = RANDOM.nextInt(6);
        for (int i = 0; i < lineCount; i++) {
            drawLine(canvas, paint);
        }
        for (int i = 0; i < POINT_COUNT; i++) {
            drawPoints(canvas, paint);
        }
        return bitmap;
    }

    /**
     * 返回真实验证码字符串
     *
     * @return 验证码内容
     */
    public String getKeyCode() {
        return mKeyCode;
    }

    /**
     * 画随机线条
     *
     * @param canvas 画布
     * @param paint  画笔
     */
    private void drawLine(Canvas canvas, Paint paint) {
        int startX = RANDOM.nextInt(mWidth), startY = RANDOM.nextInt(mHeight);
        int stopX = RANDOM.nextInt(mWidth), stopY = RANDOM.nextInt(mHeight);
        paint.setStrokeWidth(2);
        paint.setColor(randomColor());
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 画随机干扰点
     *
     * @param canvas 画布
     * @param paint  画笔
     */
    private void drawPoints(Canvas canvas, Paint paint) {
        int stopX = RANDOM.nextInt(mWidth), stopY = RANDOM.nextInt(mHeight);
        paint.setStrokeWidth(3);
        paint.setColor(randomColor());
        canvas.drawPoint(stopX, stopY, paint);
    }

    /**
     * 获得一个随机的颜色
     *
     * @return 返回一个颜色数值
     */
    private int randomColor() {
        int red = RANDOM.nextInt(256), green = RANDOM.nextInt(256), blue = RANDOM.nextInt(256);
        return Color.rgb(red, green, blue);
    }

    public String createKeyCode() {
        int length = KEY_CODE_CHARS.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < KEY_CODE_LENGTH; i++) {
            int position = RANDOM.nextInt(length);
            if (position >= length) {
                position = length - 1;
            }
            builder.append(KEY_CODE_CHARS[position]);
        }
        return builder.toString();
    }

}
