package ssthouse.com.simplereader.main;

import android.content.Context;

import java.util.List;

import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;

/**
 * Created by ssthouse on 2016/9/29.
 */

public interface IMainModel {

    /**
     * 保存新书
     *
     * @param filePath 新书文件路径
     */
    void saveNewBook(String filePath);

    /**
     * 加载apk中自带书籍
     *
     * @param context
     */
    void saveApkBookBeans(Context context);

    /**
     * 加载app已保存所有书籍
     *
     * @return
     */
    public List<BookBean> getAllBookBeans();

    /**
     * 获取BookBean中的ArticleBeans
     * @param bookBean
     * @return
     */
    List<ArticleBean> getAllArticleBeans(BookBean bookBean);
}
