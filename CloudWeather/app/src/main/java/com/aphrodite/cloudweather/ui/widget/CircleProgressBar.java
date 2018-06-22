package com.aphrodite.cloudweather.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.utils.DisplayUtils;
import com.aphrodite.cloudweather.utils.Logger;

/**
 * 圆形进度条
 * Created by Aphrodite on 2018/6/14.
 */
public class CircleProgressBar extends View {
    private static final String TAG = CircleProgressBar.class.getSimpleName();
    private Context mContext;

    // 刻度画笔
    private Paint mScalePaint;

    // 小原点画笔
    private Paint mDotPaint;

    // 文字画笔
    private Paint mTextPaint;

    // 当前进度
    private int progress = 0;

    /**
     * 小圆点的当前进度
     */
    public float mDotProgress;

    // View宽
    private int mWidth;

    // View高
    private int mHeight;

    private int indexColor;

    private int baseColor;

    private int dotColor;

    private int textSize;

    private int textColor;

    private ValueAnimator animator;

    //最低温度
    private int minTemp;
    //最高温度
    private int maxTemp;
    //圆弧起始位置角度，默认120°
    private final int START_ANGLE = 120;
    //圆弧总角度，默认300°
    private final int TOTAL_ANGLE = 300;
    //0℃位置角度，默认230°
    private final int ZERO_ANGLE = 230;
    //总刻度数目
    private final int DIVIDE_NUMBER = 100;

    public CircleProgressBar(Context context) {
        super(context);
        initUI();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.circleProgressBar);
        baseColor = tya.getColor(R.styleable.circleProgressBar_baseColor, Color.LTGRAY);
        indexColor = tya.getColor(R.styleable.circleProgressBar_indexColor, Color.BLUE);
        textColor = tya.getColor(R.styleable.circleProgressBar_textColor, Color.BLUE);
        dotColor = tya.getColor(R.styleable.circleProgressBar_dotColor, Color.RED);
        textSize = tya.getDimensionPixelSize(R.styleable.circleProgressBar_textSize, 36);
        tya.recycle();
        initUI();
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取用户配置属性
        TypedArray tya = context.obtainStyledAttributes(attrs, R.styleable.circleProgressBar);
        baseColor = tya.getColor(R.styleable.circleProgressBar_baseColor, Color.LTGRAY);
        indexColor = tya.getColor(R.styleable.circleProgressBar_indexColor, Color.BLUE);
        textColor = tya.getColor(R.styleable.circleProgressBar_textColor, Color.BLUE);
        dotColor = tya.getColor(R.styleable.circleProgressBar_dotColor, Color.RED);
        textSize = tya.getDimensionPixelSize(R.styleable.circleProgressBar_textSize, 36);
        tya.recycle();

        initUI();
    }

    private void initUI() {
        mContext = getContext();

        // 刻度画笔
        mScalePaint = new Paint();
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStrokeWidth(DisplayUtils.dip2px(mContext, 1));
        mScalePaint.setStrokeCap(Paint.Cap.ROUND);
        mScalePaint.setColor(baseColor);
        mScalePaint.setStyle(Paint.Style.STROKE);

        // 小圆点画笔
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(dotColor);
        mDotPaint.setStrokeWidth(DisplayUtils.dip2px(mContext, 1));
        mDotPaint.setStyle(Paint.Style.FILL);

        // 文字画笔
        mTextPaint = new TextPaint();
        //抗锯齿功能
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mTextPaint.setTypeface(typeface);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArcScale(canvas);
        drawTextValue(canvas);
        drawRotateDot(canvas);
    }

    /**
     * 画刻度
     */
    private void drawArcScale(Canvas canvas) {
        canvas.save();
        float angle = (float) TOTAL_ANGLE / DIVIDE_NUMBER;
        canvas.rotate(-270 + START_ANGLE, mWidth / 2, mHeight / 2);

        int startIndex = getStartIndex(minTemp, maxTemp);
        int endIndex = getEndIndex(minTemp, maxTemp);
        Logger.i(TAG, "Enter drawArcScale method.(" + startIndex + "," + endIndex + ")");

        for (int i = 0; i <= DIVIDE_NUMBER; i++) {
            if (0 == i || DIVIDE_NUMBER == i) {
                mScalePaint.setStrokeWidth(1);
                mScalePaint.setColor(Color.WHITE);
                canvas.drawLine(mWidth / 2, -DisplayUtils.dip2px(mContext, 4), mWidth / 2, DisplayUtils.dip2px(mContext, 18), mScalePaint);
            } else if (i > startIndex && i <= endIndex) {
                mScalePaint.setStrokeWidth(3);
                mScalePaint.setColor(indexColor);
                canvas.drawLine(mWidth / 2, 0, mWidth / 2, DisplayUtils.dip2px(mContext, 18), mScalePaint);
            } else {
                mScalePaint.setStrokeWidth(2);
                mScalePaint.setColor(baseColor);
                canvas.drawLine(mWidth / 2, 0, mWidth / 2, DisplayUtils.dip2px(mContext, 18), mScalePaint);
            }
            // 旋转的度数
            canvas.rotate(angle, mWidth / 2, mHeight / 2);
        }
        canvas.restore();
    }

    /**
     * 画内部数值
     */
    private void drawTextValue(Canvas canvas) {
        canvas.save();

        String showValue = String.valueOf(progress);
        Rect textBound = new Rect();
        mTextPaint.getTextBounds(showValue, 0, showValue.length(), textBound);    // 获取文字的矩形范围
        float textWidth = textBound.right - textBound.left;  // 获得文字宽
        float textHeight = textBound.bottom - textBound.top; // 获得文字高
        canvas.drawText(showValue, mWidth / 2 - textWidth / 2, mHeight / 2 + textHeight / 2, mTextPaint);

        canvas.restore();
    }

    /**
     * 画旋转小圆点
     */
    private void drawRotateDot(final Canvas canvas) {
        canvas.save();

        canvas.rotate(mDotProgress * 3.6f, mWidth / 2, mHeight / 2);
        canvas.drawCircle(mWidth / 2, DisplayUtils.dip2px(mContext, 18) + DisplayUtils.dip2px
                (mContext, 5), DisplayUtils.dip2px(mContext, 3), mDotPaint);

        canvas.restore();
    }

    /**
     * 启动小圆点旋转动画
     */
    public void startDotAnimator() {
        animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置小圆点的进度，并通知界面重绘
                mDotProgress = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
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
            mWidth = DisplayUtils.dip2px(mContext, 120);
        }

        // 获取高
        if (myHeightSpecMode == MeasureSpec.EXACTLY) {
            // match_parent/精确值
            mHeight = myHeightSpecSize;
        } else {
            // wrap_content
            mHeight = DisplayUtils.dip2px(mContext, 120);
        }

        // 设置该view的宽高
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 根据温度范围获取起始位置索引
     *
     * @param minTemp
     * @param maxTemp
     * @return
     */
    private int getStartIndex(int minTemp, int maxTemp) {
        int index = Math.abs((getStartAngle(minTemp, maxTemp) - START_ANGLE)) * DIVIDE_NUMBER / TOTAL_ANGLE;
        return index;
    }

    /**
     * 根据温度范围获取结束位置索引
     *
     * @param minTemp
     * @param maxTemp
     * @return
     */
    private int getEndIndex(int minTemp, int maxTemp) {
        int index = (getStartAngle(minTemp, maxTemp) + 150) * DIVIDE_NUMBER / TOTAL_ANGLE;
        return index;
    }

    /**
     * 获取最低温度点角度
     *
     * @param startTemp
     * @param endTemp
     * @return
     */
    private int getStartAngle(int startTemp, int endTemp) {
        //零下温度所占总的角度数，默认 230-120 = 110
        int subzeroAngle = ZERO_ANGLE - START_ANGLE;
        //温度大于0℃时总角度，默认 300- （230-120）= 190
        int noSubzeroAngle = TOTAL_ANGLE - subzeroAngle;

        int angle = 0;

        int tempPoor = endTemp - startTemp;
        if (tempPoor < 0) {
            return angle;
        }

        int scope = 0;
        int scale = Math.abs(startTemp) / tempPoor;
        if (1 == scale) {
            scope = tempPoor;
        } else if (scale > 1) {
            scale = scale - 1;
            scope = tempPoor * scale;
        }

        //最低&最高温度大于0℃
        if (startTemp >= 0 && endTemp >= 0) {
            angle = ZERO_ANGLE + noSubzeroAngle * (endTemp - scope) * startTemp / endTemp;
        } else if (startTemp < 0 && endTemp >= 0) {
            //最低小于0℃&最高温度大于0℃
//            angle = ZERO_ANGLE - subzeroAngle *
        } else if (startTemp < 0 && endTemp < 0) {
            //最低小于0℃&最高温度小于0℃
        }
        return angle;
    }

    /**
     * 获取最低温度到最高温度范围大小
     *
     * @param lowestTemp
     * @param highestTemp
     * @return
     */
    private int getScopeAngle(int lowestTemp, int highestTemp) {
        int angle = 0;

        int tempPoor = highestTemp - lowestTemp;
        if (tempPoor <= 0) {
            return angle;
        }

        //最低&最高温度大于0℃
        if (lowestTemp >= 0 && highestTemp >= 0) {
            angle = getOffsetAngle(lowestTemp, highestTemp, false);
        } else if (lowestTemp < 0 && highestTemp >= 0) {
            //最低小于0℃&最高温度大于0℃
            angle = getOffsetAngle(lowestTemp, 0, true) + getOffsetAngle(0, highestTemp, false);
        } else if (lowestTemp < 0 && highestTemp < 0) {
            //最低小于0℃&最高温度小于0℃
            angle = getOffsetAngle(lowestTemp, highestTemp, true);
        }
        return angle;
    }

    /**
     * 获取偏移角度
     *
     * @param isSubzero
     * @return
     */
    private int getOffsetAngle(int startTemp, int endTemp, boolean isSubzero) {
        int offsetAngle = 0;
        //零下温度所占总的角度数，默认 230-120 = 110
        int subzeroAngle = ZERO_ANGLE - START_ANGLE;
        //温度大于0℃时总角度，默认 300- （230-120）= 190
        int noSubzeroAngle = TOTAL_ANGLE - subzeroAngle;

        int tempPoor = endTemp - startTemp;
        if (tempPoor < 0) {
            return offsetAngle;
        }

        int scope = 0;
        int scale = Math.abs(startTemp) / tempPoor;
        if (1 == scale) {
            scope = tempPoor;
        } else if (scale > 1) {
            scale = scale - 1;
            scope = tempPoor * scale;
        }

        if (isSubzero) {
            offsetAngle = subzeroAngle * scope / endTemp;
        } else {
            offsetAngle = noSubzeroAngle * scope / endTemp;
        }
        return offsetAngle;
    }


    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }
}
