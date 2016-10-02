package ssthouse.com.simplereader.bean.event;

import ssthouse.com.simplereader.bean.BookBean;

/**
 * 切换至文章列表event
 * Created by ssthouse on 2016/9/30.
 */

public class ChangeToArticleEvent {

    private BookBean bookBean;

    public ChangeToArticleEvent(BookBean bookBean) {
        this.bookBean = bookBean;
    }

    public BookBean getBookBean() {
        return bookBean;
    }

    public void setBookBean(BookBean bookBean) {
        this.bookBean = bookBean;
    }
}
