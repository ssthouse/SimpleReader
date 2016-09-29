package ssthouse.com.simplereader;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.OnClick;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.utils.PreferUtil;

public class MainActivity extends BaseActivity {

    @Bind(R.id.id_tb)
    Toolbar toolbar;

    @Bind(R.id.id_fab_add_book)
    FloatingActionButton fabAddBook;

    @Bind(R.id.id_lv_books)
    ListView lvBooks;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("SimpleReader");


        if (PreferUtil.getInstance().isFistIn(this)) {
            //模拟点击添加书籍按钮事件
            fabAddBook.performClick();
        }
    }

    @OnClick(R.id.id_fab_add_book)
    public void onAddBookClicked(){
        BookListActivity.start(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}
