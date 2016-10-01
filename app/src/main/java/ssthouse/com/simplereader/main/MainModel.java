package ssthouse.com.simplereader.main;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

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
import ssthouse.com.simplereader.bean.WordBean;
import ssthouse.com.simplereader.bean.event.BookBeanChangedEvent;
import timber.log.Timber;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainModel implements IMainModel {

    @Override
    public void saveNewBook(String filePath) {
        //TODO save new file to data base

    }

    @Override
    public void saveApkBookBeans(Context context) {
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
        for (ArticleBean article : articleBeanList) {
            article.save();
        }

        //log out data base
        List<ArticleBean> testList = bookBean.getArticleBeanList();
        Timber.e("数据库中article数目为:" + testList.size());
        //抛出解析完成event
        List<BookBean> bookBeanList = new ArrayList<>();
        bookBeanList.add(bookBean);
        //抛出BookBean数据更新event
        EventBus.getDefault().post(new BookBeanChangedEvent());
    }

    /**
     * TODO 作为public进行测试
     * 加载本地关键词
     */
    @Override
    public void loadApkWords(Context context) {
        InputStream is = null;
        BufferedReader br = null;
        ActiveAndroid.beginTransaction();
        try {
            is = context.getResources().openRawResource(R.raw.nce4_words);
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            ArrayList<WordBean> wordBeenList = new ArrayList<>();
            while (line != null) {
                line = br.readLine();
                //生成WordBean
                String[] wordAndLevel = line.split(" ");
                Timber.e(wordAndLevel[0] + "****" + wordAndLevel[1]);
                WordBean wordBean = new WordBean(wordAndLevel[0], Integer.parseInt(wordAndLevel[1]));
                wordBeenList.add(wordBean);
            }
            //add to database
            for (WordBean wordBean : wordBeenList) {
                wordBean.save();
                Timber.e(wordBean.name + "  " + wordBean.level);
            }
            ActiveAndroid.setTransactionSuccessful();
            Timber.e("transaction successful");
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e("what the fuck");
            Timber.e(e.getMessage());
        } finally {
            ActiveAndroid.endTransaction();
            Timber.e("transaction end");
            try {
                br.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //log out wordbean
        List<WordBean> tempList = new Select().from(WordBean.class)
                .orderBy("RANDOM()")
                .execute();
        Timber.e("总共单词数目为: " + tempList.size());
        for (WordBean wordBean : tempList) {
            Timber.e(wordBean.name + "   " + wordBean.level);
        }
    }

    /**
     * 加载数据库中所有BookBean
     *
     * @return
     */
    @Override
    public List<BookBean> getAllBookBeans() {
        return new Select()
                .from(BookBean.class)
                .orderBy("Name ASC")
                .execute();
    }

    @Override
    public List<ArticleBean> getAllArticleBeans(BookBean bookBean) {
        return bookBean.getArticleBeanList();
    }

}
