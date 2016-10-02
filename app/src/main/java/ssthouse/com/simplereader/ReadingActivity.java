package ssthouse.com.simplereader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
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
            actionBar.setTitle("SimpleReader");
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
        Timber.e(mArticleBean.content);

        //TODO  test click
//        mTvMain.setText(mArticleBean.content);

//        mTvMain.setMovementMethod(new LinkTouchMovementMethod());

        //toggle分析slid bar
        mFabToggle.setOnClickListener(clickListener);

        mRsvLevel.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                //TODO
                Timber.e("current index:\t" + index);
            }
        });

        //处理文字 注: mTOuchableSpan为空的时候, 不可以设置哦....
        foo();
    }

    private TouchableSpan getInstance() {
        return new TouchableSpan(getResources().getColor(R.color.black), 0xFFFFFFFF,
                getResources().getColor(R.color.colorPrimary)) {
            @Override
            public void onClick(View widget) {
                //clear all other click
                for (TouchableSpan touchableSpan : mTouchableSpanList) {
                    touchableSpan.setPressed(false);
                }
                setPressed(true);
                mTvMain.invalidate();
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
                set.start();
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

    private List<TouchableSpan> mTouchableSpanList = new ArrayList<>();

    private void foo() {
        mTvMain.setText("");
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            strList.add("testStr ");
        }
        for (String str : strList) {
            SpannableString text = new SpannableString(str);
            TouchableSpan touchableSpan = getInstance();
            mTouchableSpanList.add(touchableSpan);
            text.setSpan(touchableSpan, 0, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(text);
        }
        mTvMain.setText(ssb, TextView.BufferType.SPANNABLE);
        mTvMain.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private class LinkTouchMovementMethod extends LinkMovementMethod {
        //当前点击的span
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();
            x += textView.getScrollX();
            y += textView.getScrollY();
            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
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
