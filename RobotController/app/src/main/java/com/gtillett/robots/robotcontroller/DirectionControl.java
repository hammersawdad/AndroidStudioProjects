package com.gtillett.robots.robotcontroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class DirectionControl extends View {
    private static final int CONTROL_TOP = 100;
    private static final int CONTROL_LEFT = 1000;
    private static final int CONTROL_WIDTH = 500;
    private static final int CONTROL_HEIGHT = 500;

    OnTouchEventListener mListener;

    public interface OnTouchEventListener {
        void onTouch(int xPos, int yPos);
    }

    public void setTouchEventListener(OnTouchEventListener eventListener) {
        mListener = eventListener;
    }

    public DirectionControl(Context context) {
        super(context);
    }

    public DirectionControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DirectionControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawDirectionTouchpad(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

            if(mListener!=null) {
                float xPos = event.getX();
                float yPos = event.getY();

                // Only raise the OnTouch event if the touch is inside the control boundries
                if (xPos >= CONTROL_LEFT && xPos <= CONTROL_LEFT + CONTROL_WIDTH) {
                    if (yPos >= CONTROL_TOP && yPos <= CONTROL_TOP + CONTROL_HEIGHT){

                        int xRelativePos = convertToPercentage(xPos, CONTROL_LEFT, CONTROL_LEFT + CONTROL_WIDTH);
                        int yRelativePos = convertToPercentage(yPos, CONTROL_TOP, CONTROL_TOP + CONTROL_HEIGHT);
                        yRelativePos = flipPolariy(yRelativePos); // Adjust so Zero is at the bottom, instead of the top

                        mListener.onTouch(xRelativePos, yRelativePos);
                    }
                }
            }

        super.onTouchEvent(event);
        return true;
    }

    private void drawDirectionTouchpad(Canvas canvas) {
        int squareTop = CONTROL_TOP;
        int squareLeft = CONTROL_LEFT;
        int squareWidth = CONTROL_WIDTH;
        int squareHeight = CONTROL_HEIGHT;
        int borderWidth = 3;

        drawBorderBox(canvas, squareTop, squareLeft, squareWidth, squareHeight, borderWidth);
        drawTopBox(canvas, squareTop, squareLeft, squareWidth, squareHeight, borderWidth);
        drawBottomBox(canvas, squareTop, squareLeft, squareWidth, squareHeight, borderWidth);
    }

    private void drawBorderBox(Canvas canvas, int squareTop, int squareLeft, int squareWidth, int squareHeight, int borderWidth) {
        int top = squareTop;
        int left = squareLeft;
        int right = left + squareWidth;
        int bottom = top + squareHeight;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawTopBox(Canvas canvas, int squareTop, int squareLeft, int squareWidth, int squareHeight, int borderWidth) {
        int halfHeight = squareHeight / 2;

        int top = squareTop - borderWidth;
        int left = squareLeft - borderWidth;
        int right = squareLeft + squareWidth - borderWidth;
        int bottom = squareTop + halfHeight - borderWidth;

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(0);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private void drawBottomBox(Canvas canvas, int squareTop, int squareLeft, int squareWidth, int squareHeight, int borderWidth) {
        int halfHeight = squareHeight / 2;

        int top = squareTop + halfHeight - borderWidth + 1;
        int left = squareLeft - borderWidth;
        int right = squareLeft + squareWidth - borderWidth;
        int bottom = squareTop + squareHeight - borderWidth;

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(0);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private int convertToPercentage(float value, int mininum, int maximum){
        float intValue = (int) Math.abs(value);

        // Adjust the numbers to start at zero
        intValue = intValue - mininum;
        int max = maximum - mininum;

        float percentage = (intValue / max) * 100;

        return (int) Math.abs(percentage);
    }

    private int flipPolariy(int percentageValue){
        return 100 - percentageValue;
    }

}
