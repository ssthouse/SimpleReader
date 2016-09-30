package ssthouse.com.simplereader;

import android.content.Context;
import android.content.Intent;

import ssthouse.com.simplereader.base.BaseActivity;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class ReadingActivity extends BaseActivity {



    public static void start(Context context) {
        Intent intent = new Intent(context, ReadingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_reading;
    }
}
