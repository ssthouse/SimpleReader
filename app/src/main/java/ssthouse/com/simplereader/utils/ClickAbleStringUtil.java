package ssthouse.com.simplereader.utils;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by ssthouse on 2016/10/2.
 */

public class ClickAbleStringUtil {

    /*
 * ClickableString class
 */

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
