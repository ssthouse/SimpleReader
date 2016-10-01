package ssthouse.com.simplereader.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.LoadApkBookBeanEvent;
import ssthouse.com.simplereader.utils.PreferUtil;
import ssthouse.com.simplereader.utils.ToastUtil;

public class MainActivity extends BaseActivity implements IMainView {

    //两次返回退出app
    private static long lastBackKeyPressedTIme = 0;

    @Bind(R.id.id_tb)
    Toolbar toolbar;

    private MainPresenter mPresenter;

    private FragmentManager mFragmentManager;
    private BookListFragment mBookListFragment;
    private ArticleListFragment mArticleListFragment;
    private int curFragment = FRAGMENT_BOOK_LIST;

    private AlertDialog mDialog;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("书架");

        //初始化fragmentManager
        mFragmentManager = getSupportFragmentManager();
        mBookListFragment = new BookListFragment();
        mArticleListFragment = new ArticleListFragment();
        transFragment(FRAGMENT_BOOK_LIST);

        //presenter层
        mPresenter = new MainPresenter(this, new MainModel());

        //第一次进入 加载apk中BookBean
        if (PreferUtil.getInstance().isFistIn(this)) {
            EventBus.getDefault().post(new LoadApkBookBeanEvent());
            PreferUtil.getInstance().setIsFistIn(this, false);
        }


    }

    public int getContentView() {
        return R.layout.activity_main;
    }

    /**
     * 切换fragment
     *
     * @param fragmentState
     */
    @Override
    public void transFragment(int fragmentState) {
        Fragment targetFragment = mBookListFragment;
        ActionBar actionBar = getSupportActionBar();
        switch (fragmentState) {
            case FRAGMENT_BOOK_LIST:
                curFragment = FRAGMENT_BOOK_LIST;
                targetFragment = mBookListFragment;
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setTitle("书库");
                }
                break;
            case FRAGMENT_ARTICLE_LIST:
                curFragment = FRAGMENT_ARTICLE_LIST;
                targetFragment = mArticleListFragment;
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setTitle("文章列表");
                }
                break;
        }
        if (targetFragment == null)
            return;
        mFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.id_fragment_holder, targetFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                transFragment(FRAGMENT_BOOK_LIST);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showWaitDialog() {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this)
                    .setView(R.layout.dialog_wait)
                    .create();
        }
        mDialog.show();
    }

    @Override
    public void dismissWaitDialog() {
        mDialog.dismiss();
    }

    @Override
    public void reloadBooks(List<BookBean> bookList) {
        mBookListFragment.loadBookList(bookList);
    }

    @Override
    public void reloadArticles(List<ArticleBean> articleBeanList) {
        mArticleListFragment.loadArticles(articleBeanList);
    }

    @Override
    public void onBackPressed() {
        //判断fragment
        if (curFragment == FRAGMENT_ARTICLE_LIST) {
            transFragment(FRAGMENT_BOOK_LIST);
            return;
        }
        if (System.currentTimeMillis() - lastBackKeyPressedTIme < 1000) {
            finish();
        } else {
            lastBackKeyPressedTIme = System.currentTimeMillis();
            ToastUtil.toastSort(this, "再次点击退出");
        }
    }
}
