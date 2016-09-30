package ssthouse.com.simplereader.bean;

/**
 * 保存图书基本信息
 * Created by ssthouse on 2016/9/29.
 */

public class BookBean {

    /**
     * 书名
     */
    private String bookName;

    /**
     * 简介
     */
    private String brief;

    public BookBean(String bookName, String brief) {
        this.bookName = bookName;
        this.brief = brief;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}
