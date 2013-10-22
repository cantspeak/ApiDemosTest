package com.example.android.apis.text;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.widget.TextView;
import com.example.android.apis.R;

/**
 * Created by root on 13-10-18.
 */
public class Link extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        //Text2
        TextView t2 = (TextView) findViewById(R.id.text2);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        //Text3
        TextView t3 = (TextView)findViewById(R.id.text3);
        t3.setText(
          Html.fromHtml(
                  "<b>text3: Constructed from HTML programmatically.</b> Text with a " +
                  "<a href=\"http://www.google.com\">link</a> "+
                  "created in the Java source code using HTML."));
        t3.setMovementMethod(LinkMovementMethod.getInstance());

        // text4 illustrates constructing a styled string containing a
        // link without using HTML at all.  Again, for a fixed string
        // you should probably be using a string resource, not a
        // hardcoded value.

        SpannableString ss = new SpannableString(
            "text4: Manually created spans. Click here to dial the phone.");

        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 30,
                   Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new URLSpan("tel:4155551212"), 31+6, 31+10,
                   Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView t4 = (TextView) findViewById(R.id.text4);
        t4.setText(ss);
        t4.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
