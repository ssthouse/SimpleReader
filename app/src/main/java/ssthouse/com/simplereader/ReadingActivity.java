package ssthouse.com.simplereader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;

import butterknife.Bind;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.bean.ArticleBean;
import ssthouse.com.simplereader.utils.ToastUtil;
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

        mTvMain.setText(mArticleBean.content);

        //toggle分析slid bar
        mFabToggle.setOnClickListener(new View.OnClickListener() {
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
        });

        //TODO  等级切换
        mRsvLevel.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                //TODO
                Timber.e("current index:\t" + index);
            }
        });

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
