package ssthouse.com.simplereader.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by ssthouse on 2016/9/28.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        init();
    }

    /**
     * onCreate中初始话方法
     */
    protected abstract void init();

    /**
     * 设置布局文件
     *
     * @return
     */
    public abstract int getContentView();
}
