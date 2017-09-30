package com.tuankhai.travelassistants.module.floatingsearchview.utils.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by tuank on 03/09/2017.
 */

public abstract class TextWatcherAdapter implements TextWatcher {

    private static final String TAG = "TextWatcherAdapter";

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}