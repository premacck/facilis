package com.prembros.facilis.library.cardstack.cardstack;

import android.view.MotionEvent;

public interface DragListener {

    boolean onDragStart(MotionEvent e1, MotionEvent e2, float distanceX,
                        float distanceY);

    boolean onDragContinue(MotionEvent e1, MotionEvent e2, float distanceX,
                           float distanceY);

    boolean onDragEnd(MotionEvent e1, MotionEvent e2);

    boolean onTapUp();
}
