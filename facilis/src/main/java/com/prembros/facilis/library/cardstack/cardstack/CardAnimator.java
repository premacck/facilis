package com.prembros.facilis.library.cardstack.cardstack;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.prembros.facilis.library.cardstack.animation.RelativeLayoutParamsEvaluator;

import java.util.ArrayList;
import java.util.HashMap;

import static com.prembros.facilis.library.cardstack.cardstack.CardUtils.cloneParams;
import static com.prembros.facilis.library.cardstack.cardstack.CardUtils.getMoveParams;
import static com.prembros.facilis.library.cardstack.cardstack.CardUtils.move;
import static com.prembros.facilis.library.cardstack.cardstack.CardUtils.scale;

public class CardAnimator {

    public static final int TOP = 1;
    public static final int BOTTOM = 2;

    private static final int REMOTE_DISTANCE = 1000;
    private int mBackgroundColor;
    public ArrayList<View> mCardCollection;
    private HashMap<View, LayoutParams> mLayoutsMap;
    private LayoutParams mRemoteLayoutParam;
    private LayoutParams baseLayout;
    private int mStackMargin;
    private int mGravity = BOTTOM;

    CardAnimator(ArrayList<View> viewCollection, int backgroundColor, int margin) {
        mCardCollection = viewCollection;
        mBackgroundColor = backgroundColor;
        mStackMargin = margin;
        setup();
    }

    private void setup() {
        mLayoutsMap = new HashMap<>();

        for (View v : mCardCollection) {
            //setup basic layout
            LayoutParams params = (LayoutParams) v.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;

            if (mBackgroundColor != -1) {
                v.setBackgroundColor(mBackgroundColor);
            }

            v.setLayoutParams(params);
        }

        baseLayout = (LayoutParams) mCardCollection.get(0).getLayoutParams();
        baseLayout = cloneParams(baseLayout);

    }

    void initLayout() {
        int size = mCardCollection.size();
        for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (index != 0) {
                index -= 1;
            }
            LayoutParams params = cloneParams(baseLayout);
            v.setLayoutParams(params);

            scale(v, -(size - index - 1) * 5, mGravity);

            int margin = index * mStackMargin;
            move(v, mGravity == TOP ? -margin : margin, 0);
            v.setRotation(0);

            LayoutParams paramsCopy =
                    cloneParams((LayoutParams) v.getLayoutParams());
            mLayoutsMap.put(v, paramsCopy);
        }

        setupRemotes();
    }

    /**
     * Set the direction to support up and down.
     * Reset the layout by calling {@link #initLayout()} after setting
     *
     * @param gravity {@link #TOP} Up {@link #BOTTOM} Down, default
     */
    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    private void setupRemotes() {
        View topView = getTopView();
        mRemoteLayoutParam = getMoveParams(topView, -REMOTE_DISTANCE, 0);
    }

    private View getTopView() {
        return mCardCollection.get(mCardCollection.size() - 1);
    }

    private void moveToBack(View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0); // Move to the last one
        }
    }

    // Sort the cards, pull one out, one on the bottom
    private void reorder() {

        View temp = getTopView();
        //RelativeLayout.LayoutParams tempLp = mLayoutsMap.get(mCardCollection.get(0));
        //mLayoutsMap.put(temp,tempLp);
        moveToBack(temp);

        for (int i = (mCardCollection.size() - 1); i > 0; i--) {
            //View next = mCardCollection.get(i);
            //RelativeLayout.LayoutParams lp = mLayoutsMap.get(next);
            //mLayoutsMap.remove(next);
            View current = mCardCollection.get(i - 1);

            //current replace next
            mCardCollection.set(i, current);
            //mLayoutsMap.put(current,lp);

        }

        mCardCollection.set(0, temp);
    }

    // Destroy card
    void discard(final AnimatorListener al, float y1, float y2) {
        AnimatorSet as = new AnimatorSet();
        ArrayList<Animator> aCollection = new ArrayList<Animator>();

        final View topView = getTopView();
        LayoutParams topParams = (LayoutParams) topView.getLayoutParams();
        LayoutParams layout = cloneParams(topParams);
        ValueAnimator discardAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), layout, mRemoteLayoutParam);

        discardAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                topView.setLayoutParams((LayoutParams) value.getAnimatedValue());
            }
        });

        if (y1 == 0 || y2 == 0 || y1 == y2) {
            discardAnim.setDuration(200);
        } else {
            discardAnim.setDuration((int) Math.abs(500 - Math.abs(y2 - y1)));
        }
        discardAnim.setInterpolator(new FastOutLinearInInterpolator());
        aCollection.add(discardAnim);

        for (int i = 0; i < mCardCollection.size(); i++) {
            final View v = mCardCollection.get(i);

            if (v == topView) continue;
            final View nv = mCardCollection.get(i + 1);
            LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
            LayoutParams endLayout = cloneParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(nv));
            layoutAnim.setDuration(250);
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            aCollection.add(layoutAnim);
        }

        as.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                reorder();
                if (al != null) {
                    al.onAnimationEnd(animation);
                }
                mLayoutsMap = new HashMap<View, LayoutParams>();
                for (View v : mCardCollection) {
                    LayoutParams params = (LayoutParams) v.getLayoutParams();
                    LayoutParams paramsCopy = cloneParams(params);
                    mLayoutsMap.put(v, paramsCopy);
                }

            }

        });

        as.playTogether(aCollection);
        as.start();
    }

    /**
     * Restore back cards location
     */
    void reverse(MotionEvent e1, MotionEvent e2) {
        for (final View v : mCardCollection) {
            LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
            LayoutParams endLayout = cloneParams(layoutParams);
            ValueAnimator layoutAnim = ValueAnimator.ofObject(new RelativeLayoutParamsEvaluator(), endLayout, mLayoutsMap.get(v));
            layoutAnim.setDuration(200);
            layoutAnim.setInterpolator(new OvershootInterpolator(2f));
            layoutAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    v.setLayoutParams((LayoutParams) value.getAnimatedValue());
                }
            });
            layoutAnim.start();
        }

    }

    void drag(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        View topView = getTopView();
        int y_diff = (int) ((e2.getRawY() - e1.getRawY()));
        LayoutParams layoutParams = (LayoutParams) topView.getLayoutParams();
        LayoutParams topViewLayouts = mLayoutsMap.get(topView);
        layoutParams.topMargin = topViewLayouts.topMargin + y_diff;
        layoutParams.bottomMargin = topViewLayouts.bottomMargin - y_diff;

        //animate secondary views.
        for (View v : mCardCollection) {
            int index = mCardCollection.indexOf(v);
            if (v != getTopView() && index != 0) {
                LayoutParams l = CardUtils.scaleFrom(v, mLayoutsMap.get(v), (int) (Math.abs(y_diff) * 0.02), mGravity);
                CardUtils.moveFrom(v, l, 0, (int) (Math.abs(y_diff) * index * 0.05), mGravity);
            }
        }
    }

    void setStackMargin(int margin) {
        mStackMargin = margin;
    }
}
