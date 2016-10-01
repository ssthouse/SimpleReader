package ssthouse.com.simplereader.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.Bind;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.utils.PreferUtil;

public class MainActivity extends BaseActivity implements IMainView {

    @Bind(R.id.id_tb)
    Toolbar toolbar;

    private MainPresenter mPresenter;

    private int curFragment = FRAGMENT_BOOK_LIST;

    private FragmentManager mFragmentManager;
    private BookListFragment mBookListFragment;
    private ArticleListFragment mArticleListFragment;

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

        //初始话presenter
        mPresenter = new MainPresenter(this, new MainModel());

        //TODO  第一次进入逻辑有待商榷
        if (PreferUtil.getInstance().isFistIn(this)) {
            //模拟点击添加书籍按钮事件
            PreferUtil.getInstance().setIsFistIn(this, false);
        }
    }

    @Override
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
        switch (fragmentState) {
            case FRAGMENT_BOOK_LIST:
                targetFragment = mBookListFragment;
                break;
            case FRAGMENT_ARTICLE_LIST:
                targetFragment = mArticleListFragment;
                break;
        }
        if (targetFragment == null)
            return;
        mFragmentManager.beginTransaction()
                .replace(R.id.id_fragment_holder, targetFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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

}
