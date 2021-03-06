package ssthouse.com.simplereader.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * 保存图书基本信息
 * Created by ssthouse on 2016/9/29.
 */
@Table(name = "Books")
public class BookBean extends Model {

    /**
     * 书名
     */
    @Column(name = "Name")
    public String bookName;

    public BookBean() {
        super();
    }

    public BookBean(String bookName) {
        super();
        this.bookName = bookName;
    }

    /**
     * 获取文章列表
     *
     * @return
     */
    public List<ArticleBean> getArticleBeanList() {
        return getMany(ArticleBean.class, "BookBean");
    }
}
