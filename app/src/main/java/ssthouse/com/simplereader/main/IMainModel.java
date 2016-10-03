package ssthouse.com.simplereader.main;

import android.content.Context;

import java.util.List;

import rx.Observable;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;

/**
 * Created by ssthouse on 2016/9/29.
 */

public interface IMainModel {

    /**
     * 加载raw中文件
     *
     * @param context
     * @return
     */
    Observable loadRawFiles(Context context);

    /**
     * 加载app已保存所有书籍
     *
     * @return
     */
    List<BookBean> getAllBookBeans();

    /**
     * 获取BookBean中的ArticleBeans
     *
     * @param bookBean
     * @return
     */
    List<ArticleBean> getAllArticleBeans(BookBean bookBean);
}
