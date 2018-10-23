package com.prembros.facilis.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.prembros.facilis.library.cardstack.cardstack.CardAnimator;
import com.prembros.facilis.library.cardstack.cardstack.CardStack;
import com.prembros.facilis.sample.R;


public class MyActivity extends Activity {
    private CardStack mCardStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mCardStack = findViewById(R.id.container);

        mCardStack.setContentResource(R.layout.card_content);
//        mCardStack.setStackMargin(20);

        CardsDataAdapter mCardAdapter = new CardsDataAdapter(getApplicationContext());
        mCardAdapter.add("test1");
        mCardAdapter.add("test2");
        mCardAdapter.add("test3");
        mCardAdapter.add("test4");
        mCardAdapter.add("test5");
        mCardAdapter.add("test6");
        mCardAdapter.add("test7");

        mCardStack.setAdapter(mCardAdapter);

        if (mCardStack.getAdapter() != null) {
            Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Reset
        switch (item.getItemId()) {
            case R.id.action_reset:
                mCardStack.reset(true);
                return true;
            // Bottom
            case R.id.action_bottom:
                mCardStack.setStackGravity(mCardStack.getStackGravity() == CardAnimator.TOP ? CardAnimator.BOTTOM : CardAnimator.TOP);
                mCardStack.reset(true);
                return true;
            // cycle
            case R.id.action_loop:
                mCardStack.setEnableLoop(!mCardStack.isEnableLoop());
                mCardStack.reset(true);
                return true;
            // Whether to allow rotation
            case R.id.action_rotation:
                mCardStack.setEnableRotation(!mCardStack.isEnableRotation());
                mCardStack.reset(true);
                return true;
            // Visible number
            case R.id.action_visibly_size:
                mCardStack.setVisibleCardNum(mCardStack.getVisibleCardNum() + 1);
                return true;
            // interval
            case R.id.action_span:
                mCardStack.setStackMargin(mCardStack.getStackMargin() + 10);
                return true;
            case R.id.action_settings:
                mCardStack.undo();
                return true;
        }
        return false;
    }
}
