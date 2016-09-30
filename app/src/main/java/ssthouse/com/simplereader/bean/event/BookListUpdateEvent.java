package ssthouse.com.simplereader.bean.event;

import java.util.List;

import ssthouse.com.simplereader.bean.BookBean;

/**
 * Created by ssthouse on 2016/9/30.
 */

public class BookListUpdateEvent {

    private List<BookBean> bookList;

    public BookListUpdateEvent(List<BookBean> bookList) {
        this.bookList = bookList;
    }

    public List<BookBean> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookBean> bookList) {
        this.bookList = bookList;
    }
}
