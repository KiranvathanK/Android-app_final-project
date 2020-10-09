package shashi.com.driving_style.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditTextRoboto_Light extends AppCompatEditText {

    public EditTextRoboto_Light(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            createFont();
    }

    public EditTextRoboto_Light(Context context, AttributeSet attrs) {
            super(context, attrs);
            createFont();
    }

    public EditTextRoboto_Light(Context context) {
            super(context);
            createFont();
    }

    public void createFont() {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(font);
    }
}