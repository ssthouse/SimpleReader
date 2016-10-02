package ssthouse.com.simplereader.reading;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by ssthouse on 2016/10/2.
 */

public abstract class TouchableSpan extends ClickableSpan {

    //是否被选中
    private boolean mIsSelected;

    //颜色参数
    private int mPressedBgColor;
    private int mNormalTextColor;
    private int mPressedTextColor;

    public TouchableSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mPressedBgColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isSelected) {
        mIsSelected = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mIsSelected ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = (mIsSelected ? mPressedBgColor : 0xFFFFFF);
        ds.setUnderlineText(false);
    }
}
