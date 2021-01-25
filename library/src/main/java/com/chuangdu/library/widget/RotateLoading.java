package com.chuangdu.library.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.chuangdu.library.R;


/**
 * <p>
 * 圆形加载样式
 */
public class RotateLoading extends View {

    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_SHADOW_POSITION = 2;

    private Paint mPaint;
    private RectF loadingRectF;
    private RectF shadowRectF;

    private int width;
    private int topColor;
    private int bottomColor;
    private int shadowPosition;

    private int topDegree = 10;
    private int bottomDegree = 190;
    private int speedOfDegree = 10;
    private float speedOfArc;

    private float arc;
    private boolean isStart = false;
    private boolean changeBigger = true;

    public RotateLoading(Context context) {
        this(context, null);
    }

    public RotateLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RotateLoading);
        topColor = ta.getColor(R.styleable.RotateLoading_loading_top_color, Color.WHITE);
        bottomColor = ta.getColor(R.styleable.RotateLoading_loading_bottom_color, Color.WHITE);
        width = ta.getDimensionPixelOffset(R.styleable.RotateLoading_loading_width, DEFAULT_WIDTH);
        shadowPosition = ta.getDimensionPixelOffset(R.styleable.RotateLoading_shadow_position, DEFAULT_SHADOW_POSITION);
        ta.recycle();

        speedOfArc = speedOfDegree / 4;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        // 线宽单位是像素
        mPaint.setStrokeWidth(width);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        arc = 10;

        loadingRectF = new RectF(2 * width, 2 * width, w - 2 * width, w - 2 * width);
        shadowRectF = new RectF(2 * width + shadowPosition, 2 * width + shadowPosition, w - 2 * width + shadowPosition, w - 2 *
                width + shadowPosition);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isStart) {
            return;
        }

        // 绘制阴影
        mPaint.setColor(Color.parseColor("#1a000000"));
        canvas.drawArc(shadowRectF, topDegree, arc, false, mPaint);
        canvas.drawArc(shadowRectF, bottomDegree, arc, false, mPaint);

        // 绘制圆弧
        mPaint.setColor(topColor);
        canvas.drawArc(loadingRectF, topDegree, arc, false, mPaint);
        mPaint.setColor(bottomColor);
        canvas.drawArc(loadingRectF, bottomDegree, arc, false, mPaint);

        topDegree += speedOfDegree;
        bottomDegree += speedOfDegree;
        if (topDegree > 360) {
            topDegree = topDegree - 360;
        }
        if (bottomDegree > 360) {
            bottomDegree = bottomDegree - 360;
        }

        if (changeBigger) {
            if (arc < 160) {
                arc += speedOfArc;
                invalidate();
            }
        } else {
            if (arc > speedOfDegree) {
                arc -= 2 * speedOfArc;
                invalidate();
            }
        }

        if (arc >= 160 || arc <= 10) {
            changeBigger = !changeBigger;
            invalidate();
        }
    }

    public void showLoading() {
        startAnimator();
        isStart = true;
        invalidate();
    }

    private void startAnimator() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.0f, 1);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.0f, 1);
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setInterpolator(new LinearInterpolator());
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new LinearInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.start();
    }

    public void hideLoading() {
        stopAnimator();
        invalidate();
    }

    private void stopAnimator() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setInterpolator(new LinearInterpolator());
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new LinearInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
}
