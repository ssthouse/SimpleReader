package ssthouse.com.simplereader;

import android.content.Context;
import android.content.Intent;

import ssthouse.com.simplereader.base.BaseActivity;

public class MainActivity extends BaseActivity {


    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}
