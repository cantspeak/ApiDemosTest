package com.example.android.apis.text;

import android.content.Context;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by root on 13-10-18.
 */
public class LogTextBox extends TextView {
    public LogTextBox (Context context){
        this(context,null);

    }
    public LogTextBox(Context context,AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public LogTextBox(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs,defStyle);
    }
    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ScrollingMovementMethod.getInstance();
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }

}
