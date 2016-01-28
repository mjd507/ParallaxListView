package com.fighting.qqview_parallax;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * 描述：
 * 作者 mjd
 * 日期：2016/1/28 13:12
 */
public class ParallaxListView extends ListView {

    private static final String TAG = "ParallaxListView";

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //deltaY 竖直方向滑动的瞬时变化量，顶部下拉为-，底部上拉为+
        //scrollY 两端滑动超出的距离，顶部为-，底部为+
        //scrollRangeY 竖立方向滑动的范围
        //maxOverScrollY 竖立方向最大的滑动位置
        //isTouchEvent 是否是用户触摸拉动， true 表示用户手指触摸拉动， false 表示惯性
        Log.e(TAG, "deltaY： " + deltaY + " scrollY:" + scrollY + " scrollRangeY:" + scrollRangeY + " maxOverScrollY:" + maxOverScrollY + " isTouchEvent:" + isTouchEvent);
        //顶部下拉，用户触摸时，将 deltaY 累加给 Header
        if (deltaY < 0 && isTouchEvent) {
            int newHeight = headerImage.getHeight() + Math.abs(deltaY);
            //新高度小于图片原始高度才允许累加变化量
            if (newHeight <= drawableHeight) {
                //让新的值生效
                headerImage.getLayoutParams().height = newHeight;
                headerImage.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    private ImageView headerImage;
    private int orignalHeight;
    private int drawableHeight;

    public void setParallaxImage(ImageView imageView) {
        this.headerImage = imageView;
        //ImageView 初始高度
        orignalHeight = imageView.getHeight();
        //图片原始高度
        drawableHeight = imageView.getDrawable().getIntrinsicHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //松手时，把 currentHeight 恢复到 orignalHeight
                int currentHeight = headerImage.getHeight();
                //300->160 300,299,280,250,200,...160 随时间生成 300 到 160 间的值
                ValueAnimator animator = ValueAnimator.ofInt(currentHeight, orignalHeight);
                animator.setDuration(500);
                //动画更新的监听
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //获取随时间变化得到的 currentHeight 到 orignalHeight 间的值
                        int value = (Integer) animation.getAnimatedValue();
                        //让新的值生效
                        headerImage.getLayoutParams().height = value;
                        headerImage.requestLayout();
                    }
                });
                //设置插值器实现回弹效果
                animator.setInterpolator(new OvershootInterpolator(2));
                animator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
