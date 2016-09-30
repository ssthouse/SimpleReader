package ssthouse.com.simplereader;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.OnClick;
import ssthouse.com.simplereader.base.BaseActivity;

/**
 * 展示,添加书籍Activity
 * Created by ssthouse on 2016/9/29.
 */

public class AddBookActivity extends BaseActivity {

    @Bind(R.id.id_tb)
    Toolbar toolbar;

    @Bind(R.id.id_fab_find_book)
    FloatingActionButton fabFindBook;

    public static void start(Context context) {
        Intent intent = new Intent(context, AddBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("添加书籍");


    }

    @OnClick(R.id.id_fab_find_book)
    public void onFabFindBookClicked() {
        //TODO 打开文件管理器  获取文件路径
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_book;
    }
}
