package com.handmark.pulltorefresh.library.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.utils.DisplayUtils;

/**
 * Created by Aphrodite on 2018/6/28.
 */
public class CircleLoadingProgressBar extends View {
    private Context mContext;

    // View宽
    private int mWidth;
    // View高
    private int mHeight;
    //圆点背景填充色
    private int mPointColor;
    //圆点半径
    private int mPointRadius;
    //大圆外边缘线条颜色
    private int mCircleColor;

    private Paint mCirclePaint;
    private Paint mPointPaint;

    private float mPointProgress;
    private ValueAnimator mAnimator;

    public CircleLoadingProgressBar(Context context) {
        this(context, null);
    }

    public CircleLoadingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initAttribute(attrs);
        initData();
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.circleLoadingProgressBar);
        mPointColor = array.getColor(R.styleable.circleLoadingProgressBar_pointColor, Color.WHITE);
        mPointRadius = DisplayUtils.dip2px(mContext, 1);
        mCircleColor = array.getColor(R.styleable.circleLoadingProgressBar_circleColor, Color.parseColor("#65E6E6E6"));
        array.recycle();
    }

    private void initData() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);

        mPointPaint = new Paint();
        mPointPaint.setColor(mPointColor);
        mPointPaint.setStrokeWidth(1);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int myWidthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int myWidthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int myHeightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int myHeightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // 获取宽
        if (myWidthSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            mWidth = myWidthSpecSize;
        } else {
            // wrap_content
            mWidth = getResources().getDimensionPixelSize(R.dimen.dp_12);
        }

        // 获取高
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = getResources().getDimensionPixelSize(R.dimen.dp_12);
        }
        // 设置该view的宽高
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoint(canvas);
        drawCircle(canvas);
    }

    /**
     * 绘制圆圈
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mPointRadius, mCirclePaint);
    }

    /**
     * 绘制小圆点
     *
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {
        canvas.save();
        canvas.rotate(mPointProgress * 3.6f, mWidth / 2, mHeight / 2);
        canvas.drawCircle(mWidth / 2, mPointRadius, mPointRadius, mPointPaint);
        canvas.restore();
    }

    /**
     * 启动小圆点旋转动画
     */
    public void startDotAnimator() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAnimator = ValueAnimator.ofFloat(0, 100);
            mAnimator.setDuration(1000);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @SuppressLint("NewApi")
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 设置小圆点的进度，并通知界面重绘
                    mPointProgress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mAnimator.start();
        }
    }
}
