package ssthouse.com.simplereader;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import butterknife.Bind;
import ssthouse.com.simplereader.base.BaseActivity;
import ssthouse.com.simplereader.main.MainActivity;

/**
 * Created by ssthouse on 2016/9/28.
 */

public class SplashActivity extends BaseActivity {

    private static final int ANIMATION_TIME = 1500;

    @Bind(R.id.id_iv_bg)
    ImageView ivBg;

    @Override
    protected void init() {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

        Animator animatorX = ObjectAnimator.ofFloat(ivBg, "scaleX", 1.0f, 1.15f)
                .setDuration(ANIMATION_TIME);
        Animator animatorY = ObjectAnimator.ofFloat(ivBg, "scaleY", 1.0f, 1.15f)
                .setDuration(ANIMATION_TIME);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                MainActivity.start(SplashActivity.this);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }
}
