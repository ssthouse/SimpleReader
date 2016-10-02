package ssthouse.com.simplereader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.github.channguyen.rsv.RangeSliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.WordBean;
import ssthouse.com.simplereader.utils.ToastUtil;
import ssthouse.com.simplereader.utils.TouchableSpan;
import timber.log.Timber;

/**
 * 实现分级高亮的基本思路: 将所有文章中单词进行遍历, 将其中存在于数据库中的  保存到一个set中
 * 在
 * Created by ssthouse on 2016/9/30.
 */

public class ReadingActivity extends BaseActivity {

    public static final String KEY_EXTRA_ARTICLE_ID = "ArticleBeanId";
    public static final int ANIMATE_DURATION = 500;

    private ArticleBean mArticleBean;

    @Bind(R.id.id_tb)
    Toolbar mToolbar;

    @Bind(R.id.id_tv_main)
    TextView mTvMain;

    @Bind(R.id.id_fab_toggle_slide)
    FloatingActionButton mFabToggle;

    @Bind(R.id.id_rsv_level)
    RangeSliderView mRsvLevel;

    //所有可点击单词
    private List<TouchableSpan> mTouchableSpanList = new ArrayList<>();
    private TouchableSpan mCurFocusSpan;
    private int mCurLevel = 0;
    //所有当前文章
    private List<WordBean> mWordBeanList = new ArrayList<>();

    public static void start(Context context, ArticleBean articleBean) {
        if (articleBean == null) {
            return;
        }
        Intent intent = new Intent(context, ReadingActivity.class);
        intent.putExtra(KEY_EXTRA_ARTICLE_ID, articleBean.getId());
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_reading;
    }

    @Override
    protected void init() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("分级阅读");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        long articleId = getIntent().getLongExtra(KEY_EXTRA_ARTICLE_ID, 0);
        if (articleId == 0) {
            ToastUtil.toastSort(this, "文章为空");
            return;
        }
        mArticleBean = ArticleBean.load(ArticleBean.class, articleId);
        if (mArticleBean == null) {
            ToastUtil.toastSort(this, "文章为空");
            return;
        }

        mFabToggle.setOnClickListener(clickListener);

        mRsvLevel.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                mCurLevel = index - 1;
                updateTextView();
                Timber.e("current index:\t" + index);
            }
        });

        //处理文字 注: mTouchableSpan为空的时候, 不可以设置哦....
        initTouchableSpan();
    }

    private TouchableSpan getNewTouchableSpan() {
        return new TouchableSpan(getResources().getColor(R.color.black_text), 0xFFFFFFFF,
                getResources().getColor(R.color.colorPrimary)) {
            @Override
            public void onClick(View widget) {
                if (mCurFocusSpan != null) {
                    mCurFocusSpan.setPressed(false);
                }
                //设置当前focus span
                mCurFocusSpan = this;
                setPressed(true);
                updateTextView();
            }
        };
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRsvLevel.getVisibility() == View.VISIBLE) {
                ObjectAnimator animatorOne = ObjectAnimator.ofFloat(mRsvLevel, "translationX", 0, 800);
                ObjectAnimator animatorTwo = ObjectAnimator.ofFloat(mFabToggle, "rotationY", -180, 0);
                AnimatorSet set = new AnimatorSet();
                set.play(animatorOne)
                        .with(animatorTwo);
                set.setDuration(ANIMATE_DURATION);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRsvLevel.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                set.start();
            } else {
                mRsvLevel.setVisibility(View.VISIBLE);
                ObjectAnimator animatorOne = ObjectAnimator.ofFloat(mRsvLevel, "translationX", 800, 0);
                ObjectAnimator animatorTwo = ObjectAnimator.ofFloat(mFabToggle, "rotationY", 0, 180);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(ANIMATE_DURATION);
                set.play(animatorOne)
                        .with(animatorTwo);
                set.start();
            }
        }
    };

    /**
     * 初始话TouchableSpan
     */
    private void initTouchableSpan() {
        //获取所有WordBean
        List<WordBean> allWordBeenList = new Select().from(WordBean.class).execute();
        Map<String, WordBean> wordBeanMap = new HashMap<>();
        for (WordBean wordBean : allWordBeenList) {
            wordBeanMap.put(wordBean.name, wordBean);
        }
        //获取所有当前article的word str -> 添加到wordBeanList中
        String[] strArray = mArticleBean.content.split("\\s+");
        List<String> strList = Arrays.asList(strArray);
        for (String str : strList) {
            if (wordBeanMap.containsKey(str)) {
                mWordBeanList.add(wordBeanMap.get(str));
            } else {
                mWordBeanList.add(new WordBean(str, Integer.MAX_VALUE));
            }
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        for (String str : strList) {
            SpannableString text = new SpannableString(str + " ");
            TouchableSpan touchableSpan = getNewTouchableSpan();
            mTouchableSpanList.add(touchableSpan);
            text.setSpan(touchableSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(text);
        }
        mTvMain.setText(ssb, TextView.BufferType.SPANNABLE);
        mTvMain.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 刷新TextView
     */
    private void updateTextView() {
        for (int i = 0; i < mWordBeanList.size(); i++) {
            //如果大于level set pressed true
            if (mWordBeanList.get(i).level <= mCurLevel) {
                mTouchableSpanList.get(i).setPressed(true);
            } else {
                mTouchableSpanList.get(i).setPressed(false);
            }
        }
        mTvMain.invalidate();
        //设置上一个点击的span为pressed
        if (mCurFocusSpan != null)
            mCurFocusSpan.setPressed(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
