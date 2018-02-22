package ru.spbau.lecturenotes.uiElements.PdfViewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ShowRectView extends View {
    public ShowRectView(Context context) {
        super(context);
    }

    public ShowRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShowRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Rect rect = new Rect();
    private Paint mRectPaint = new Paint();

    {
        mRectPaint.setColor(getContext().getResources().getColor(android.R.color.holo_green_light));
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(10); // TODO: should take from resources
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(rect, mRectPaint);
    }

    public void setRect(@NonNull Rect rect) {
        this.rect = rect;
        invalidate();
    }

    public Rect getRect() {
        return rect;
    }
}
