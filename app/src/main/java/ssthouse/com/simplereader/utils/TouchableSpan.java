package ssthouse.com.simplereader.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by ssthouse on 2016/10/2.
 */

public abstract class TouchableSpan extends ClickableSpan {
    public boolean mIsPressed;
    private int mPressedBackgroundColor;
    private int mNormalTextColor;
    private int mPressedTextColor;

    public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //super.updateDrawState(ds);
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = (mIsPressed ? mPressedBackgroundColor : 0xFFFFFF);
        ds.setUnderlineText(false);
    }
}
