package ssthouse.com.simplereader.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class ToastUtil {

    public static void toastSort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
