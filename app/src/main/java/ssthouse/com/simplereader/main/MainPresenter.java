package ssthouse.com.simplereader.main;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.AddNewBookEvent;
import ssthouse.com.simplereader.bean.event.BookBeanChangedEvent;
import ssthouse.com.simplereader.bean.event.ChangeToArticleEvent;
import ssthouse.com.simplereader.bean.event.ChangeToBookListEvent;
import ssthouse.com.simplereader.bean.event.LoadApkBookBeanEvent;
import ssthouse.com.simplereader.utils.ToastUtil;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainPresenter {

    private IMainModel mMainModel;

    private IMainView mMainView;

    private Context mContext;

    public MainPresenter(IMainView mMainView, IMainModel mMainModel) {
        this.mMainView = mMainView;
        this.mMainModel = mMainModel;
        this.mContext = (Context) mMainView;
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onAddNewBook(AddNewBookEvent event) {
        if (event == null || event.getFilePath() == null || event.getFilePath().length() == 0) {
            ToastUtil.toastSort(mContext, "文件路径无效");
            return;
        } else {
            mMainModel.saveNewBook(event.getFilePath());
        }
    }

    /**
     * 书籍列表刷新事件
     *
     * @param event
     */
    @Subscribe
    public void onBookListUpdate(BookBeanChangedEvent event) {
        mMainView.showWaitDialog();
        List<BookBean> bookBeanList = mMainModel.getAllBookBeans();
        mMainView.reloadBooks(bookBeanList);
        mMainView.dismissWaitDialog();
    }

    /**
     * 跳转文章列表event
     *
     * @param event
     */
    @Subscribe
    public void onChangeToArticleList(ChangeToArticleEvent event) {
        if (event == null || event.getBookBean() == null) {
            return;
        }
        mMainView.transFragment(IMainView.FRAGMENT_ARTICLE_LIST);
        //加载articles
        mMainView.reloadArticles(mMainModel.getAllArticleBeans(event.getBookBean()));
    }

    /**
     * 跳转书架event
     *
     * @param event
     */
    @Subscribe
    public void onChangeToBookList(ChangeToBookListEvent event) {
        mMainView.transFragment(IMainView.FRAGMENT_BOOK_LIST);
    }

    /**
     * TODO
     * 加载apk中books
     */
    @Subscribe
    public void onLoadApkBookBeanEvent(LoadApkBookBeanEvent event) {
        mMainView.showWaitDialog();
        //获取raw中.txt文件
        mMainModel.saveApkBookBeans(mContext);
    }
}
