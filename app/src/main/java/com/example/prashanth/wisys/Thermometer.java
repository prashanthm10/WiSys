package com.example.prashanth.wisys;

/**
 * Created by ${SHASHIKANt} on 06-08-2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;


public class Thermometer extends View {
    private float outerCircleRadius;
    private float outerRectRadius;
    private Paint outerPaint;
    private float middleCircleRadius;
    private float middleRectRadius;
    private Paint middlePaint;
    private float innerCircleRadius;
    private float innerRectRadius;
    private Paint innerPaint;

    private Paint degreePaint;

    private static final float DEGREE_WIDTH = 20;
    private static final float MAX_TEMP = 50;
    private static final float MIN_TEMP = 0;
    private static final float RANGE_TEMP = 50;
    private float currentTemp = 20;

    public void setCurrentTemp(float currentTemp) {
        if (currentTemp > MAX_TEMP) {
            this.currentTemp = MAX_TEMP;
        } else if (currentTemp < MIN_TEMP) {
            this.currentTemp = MIN_TEMP;
        } else {
            this.currentTemp = currentTemp;
        }
        invalidate();
    }

    public Thermometer(Context context) {
        super(context);
        init(context, null);
    }

    public Thermometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Thermometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Thermometer);
        outerCircleRadius = typedArray.getDimension(R.styleable.Thermometer_radius, 20f);
        int outerColor = typedArray.getColor(R.styleable.Thermometer_outerColor, Color.GRAY);
        int middleColor = typedArray.getColor(R.styleable.Thermometer_middleColor, Color.WHITE);
        int innerColor = typedArray.getColor(R.styleable.Thermometer_innerColor, Color.RED);
        typedArray.recycle();


        outerRectRadius = outerCircleRadius / 2;
        outerPaint = new Paint();
        outerPaint.setColor(outerColor);
        outerPaint.setStyle(Paint.Style.FILL);

        middleCircleRadius = outerCircleRadius - 5;
        middleRectRadius = outerRectRadius - 5;
        middlePaint = new Paint();
        middlePaint.setColor(middleColor);
        middlePaint.setStyle(Paint.Style.FILL);

        innerCircleRadius = middleCircleRadius - 10;
        innerRectRadius = middleRectRadius - 10;
        innerPaint = new Paint();
        innerPaint.setColor(innerColor);
        innerPaint.setStyle(Paint.Style.FILL);


        degreePaint = new Paint();
        degreePaint.setStrokeWidth(2);
        degreePaint.setColor(outerColor);
        degreePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();

        int CircleCenterX = width / 2;
        float CircleCenterY = height - outerCircleRadius;

        float outerStartY = 0;
        float middleStartY = outerStartY + 5;

        float innerEffectStartY = middleStartY + middleRectRadius + 10;
        float innerEffectEndY = CircleCenterY - outerCircleRadius - 10;
        float innerRectHeight = innerEffectEndY - innerEffectStartY;
        float innerStartY = innerEffectStartY + (currentTemp - MIN_TEMP) / RANGE_TEMP * innerRectHeight;

        RectF outerRect=new RectF();
        outerRect.left = CircleCenterX - outerRectRadius;
        outerRect.top = outerStartY;
        outerRect.right=CircleCenterX + outerRectRadius;
        outerRect.bottom = CircleCenterY;
        canvas.drawRoundRect(outerRect, outerRectRadius, outerRectRadius, outerPaint);

        canvas.drawCircle(CircleCenterX, CircleCenterY, outerCircleRadius, outerPaint);

        RectF middleRect = new RectF();
        middleRect.left = CircleCenterX - middleRectRadius;
        middleRect.top = middleStartY;
        middleRect.right=CircleCenterX + middleRectRadius;
        middleRect.bottom = CircleCenterY;
        canvas.drawRoundRect(middleRect, middleRectRadius, middleRectRadius, middlePaint);
        canvas.drawCircle(CircleCenterX, CircleCenterY, middleCircleRadius, middlePaint);

        canvas.drawRect(CircleCenterX - innerRectRadius, innerStartY, CircleCenterX + innerRectRadius, CircleCenterY, innerPaint);
        canvas.drawCircle(CircleCenterX, CircleCenterY, innerCircleRadius, innerPaint);

        int i=50;
        float j=CircleCenterX - outerRectRadius - 3*DEGREE_WIDTH;
        Paint textPaint=new Paint();
        textPaint.setStrokeWidth(3);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(18);
        float tmp = innerEffectStartY;

        while (tmp <= innerEffectEndY) {
            canvas.drawLine(CircleCenterX - outerRectRadius - DEGREE_WIDTH, tmp, CircleCenterX - outerRectRadius, tmp, degreePaint);
            canvas.drawText(String.format(Locale.ENGLISH,"%d",i),j, tmp,textPaint);
            tmp += (innerEffectEndY - innerEffectStartY) / 10;
            i-=5;
        }


    }
}