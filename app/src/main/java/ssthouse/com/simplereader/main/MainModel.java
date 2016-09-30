package ssthouse.com.simplereader.main;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.event.BookListUpdateEvent;
import timber.log.Timber;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainModel implements IMainModel {

    @Override
    public List<BookBean> getBookList() {
        //TODO 数据库查询
        return null;
    }

    @Override
    public void saveNewBook(String filePath) {
        //TODO save new file to data base

    }

    @Override
    public void loadLocalBook(Context context) {
        InputStream is = null;
        BufferedReader br = null;
        BookBean bookBean = new BookBean();
        bookBean.bookName = "新概念4";
        bookBean.brief = "brief";
        bookBean.save();
        List<ArticleBean> articleBeanList = new ArrayList<>();
        try {
            //依次读取每一行  遇到Lesson换行
            is = context.getResources().openRawResource(R.raw.new_concept_english_v4);
            br = new BufferedReader(new InputStreamReader(is));
            String curLine = "";
            StringBuilder sb = new StringBuilder();
            String curArticleName = "";
            while (curLine != null) {
                if (curLine.contains("Lesson")) {
                    if (sb.length() != 0 && curArticleName.length() != 0) {
                        articleBeanList.add(new ArticleBean(curArticleName, sb.toString(), bookBean));
                    }
                    sb = new StringBuilder();
                    curArticleName = curLine;
                }
                curLine = br.readLine();
                sb.append(curLine);
            }
            //add last lesson
            articleBeanList.add(new ArticleBean(curArticleName, sb.toString(), bookBean));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Log out content
//        for (ArticleBean article: articleBeanList) {
//            Timber.e(article.content);
//        }
        Timber.e("所有文章的数目:\t" + articleBeanList.size());
//        bookBean.save();
        for (ArticleBean article: articleBeanList) {
            article.save();
        }

        //log out data base
        List<ArticleBean> testList = bookBean.getArticleBeanList();
        Timber.e("数据库中article数目为:"+testList.size());
        //保存解析出来的数据
//        for()
        //抛出解析完成event
        List<BookBean> bookBeanList = new ArrayList<>();
        bookBeanList.add(bookBean);
        EventBus.getDefault().post(new BookListUpdateEvent(bookBeanList));
    }
}
