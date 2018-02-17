package ru.spbau.lecturenotes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DragRectView extends View {
    public Matrix matrix = new Matrix();

    private Paint mRectPaint;

    private int mStartX = 0;
    private int mStartY = 0;
    private int mEndX = 0;
    private int mEndY = 0;
    private boolean mDrawRect = false;
    private TextPaint mTextPaint = null;

    private OnUpCallback mCallback = null;
    private Rect lastRect = new Rect(0,0,0,0);

    public interface OnUpCallback {
        void onRectFinished(Rect rect);
    }

    public DragRectView(final Context context) {
        super(context);
        init();
    }

    public DragRectView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragRectView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Sets callback for up
     *
     * @param callback {@link OnUpCallback}
     */
    public void setOnUpCallback(OnUpCallback callback) {
        mCallback = callback;
    }


    public Rect getRect() {
        return lastRect;
    }


    private OnClickListener onClickCallback;

    public void setOnClickCallback(OnClickListener onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    /**
     * Inits internal data
     */
    private void init() {
        mRectPaint = new Paint();
        mRectPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(5); // TODO: should take from resources

        mTextPaint = new TextPaint();
        mTextPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mTextPaint.setTextSize(20);
    }

    private boolean isMoving = false;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        // TODO: be aware of multi-touches
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mDrawRect = false;
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                invalidate();
                isMoving = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final int x = (int) event.getX();
                final int y = (int) event.getY();

                if (!mDrawRect || Math.abs(x - mEndX) > 5 || Math.abs(y - mEndY) > 5) {
                    mEndX = x;
                    mEndY = y;
                    invalidate();
                }

                mDrawRect = true;
                isMoving = true;
                break;

            case MotionEvent.ACTION_UP:
                lastRect = new Rect(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                        Math.max(mEndX, mStartX), Math.max(mEndY, mStartX));

                if (mCallback != null) {
                    mCallback.onRectFinished(lastRect);
                }
                invalidate();

                // if rect is small, then it was just a tap
                final int x2 = (int) event.getX();
                final int y2 = (int) event.getY();
                if (onClickCallback != null && (!isMoving || Math.abs(x2 - mStartX) < 10 && Math.abs(y2 - mStartY) < 10)) {
                    onClickCallback.onClick(this);
                }
                isMoving = false;
                break;

            default:
                break;
        }

        return true;
    }

    private RectF drawRect = new RectF(0,0,0,0);
    private RectF transformedRect = new RectF(0,0,0,0);


    public Rect publicRect = new Rect(0,0,100,100);

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (mDrawRect) {
            drawRect.set(Math.min(mStartX, mEndX), Math.min(mStartY, mEndY),
                    Math.max(mEndX, mStartX), Math.max(mEndY, mStartY));
            canvas.drawRect(drawRect, mRectPaint);
//            canvas.drawRect(publicRect, mRectPaint);
            canvas.drawText("  (" + Math.abs(mStartX - mEndX) + ", " + Math.abs(mStartY - mEndY) + ")",
                    Math.max(mEndX, mStartX), Math.max(mEndY, mStartY), mTextPaint);

            transformedRect.set(drawRect);


            Log.i("helloVinni", matrix.toShortString());

//            matrix.setScale(2, 2);
            matrix.mapRect(transformedRect);

            Log.i("hello", transformedRect.toShortString());

//            transformedRect.left += 100;
//            transformedRect.right += 100;
            canvas.drawRect(transformedRect, mRectPaint);
        }
    }
}