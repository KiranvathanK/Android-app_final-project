package shashi.com.driving_style.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextViewRoboto_Light extends AppCompatTextView {

    public TextViewRoboto_Light(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            createFont();
    }

    public TextViewRoboto_Light(Context context, AttributeSet attrs) {
            super(context, attrs);
            createFont();
    }

    public TextViewRoboto_Light(Context context) {
            super(context);
            createFont();
    }

    public void createFont() {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(font);
    }
}