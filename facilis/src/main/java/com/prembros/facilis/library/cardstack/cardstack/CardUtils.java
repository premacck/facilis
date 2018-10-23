package com.prembros.facilis.library.cardstack.cardstack;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

public class CardUtils {
    final static int DIRECTION_BOTTOM = 0;
    final static int DIRECTION_UP = 1;

    static void scale(View v, int pixel, int gravity) {
        LayoutParams params = (LayoutParams) v.getLayoutParams();
        params.leftMargin -= pixel * 3;
        params.rightMargin -= pixel * 3;
        if (gravity == CardAnimator.TOP) {
            params.topMargin += pixel;
        } else {
            params.topMargin -= pixel;
        }
        params.bottomMargin -= pixel;
        v.setLayoutParams(params);
    }

    static LayoutParams getMoveParams(View v, int upDown, int leftRight) {
        LayoutParams original = (LayoutParams) v.getLayoutParams();
        LayoutParams params = cloneParams(original);

        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;
        params.topMargin -= upDown;
        params.bottomMargin += upDown;
        return params;
    }

    static void move(View v, int upDown, int leftRight) {
        LayoutParams params = getMoveParams(v, upDown, leftRight);
        v.setLayoutParams(params);
    }

    static LayoutParams scaleFrom(View v, LayoutParams params, int pixel, int gravity) {
        Log.d("pixel", "onScroll: " + pixel);
        params = cloneParams(params);
        params.leftMargin -= pixel;
        params.rightMargin -= pixel;
        if (gravity == CardAnimator.TOP) {
            params.topMargin += pixel;
        } else {
            params.topMargin -= pixel;
        }
        params.bottomMargin -= pixel;
        v.setLayoutParams(params);

        return params;
    }

    static LayoutParams moveFrom(View v, LayoutParams params, int leftRight, int upDown, int gravity) {
        params = cloneParams(params);
        params.leftMargin += leftRight;
        params.rightMargin -= leftRight;

        if (gravity == CardAnimator.BOTTOM) {
            params.bottomMargin += upDown;
            params.topMargin -= upDown;
        } else {
            params.bottomMargin -= upDown;
            params.topMargin += upDown;
        }
        v.setLayoutParams(params);

        return params;
    }

    //a copy method for RelativeLayout.LayoutParams for backward compartibility
    public static LayoutParams cloneParams(LayoutParams params) {
        LayoutParams copy = new LayoutParams(params.width, params.height);
        copy.leftMargin = params.leftMargin;
        copy.topMargin = params.topMargin;
        copy.rightMargin = params.rightMargin;
        copy.bottomMargin = params.bottomMargin;
        int[] rules = params.getRules();
        for (int i = 0; i < rules.length; i++) {
            copy.addRule(i, rules[i]);
        }
        //copy.setMarginStart(params.getMarginStart());
        //copy.setMarginEnd(params.getMarginEnd());

        return copy;
    }

    static float distance(float x1, float y1, float x2, float y2) {

        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    static int direction(float y1, float y2) {
        if (y2 > y1) {
            return DIRECTION_BOTTOM;
        } else {
            return DIRECTION_UP;
        }
    }
}