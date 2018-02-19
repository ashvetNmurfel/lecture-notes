package ru.spbau.lecturenotes.storage;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;

public class Rectangle {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public @NonNull RectF getRect() {
        return new RectF(left, top, right, bottom);
    }

    public Rectangle() {
    }

    public Rectangle(@NonNull RectF rectF) {
        left = rectF.left;
        top = rectF.top;
        right = rectF.right;
        bottom = rectF.bottom;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
