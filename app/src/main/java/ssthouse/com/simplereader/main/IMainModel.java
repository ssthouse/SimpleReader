package ssthouse.com.simplereader.main;

import android.content.Context;

import java.util.List;

import ssthouse.com.simplereader.bean.BookBean;

/**
 * Created by ssthouse on 2016/9/29.
 */

public interface IMainModel {

    /**
     * 获取书籍信息列表
     * @return
     */
    List<BookBean> getBookList();

    void saveNewBook(String filePath);

    void loadLocalBook(Context context);
}
