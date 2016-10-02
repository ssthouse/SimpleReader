package ssthouse.com.simplereader.main;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.UpdateBookListEvent;
import ssthouse.com.simplereader.bean.event.ChangeToArticleEvent;
import ssthouse.com.simplereader.bean.event.ChangeToBookListEvent;
import ssthouse.com.simplereader.bean.event.LoadRawBookBeanEvent;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainPresenter {

    //Model层
    private IMainModel mMainModel;

    //View层
    private IMainView mMainView;

    private Context mContext;

    public MainPresenter(IMainView mMainView, IMainModel mMainModel) {
        this.mMainView = mMainView;
        this.mMainModel = mMainModel;
        this.mContext = (Context) mMainView;
        EventBus.getDefault().register(this);
    }

    /**
     * 书籍列表刷新事件
     *
     * @param event
     */
    @Subscribe
    public void onBookListUpdate(UpdateBookListEvent event) {
        mMainView.showWaitDialog();
        List<BookBean> bookBeanList = mMainModel.getAllBookBeans();
        mMainView.loadBookBeans(bookBeanList);
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
        mMainView.loadArticleBeans(mMainModel.getAllArticleBeans(event.getBookBean()));
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
     * TODO:
     * 使用RxJava进行异步操作   从Model返回一个Observable
     * 加载apk中books
     */
    @Subscribe
    public void onLoadApkBookBeanEvent(LoadRawBookBeanEvent event) {
        mMainView.showWaitDialog();
        //获取raw中.txt文件
        mMainModel.saveApkBookBeans(mContext);
        //加载raw中WordBean
        mMainModel.loadApkWords(mContext);
    }
}
