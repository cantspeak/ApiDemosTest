package com.example.android.apis.text;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.android.apis.R;

/**
 * Created by root on 13-10-18.
 */
public class LogTextBox1 extends Activity {
    private LogTextBox mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.log_text_box_1);

        mText = (LogTextBox) findViewById(R.id.text);

        Button addButton = (Button)findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText.append("This is a test\n");
            }
        });

        Button clear = (Button)findViewById(R.id.button2);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mText.setText("");
            }
        });
    }
}
