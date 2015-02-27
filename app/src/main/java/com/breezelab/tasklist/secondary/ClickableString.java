package com.breezelab.tasklist.secondary;

import android.text.style.ClickableSpan;
import android.view.View;

public class ClickableString extends ClickableSpan {
    private View.OnClickListener mListener;
    public ClickableString(View.OnClickListener listener) {
        mListener = listener;
    }
    @Override
    public void onClick(View v) {
        mListener.onClick(v);
    }
}