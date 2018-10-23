package com.prembros.facilis.library.cardstack.cardstack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.prembros.facilis.R;

import java.util.ArrayList;

import static com.prembros.facilis.library.cardstack.cardstack.CardUtils.DIRECTION_UP;

public class CardStack extends RelativeLayout {

    private boolean mEnableRotation;
    private int mGravity;
    private int mColor = -1;
    private int mIndex = 0;
    private int mNumVisible = 4;
    private boolean canSwipe = true;
    private ArrayAdapter<?> mAdapter;
    private OnTouchListener mOnTouchListener;
    private CardAnimator mCardAnimator;
    private boolean mEnableLoop; // Whether to allow circular scrolling


    private CardEventListener mEventListener = new DefaultStackEventListener(300);
    private int mContentResource = 0;
    private int mMargin;

    public void discardTop(final int direction) {
        mCardAnimator.discard(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                mCardAnimator.initLayout();
                mIndex++;
                loadLast();

                viewCollection.get(0).setOnTouchListener(null);
                viewCollection.get(viewCollection.size() - 1).setOnTouchListener(mOnTouchListener);
                mEventListener.discarded(mIndex - 1, direction);
            }
        }, 0, 0);
    }

    /**
     * Set the direction to support up and down.
     * Reset the layout by calling {@link #reset(boolean)} after setting
     *
     * @param gravity {@link CardAnimator#TOP} Improvement {@link CardAnimator#BOTTOM} Down, default
     */
    public void setStackGravity(int gravity) {
        mGravity = gravity;
    }

    /**
     * Get current direction
     */
    public int getStackGravity() {
        return mGravity;
    }

    /**
     * Whether to allow rotation
     * <p/>
     * Reset the layout by calling {@link #reset(boolean)} after setting
     */
    public void setEnableRotation(boolean enableRotation) {
        mEnableRotation = enableRotation;
    }

    /**
     * Whether to cycle through
     * After setting, call {@link #reset(boolean)} to reinitialize the layout
     */
    public void setEnableLoop(boolean enableLoop) {
        mEnableLoop = enableLoop;
    }

    /**
     * Whether to allow rotation
     */
    public boolean isEnableRotation() {
        return mEnableRotation;
    }

    /**
     * Whether to cycle through
     */
    public boolean isEnableLoop() {
        return mEnableLoop;
    }

    public int getCurrIndex() {
        //sync?
        return mIndex;
    }

    //only necessary when I need the attrs from xml, this will be used when inflating layout
    public CardStack(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CardStack);

            mColor = array.getColor(R.styleable.CardStack_card_backgroundColor, mColor);
            mGravity = array.getInteger(R.styleable.CardStack_card_gravity, Gravity.BOTTOM);
            mEnableRotation = array.getBoolean(R.styleable.CardStack_card_enable_rotation, false);
            mNumVisible = array.getInteger(R.styleable.CardStack_card_stack_size, mNumVisible);
            mEnableLoop = array.getBoolean(R.styleable.CardStack_card_enable_loop, mEnableLoop);
            mMargin = array.getDimensionPixelOffset(R.styleable.CardStack_card_margin, 20);
            array.recycle();
        }

        //get attrs assign minVisibleNum
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews(false);
        }
        setupAnimation();
    }

    private void addContainerViews(boolean anim) {
        FrameLayout v = new FrameLayout(getContext());
        viewCollection.add(v);
        addView(v);
        if (anim) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.float_up);
            v.startAnimation(animation);
        }
    }

    public void setStackMargin(int margin) {
        mMargin = margin;
        mCardAnimator.setStackMargin(mMargin);
        mCardAnimator.initLayout();
    }

    public int getStackMargin() {
        return mMargin;
    }

    public void setContentResource(int res) {
        mContentResource = res;
    }

    public void setCanSwipe(boolean can) {
        this.canSwipe = can;
    }

    public void reset(boolean resetIndex) {
        reset(resetIndex, false);
    }

    private void reset(boolean resetIndex, boolean animFirst) {
        if (resetIndex) mIndex = 0;
        removeAllViews();
        viewCollection.clear();
        for (int i = 0; i < mNumVisible; i++) {
            addContainerViews(i == mNumVisible - 1 && animFirst);
        }
        setupAnimation();
        loadData();
    }

    public void setVisibleCardNum(int visiableNum) {
        mNumVisible = visiableNum;
        if (mNumVisible >= mAdapter.getCount()) {
            mNumVisible = mAdapter.getCount();
        }
        reset(false);
    }

    public void setThreshold(int t) {
        mEventListener = new DefaultStackEventListener(t);
    }

    public void setListener(CardEventListener cel) {
        mEventListener = cel;
    }

    private void setupAnimation() {
        final View cardView = viewCollection.get(viewCollection.size() - 1);
        mCardAnimator = new CardAnimator(viewCollection, mColor, mMargin);
        mCardAnimator.setGravity(mGravity);
        mCardAnimator.initLayout();

        final DragGestureDetector dd = new DragGestureDetector(CardStack.this.getContext(), new DragListener() {

            @Override
            public boolean onDragStart(MotionEvent e1, MotionEvent e2,
                                       float distanceX, float distanceY) {
                if (canSwipe) {
                    mCardAnimator.drag(e1, e2, distanceX, distanceY);
                }
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                final int direction = CardUtils.direction(y1, y2);
                float distance = CardUtils.distance(x1, y1, x2, y2);
                mEventListener.swipeStart(direction, distance);
                return true;
            }

            @Override
            public boolean onDragContinue(MotionEvent e1, MotionEvent e2,
                                          float distanceX, float distanceY) {
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                final int direction = CardUtils.direction(y1, y2);
                if (canSwipe) {
                    mCardAnimator.drag(e1, e2, distanceX, distanceY);
                }
                mEventListener.swipeContinue(direction, Math.abs(x2 - x1), Math.abs(y2 - y1));
                return true;
            }

            @Override
            public boolean onDragEnd(MotionEvent e1, MotionEvent e2) {
                //reverse(e1,e2);
                float x1 = e1.getRawX();
                float y1 = e1.getRawY();
                float x2 = e2.getRawX();
                float y2 = e2.getRawY();
                float distance = CardUtils.distance(x1, y1, x2, y2);
                final int direction = CardUtils.direction(y1, y2);

                if (direction == DIRECTION_UP) {
                    if (canSwipe) {
                        mCardAnimator.reverse(e1, e2);
                    }
                    return true;
                }
                boolean discard = mEventListener.swipeEnd(direction, distance);
                if (discard) {
                    if (canSwipe) {
                        mCardAnimator.discard(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationEnd(Animator arg0) {
                                mCardAnimator.initLayout();
                                mIndex++;
                                mEventListener.discarded(mIndex, direction);

                                //mIndex = mIndex%mAdapter.getCount();
                                loadLast();

                                viewCollection.get(0).setOnTouchListener(null);
                                viewCollection.get(viewCollection.size() - 1)
                                        .setOnTouchListener(mOnTouchListener);
                            }

                        }, y1, y2);
                    }
                } else {
                    if (canSwipe) {
                        mCardAnimator.reverse(e1, e2);
                    }
                }
                return true;
            }

            @Override
            public boolean onTapUp() {
                mEventListener.topCardTapped();
                return true;
            }
        }
        );

        mOnTouchListener = new OnTouchListener() {
            private static final String DEBUG_TAG = "MotionEvents";

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                dd.onTouchEvent(event);
                return true;
            }
        };
        cardView.setOnTouchListener(mOnTouchListener);
    }

    private DataSetObserver mOb = new DataSetObserver() {
        @Override
        public void onChanged() {
            reset(false);
        }
    };

    //ArrayList
    ArrayList<View> viewCollection = new ArrayList<View>();

    public CardStack(Context context) {
        super(context);
    }

    public void setAdapter(final ArrayAdapter<?> adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mOb);
        }
        mAdapter = adapter;
        adapter.registerDataSetObserver(mOb);

        loadData();
    }

    public ArrayAdapter getAdapter() {
        return mAdapter;
    }

    public View getTopView() {
        return ((ViewGroup) viewCollection.get(viewCollection.size() - 1)).getChildAt(0);
    }

    private void loadData() {
        for (int i = mNumVisible - 1; i >= 0; i--) {
            ViewGroup parent = (ViewGroup) viewCollection.get(i);
            int index = (mIndex + mNumVisible - 1) - i;
            if (index > mAdapter.getCount() - 1) {
                parent.setVisibility(View.GONE);

            } else {
                View child = mAdapter.getView(index, getContentView(), this);
                parent.addView(child);
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    private View getContentView() {
        View contentView = null;
        if (mContentResource != 0) {
            LayoutInflater lf = LayoutInflater.from(getContext());
            contentView = lf.inflate(mContentResource, null);
        }
        return contentView;

    }

    // Load next
    private void loadLast() {
        ViewGroup parent = (ViewGroup) viewCollection.get(0);
        int lastIndex = ((mNumVisible - 1) + mIndex);

        // Exceed index
        if (lastIndex > mAdapter.getCount() - 1) {
            if (mEnableLoop) {
                // Loop processing
                lastIndex = lastIndex % mAdapter.getCount();
            } else {
                parent.setVisibility(View.GONE);
                return;
            }
        }

        View child = mAdapter.getView(lastIndex, getContentView(), parent);
        parent.removeAllViews();
        parent.addView(child);
    }

    /**
     * Get the number of visible cards
     */
    public int getVisibleCardNum() {
        return mNumVisible;
    }

    public void undo() {
        if (mIndex == 0) return;
        mIndex--;
        reset(false, true);
    }
}
