package com.prembros.facilis.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prembros.facilis.sample.R;

public class CardsDataAdapter extends ArrayAdapter<String> {

    CardsDataAdapter(Context context) {
        super(context, R.layout.card_content);
    }

    @NonNull
    @Override
    public View getView(int position, final View contentView, @NonNull ViewGroup parent) {
        TextView v = contentView.findViewById(R.id.content);
        v.setText(getItem(position));
        return contentView;
    }

}

