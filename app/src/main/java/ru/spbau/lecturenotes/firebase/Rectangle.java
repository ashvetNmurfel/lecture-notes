package ru.spbau.lecturenotes.firebase;

import android.graphics.Point;

class Rectangle {
    protected Point leftUpperCorner;
    protected Point rightLowerCorner;

    public Point getLeftUpperCorner() {
        return leftUpperCorner;
    }

    public Point getRightLowerCorner() {
        return rightLowerCorner;
    }
}
