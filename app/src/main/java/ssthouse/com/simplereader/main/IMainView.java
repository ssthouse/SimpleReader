package ssthouse.com.simplereader.main;

import java.util.List;

import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;

/**
 * Created by ssthouse on 2016/9/29.
 */

public interface IMainView {

    int FRAGMENT_BOOK_LIST = 1000;
    int FRAGMENT_ARTICLE_LIST = 1001;

    void loadBookBeans(List<BookBean> bookBeanList);

    void loadArticleBeans(List<ArticleBean> articleBeanList);

    void transFragment(int fragmentState);

    void showWaitDialog();

    void dismissWaitDialog();
}
