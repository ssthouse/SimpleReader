package ssthouse.com.simplereader.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ssthouse on 2016/9/30.
 */
@Table(name = "Articles")
public class ArticleBean extends Model {

    @Column(name = "Name")
    public String articleName;

    @Column(name = "Content")
    public String content;

    @Column(name = "BookBean")
    public BookBean bookBean;

    public ArticleBean(){
        super();
    }

    public ArticleBean(String articleName, String content, BookBean bookBean) {
        super();
        this.articleName = articleName;
        this.content = content;
        this.bookBean = bookBean;
    }

    public String getArticleName() {
        return articleName;
    }
}
