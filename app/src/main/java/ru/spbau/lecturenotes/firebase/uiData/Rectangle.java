package ru.spbau.lecturenotes.firebase.uiData;

import android.graphics.Point;

public class Rectangle {
    protected Point leftUpperCorner;
    protected Point rightLowerCorner;
    protected Point rightUpperCorner;
    protected Point leftLowerCorner;

    public Point getRightUpperCorner() {
        return rightUpperCorner;
    }

    public Point getLeftLowerCorner() {
        return leftLowerCorner;
    }

    public Point getLeftUpperCorner() {
        return leftUpperCorner;
    }

    public Point getRightLowerCorner() {
        return rightLowerCorner;
    }

}
