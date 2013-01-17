package com.android.appointment.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

public class CircleView extends View {
    Point mStartPoint;
    Point mEndPoint;

    float startX = 70;
    float startY = 70;

    int step = 150;
    int radius = 60;

    int beginColor = Color.WHITE;
    int endColor = Color.BLACK;

    int mWidth;
    int mHeight;
    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, int width, int height) {
        super(context);
        mWidth = width;
        mHeight = height;
    }

    public void setViewPoint(Point startPoint, Point endPoint) {
        mStartPoint = startPoint;
        mEndPoint = endPoint;
    }

    public void setWidthAndHeight(int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(10, 10, 130, 130, beginColor, endColor, Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        canvas.drawCircle(startX, startY, radius, paint);

        int[] gradientColors = new int[4];
        gradientColors[0] = Color.GREEN;
        gradientColors[1] = Color.YELLOW;
        gradientColors[2] = Color.RED;
        gradientColors[3] = Color.BLACK;

        float[] gradientPositions = new float[4];
        gradientPositions[0] = 0.0f;
        gradientPositions[1] = 0.3f;
        gradientPositions[2] = 0.7f;
        gradientPositions[3] = 1.0f;

        RadialGradient radialGradientShader = new RadialGradient(startX + step, startY, radius,
                gradientColors, gradientPositions, Shader.TileMode.CLAMP);

        RadialGradient radialGradient = new RadialGradient(startX + step, startY, radius, beginColor, endColor, Shader.TileMode.CLAMP);
        paint.reset();
        paint.setShader(radialGradientShader);
        canvas.drawCircle(startX + step, startY, radius, paint);

        SweepGradient sweepGradient = new SweepGradient(startX + step * 2, startY, beginColor, endColor);
        paint.reset();
        paint.setShader(sweepGradient);
        canvas.drawCircle(startX + step * 2, startY, radius, paint);
    }
}
