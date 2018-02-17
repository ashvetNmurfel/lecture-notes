package ru.spbau.lecturenotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class TouchableListView extends ListView {
    private boolean frozen;

    public TouchableListView(Context context) {
        super(context);
    }

    public TouchableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TouchableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!frozen) {
            super.onTouchEvent(ev);
        }
        return false;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
