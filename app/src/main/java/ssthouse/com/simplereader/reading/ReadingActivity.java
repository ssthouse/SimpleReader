package ssthouse.com.simplereader.reading;

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
import ssthouse.com.simplereader.R;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.bean.WordBean;
import ssthouse.com.simplereader.utils.ToastUtil;

/**
 * Created by ssthouse on 2016/9/30.
 */
public class ReadingActivity extends BaseActivity {

    public static final String KEY_EXTRA_ARTICLE_ID = "ArticleBeanId";
    public static final int ANIMATE_DURATION = 500;
    public static final int ANIMATE_OFFSET_X = 1200;
    public static final int ANIMATE_ROTATE_DEGREE = 180;

    private ArticleBean mArticleBean;

    @Bind(R.id.id_tb)
    Toolbar mToolbar;

    @Bind(R.id.id_tv_main)
    TextView mTvMain;

    @Bind(R.id.id_fab_toggle_slide)
    FloatingActionButton mFabToggle;

    @Bind(R.id.id_rsv_level)
    RangeSliderView mRsvLevel;
    private boolean mRsbIsVisiable = false;

    //所有可点击单词
    private List<TouchableSpan> mTouchableSpanList = new ArrayList<>();
    private TouchableSpan mCurFocusSpan;
    private int mCurLevel = -1;
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
            actionBar.setTitle(R.string.str_level_reading);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //获取ArticleBean 为空return toast提示
        long articleId = getIntent().getLongExtra(KEY_EXTRA_ARTICLE_ID, 0);
        if (articleId == 0) {
            ToastUtil.toastSort(this, getString(R.string.str_empty_article));
            return;
        }
        mArticleBean = ArticleBean.load(ArticleBean.class, articleId);
        if (mArticleBean == null) {
            ToastUtil.toastSort(this, getString(R.string.str_empty_article));
            return;
        }
        //slide bar 显示开关
        mFabToggle.setOnClickListener(clickListener);
        //分级显示关键词
        mRsvLevel.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                //index = 0 代表不显示高亮关键词
                mCurLevel = index - 1;
                updateTextViewTouchableSpan();
            }
        });
        initTouchableSpan();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFabToggle.setClickable(false);
            mRsbIsVisiable = !mRsbIsVisiable;
            Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mFabToggle.setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };
            ObjectAnimator animatorOne;
            ObjectAnimator animatorTwo;
            AnimatorSet set = new AnimatorSet();
            if (mRsbIsVisiable) {
                mRsvLevel.setVisibility(View.VISIBLE);
                animatorOne = ObjectAnimator.ofFloat(mRsvLevel, "translationX", ANIMATE_OFFSET_X, 0);
                animatorTwo = ObjectAnimator.ofFloat(mFabToggle, "rotation", 0, ANIMATE_ROTATE_DEGREE);
            } else {
                animatorOne = ObjectAnimator.ofFloat(mRsvLevel, "translationX", 0, ANIMATE_OFFSET_X);
                animatorTwo = ObjectAnimator.ofFloat(mFabToggle, "rotation", -ANIMATE_ROTATE_DEGREE, 0);
            }
            set.setDuration(ANIMATE_DURATION);
            set.addListener(animatorListener);
            set.play(animatorOne)
                    .with(animatorTwo);
            set.start();
        }
    };

    /**
     * 初始话可点击文字
     */
    private void initTouchableSpan() {
        //获取所有WordBean
        List<WordBean> allWordBeenList = new Select().from(WordBean.class).execute();
        Map<String, WordBean> wordBeanMap = new HashMap<>();
        for (WordBean wordBean : allWordBeenList) {
            wordBeanMap.put(wordBean.name, wordBean);
        }
        //for every line
        List<String> articleWordList = new ArrayList<>();
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        String[] lines = mArticleBean.content.split("\n");
        List<String> lineList = Arrays.asList(lines);
        for (String line : lineList) {
            String words[] = line.split("\\s+");
            for (String word : words) {
                articleWordList.add(word);
                SpannableString text = new SpannableString(word + " ");
                TouchableSpan touchableSpan = getTouchableSpanInstance();
                mTouchableSpanList.add(touchableSpan);
                text.setSpan(touchableSpan, 0, word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.append(text);
            }
            ssb.append("\n");
        }
        //init mWordBeanList
        for (String str : articleWordList) {
            if (wordBeanMap.containsKey(str)) {
                mWordBeanList.add(wordBeanMap.get(str));
            } else {
                mWordBeanList.add(new WordBean(str, Integer.MAX_VALUE));
            }
        }
        mTvMain.setText(ssb, TextView.BufferType.SPANNABLE);
        mTvMain.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 获取可点击span实例
     *
     * @return
     */
    private TouchableSpan getTouchableSpanInstance() {
        return new TouchableSpan(getResources().getColor(R.color.black_text),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.colorPrimary)) {
            @Override
            public void onClick(View widget) {
                //设置当前focus span
                mCurFocusSpan = this;
                updateTextViewTouchableSpan();
            }
        };
    }

    /**
     * 刷新TextView
     */
    private void updateTextViewTouchableSpan() {
        for (int i = 0; i < mWordBeanList.size(); i++) {
            //如果大于level set pressed true
            if (mWordBeanList.get(i).level <= mCurLevel) {
                mTouchableSpanList.get(i).setPressed(true);
            } else {
                mTouchableSpanList.get(i).setPressed(false);
            }
        }
        if (mCurFocusSpan != null)
            mCurFocusSpan.setPressed(true);
        mTvMain.invalidate();
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
