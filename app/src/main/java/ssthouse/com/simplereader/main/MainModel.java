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
import java.util.Scanner;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.BookBean;
import ssthouse.com.simplereader.bean.WordBean;
import ssthouse.com.simplereader.bean.event.UpdateBookListEvent;

/**
 * Created by ssthouse on 2016/9/29.
 */

public class MainModel implements IMainModel {

    private static final String DIVIDER_KEY_WORD = "Lesson";

    /**
     * 加载raw中书籍
     *
     * @param context
     */
    public void loadRawBookBeans(Context context) {
        InputStream is = null;
        BufferedReader br = null;
        BookBean bookBean = new BookBean(context.getString(R.string.str_default_book_name));
        bookBean.save();
        List<ArticleBean> articleBeanList = new ArrayList<>();
        try {
            is = context.getResources().openRawResource(R.raw.new_concept_english_v4);
            br = new BufferedReader(new InputStreamReader(is));
            String curLine = "";
            StringBuilder sb = new StringBuilder();
            String curArticleName = "";
            while (curLine != null) {
                if (curLine.contains(DIVIDER_KEY_WORD)) {
                    if (sb.length() != 0 && curArticleName.length() != 0) {
                        articleBeanList.add(new ArticleBean(curArticleName, sb.toString(), bookBean));
                    }
                    sb = new StringBuilder();
                    curArticleName = curLine.substring(curLine.indexOf('L'), curLine.length());
                } else {
                    sb.append(curLine + "\n");
                }
                curLine = br.readLine();
            }
            //add last lesson
            articleBeanList.add(new ArticleBean(curArticleName, sb.toString(), bookBean));
            //save to database
            ActiveAndroid.beginTransaction();
            for (ArticleBean article : articleBeanList) {
                article.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
            try {
                br.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //抛出BookBean数据更新event
        EventBus.getDefault().post(new UpdateBookListEvent());
    }

    /**
     * 加载raw中关键字文件
     */
    public void loadRawWords(Context context) {
        Scanner scanner = null;
        ArrayList<WordBean> wordBeenList = new ArrayList<>();
        try {
            scanner = new Scanner(context.getResources().openRawResource(R.raw.nce4_words));
            String curWord = "";
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    WordBean wordBean = new WordBean(curWord, scanner.nextInt());
                    wordBeenList.add(wordBean);
                    curWord = "";
                } else {
                    curWord = curWord + scanner.next();
                }
            }
            //add to database
            ActiveAndroid.beginTransaction();
            for (WordBean wordBean : wordBeenList) {
                wordBean.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
            if (scanner != null)
                scanner.close();
        }
    }

    @Override
    public Observable loadRawFiles(final Context context) {
        return Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        //进行子线程文件操作
                        loadRawWords(context);
                        loadRawBookBeans(context);
                        return null;
                    }
                });
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
                .execute();
    }

    /**
     * 加载数据库中所有ArticleBean
     *
     * @param bookBean
     * @return
     */
    @Override
    public List<ArticleBean> getAllArticleBeans(BookBean bookBean) {
        return bookBean.getArticleBeanList();
    }

}
