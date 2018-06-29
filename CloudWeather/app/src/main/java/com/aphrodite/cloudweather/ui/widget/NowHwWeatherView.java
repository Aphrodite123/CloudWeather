package com.aphrodite.cloudweather.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.aphrodite.cloudweather.R;
import com.aphrodite.cloudweather.utils.DisplayUtils;
import com.aphrodite.cloudweather.utils.Logger;

/**
 * Created by Aphrodite on 2018/6/14.
 */
public class NowHwWeatherView extends View {
    private static final String TAG = NowHwWeatherView.class.getSimpleName();
    private Context mContext;

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;

    private float mWidth;
    private float mHeight;
    private float mRadius;
    //圆弧开始角
    private int mStartAngle;
    //圆弧总角度数
    private int mTotalAngle;
    //圆弧分隔的份数
    private int mDivideNumber;

    //当前温度
    private int mCurrentTemp;
    //最低温度
    private int minTemp;
    //最高温度
    private int maxTemp;
    private Bitmap bitmap;
    //0℃初始角度
    private int mZeroAngle;
    //总覆盖的角
    private int mCoverAngle;


    private int mOffset;

    private ValueAnimator mAnimator;

    private int mPointAngle;
    private float mPointProgress;

    public NowHwWeatherView(Context context) {
        this(context, null);
    }

    public NowHwWeatherView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NowHwWeatherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        initPaint();

        mStartAngle = 120;
        mTotalAngle = 300;
        mDivideNumber = 80;//刻度份数

        mCurrentTemp = 26;
        maxTemp = 27;
        minTemp = 20;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.weather_icon_0);
        mZeroAngle = 230;
        mCoverAngle = 90;
        mOffset = 22;
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setColor(Color.WHITE);
        mArcPaint.setStrokeWidth(2);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStrokeWidth(4);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(144);

        mPointPaint = new Paint();
        mPointPaint.setColor(Color.WHITE);
        mPointPaint.setStrokeWidth(2);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wrap_Len = 600;
        int width = measureDimension(wrap_Len, widthMeasureSpec);
        int height = measureDimension(wrap_Len, heightMeasureSpec);
        int len = Math.min(width, height);
        //保证是一个正方形
        setMeasuredDimension(len, len);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        mRadius = (mWidth - getPaddingLeft() - getPaddingRight()) / 2;//半径
        canvas.translate(mWidth / 2, mHeight / 2);

        //画短线
        drawLine(canvas);
        //画中间的温度和下边的图片
        drawTextBitmapView(canvas);
        //画动态温度
        drawTempLineView(canvas);
        //画圆点
        drawRotateDot(canvas);
    }

    private void drawTempLineView(Canvas canvas) {
        mTextPaint.setTextSize(24);

        int startTempAngle = getStartAngle(minTemp, maxTemp);
        canvas.drawText(minTemp + "°", getRealCosX(startTempAngle, mOffset, true), getRealSinY(startTempAngle, mOffset, true), mTextPaint);
        canvas.drawText(maxTemp + "°", getRealCosX(startTempAngle + mCoverAngle, mOffset, true), getRealSinY(startTempAngle + mCoverAngle, mOffset, true), mTextPaint);
    }

    /**
     * 画旋转小圆点
     */
    private void drawRotateDot(Canvas canvas) {
        canvas.save();

        int startTempAngle = getStartAngle(minTemp, maxTemp);
        mPointPaint.setColor(getRealColor(minTemp, maxTemp));
        mPointAngle = (int) mPointProgress + startTempAngle;

        float x = getRealCosX(mPointAngle, 60, false);
        float y = getRealSinY(mPointAngle, 60, false);
        canvas.drawCircle(x, y, DisplayUtils.dip2px(mContext, 3), mPointPaint);
        Logger.i(TAG, "Enter drawRotateDot method. (" + x + "," + y + ")");
        canvas.restore();
    }

    /**
     * 启动小圆点旋转动画
     */
    public void startDotAnimator() {
        mAnimator = ValueAnimator.ofFloat(0, ((mCurrentTemp - minTemp) * mCoverAngle) / (maxTemp - minTemp));
        mAnimator.setDuration(5 * 1000);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置小圆点的进度，并通知界面重绘
                mPointProgress = (Float) animation.getAnimatedValue();
                Logger.i(TAG, "Enter startDotAnimator method." + mPointProgress);
                invalidate();
            }
        });
        mAnimator.start();
    }

    /**
     * 画圆环
     *
     * @param canvas
     */
    private void drawArcView(Canvas canvas) {
        RectF mRect = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        canvas.drawArc(mRect, mStartAngle, mTotalAngle, false, mArcPaint);
    }

    /**
     * 循环画出短线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.save();
        float angle = (float) mTotalAngle / mDivideNumber;//刻度间隔
        canvas.rotate(-270 + mStartAngle);//将起始刻度点旋转到正上方
        for (int i = 0; i <= mDivideNumber; i++) {
            if (i == 0 || i == mDivideNumber) {
                mLinePaint.setStrokeWidth(1);
                mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0, -mRadius - DisplayUtils.dip2px(mContext, 7), 0, -mRadius + DisplayUtils.dip2px(mContext, 18), mLinePaint);

            } else if (i > getStartLineIndex(minTemp, maxTemp) && i <= getEndLineIndex(minTemp, maxTemp)) {
                mLinePaint.setStrokeWidth(3);
                mLinePaint.setColor(getRealColor(minTemp, maxTemp));
                canvas.drawLine(0, -mRadius, 0, -mRadius + DisplayUtils.dip2px(mContext, 18), mLinePaint);

            } else {
                mLinePaint.setStrokeWidth(2);
                mLinePaint.setColor(Color.WHITE);
                canvas.drawLine(0, -mRadius, 0, -mRadius + DisplayUtils.dip2px(mContext, 18), mLinePaint);
            }
            canvas.rotate(angle);//逆时针旋转
        }
        canvas.restore();
    }

    private void drawTextBitmapView(Canvas canvas) {
        mTextPaint.setTextSize(144);
        canvas.drawText(mCurrentTemp + "°", 0, 0 + getTextPaintOffset(mTextPaint), mTextPaint);
        canvas.drawBitmap(bitmap, 0 - bitmap.getWidth() / 2, mRadius - bitmap.getHeight() / 2 -
                50, null);
    }

    /**
     * 改变文字的Y坐标偏移量
     *
     * @param paint
     * @return
     */
    public float getTextPaintOffset(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return -fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
    }

    //根据角获得X坐标  R*cos&+getTextPaintOffset(mTextPaint)-off
    private float getCosX(int Angle) {
        return (float) (mRadius * Math.cos(Angle * Math.PI / 180)) + getTextPaintOffset(mTextPaint);
    }

    //根据角获得y坐标
    private float getSinY(int Angle) {
        return (float) (mRadius * Math.sin(Angle * Math.PI / 180)) + getTextPaintOffset(mTextPaint);
    }

    //根据项限加一个偏移量
    private float getRealCosX(int Angle, int off, boolean outoff) {
        if (!outoff) {
            off = -off;
        }
        if (getCosX(Angle) < 0) {
            return getCosX(Angle) - off;
        } else {
            return getCosX(Angle) + off;
        }
    }

    private float getRealSinY(int Angle, int off, boolean outoff) {
        if (!outoff) {
            off = -off;
        }
        if (getSinY(Angle) < 0) {
            return getSinY(Angle) - off;
        } else {
            return getSinY(Angle) + off;
        }
    }

    //根据当天温度范围获得扇形开始角。
    private int getStartAngle(int minTemp, int maxTemp) {
        int startFgAngle = 0;
        if (minTemp >= maxTemp) {
            return startFgAngle;
        }
        if (minTemp <= 0) {
            startFgAngle = mZeroAngle - (0 - minTemp) * mCoverAngle / (maxTemp - minTemp);
        } else {
            startFgAngle = mZeroAngle + (minTemp - 0) * mCoverAngle / (maxTemp - minTemp);
        }
        //边界 start
        if (startFgAngle <= mStartAngle) {//如果开始角小于startAngle，防止过边界
            startFgAngle = mStartAngle + 10;
        } else if ((startFgAngle + mCoverAngle) >= (mStartAngle + mTotalAngle)) {//如果结束角大于(mStartAngle+mTotalAngle)
            startFgAngle = mStartAngle + mTotalAngle - 20 - mCoverAngle;
        }
        //边界 end
        return startFgAngle;
    }

    //根据当天温度范围获取开始短线的索引
    private int getStartLineIndex(int minTemp, int maxTemp) {
        return (getStartAngle(minTemp, maxTemp) - mStartAngle) / (mTotalAngle / mDivideNumber);
    }

    private int getEndLineIndex(int minTemp, int maxTemp) {
        return (getStartAngle(minTemp, maxTemp) - mStartAngle) / (mTotalAngle / mDivideNumber) + mCoverAngle / (mTotalAngle / mDivideNumber);
    }

    //根据温度返回颜色值
    public int getRealColor(int minTemp, int maxTemp) {
        if (maxTemp <= 0) {
            return Color.parseColor("#00008B");//深海蓝
        } else if (minTemp <= 0 && maxTemp > 0) {
            return Color.parseColor("#4169E1");//黄君兰
        } else if (minTemp > 0 && minTemp < 15) {
            return Color.parseColor("#40E0D0");//宝石绿
        } else if (minTemp >= 15 && minTemp < 25) {
            return Color.parseColor("#00FF00");//酸橙绿
        } else if (minTemp >= 25 && minTemp < 30) {
            return Color.parseColor("#FFD700");//金色
        } else if (minTemp >= 30) {
            return Color.parseColor("#CD5C5C");//印度红
        }
        return Color.parseColor("#00FF00");//酸橙绿;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.bitmap = mBitmap;
        invalidate();
    }
}
