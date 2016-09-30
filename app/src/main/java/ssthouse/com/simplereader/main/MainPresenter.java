package ssthouse.com.simplereader.main;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.BookListUpdateEvent;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainPresenter {

    private IMainModel mMainModel;

    private IMainView mMainView;

    public MainPresenter(IMainView mMainView, IMainModel mMainModel) {
        this.mMainView = mMainView;
        this.mMainModel = mMainModel;
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    public void reloadBookList(){
        //TODO  测试添加数据
        List<BookBean> bookList = new ArrayList<>();
        bookList.add(new BookBean("bookName1", "I am book one."));
        //List<BookBean> bookList = mMainModel.getBookList();
        mMainView.reloadBooks(bookList);

    }

    /**
     * 书籍列表刷新事件
     * @param event
     */
    @Subscribe
    public void onBookListUpdate(BookListUpdateEvent event) {
        mMainView.reloadBooks(event.getBookList());
    }
}
