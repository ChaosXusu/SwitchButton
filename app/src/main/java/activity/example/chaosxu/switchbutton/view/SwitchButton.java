package activity.example.chaosxu.switchbutton.view;/**
 * Created by ChaosXu on 2016/05/29 029.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import activity.example.chaosxu.switchbutton.R;

/**
 * Created by ChaosXu on 2016/05/29 029.
 */
public class SwitchButton extends View {
        private Paint mPaint;
        private Bitmap mSwitchBitmap;//滑动开关背景图片
        private Bitmap mSlideBitmap;//滑块的背景图片
        private int mMAX_left;//滑块left的最大值
        private int mSlideLeft;//滑块的移动left值

        private boolean isOpen = false;//记录当前开关的状态，true为打开，false为关闭
        private OnCheckChangeListener mOnCheckChangeListener;//状态改变监听接口对象
        private int mStartX;

        private int moveX;//用来记录手指在控件上移动的间距，用来判断是点击事件还是触摸事件

        private boolean isClick;//用来记录是否是点击事件还是触摸事件，true是点击事件，false是触摸事件

        //当使用代码创建该控件时，调用这个构造方法
        public SwitchButton(Context context) {
                super(context);
                init();
        }

        //在xml中引用该控件时，调用这个构造方法
        public SwitchButton(Context context, AttributeSet attrs) {
                super(context, attrs);
                init();
                String namespace = "http://schemas.android.com/apk/res-auto";
                //状态值属性
                isOpen = attrs.getAttributeBooleanValue(namespace, "isOpen", false);
                if (isOpen) {
                        mSlideLeft = mMAX_left;
                } else {
                        mSlideLeft = 0;
                }
                //滑块的背景属性
                int slidBitmapId = attrs.getAttributeResourceValue(namespace, "slidBitmap", -1);
                if (slidBitmapId > -1) {
                        mSlideBitmap = BitmapFactory.decodeResource(getResources(), slidBitmapId);
                }
        }

        //带有样式的时候，引用该控件时，调用这个构造方法
        public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                init();
        }


        //初始化操作
        private void init() {
                mPaint = new Paint();
                mPaint.setColor(Color.RED);//设置画笔为红色

                //获取滑动开关的背景图片
                mSwitchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
                //获取滑块的背景图片
                mSlideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);

                mMAX_left = mSwitchBitmap.getWidth() - mSlideBitmap.getWidth();

                //设置点击事件
                this.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (isClick) {
                                        if (isOpen) {
                                                System.out.println("开关关闭");
                                                isOpen = false;
                                                mSlideLeft = 0;
                                        } else {
                                                System.out.println("开关打开");
                                                isOpen = true;
                                                mSlideLeft = mMAX_left;
                                        }

                                        if (mOnCheckChangeListener != null) {
                                                mOnCheckChangeListener.onCheckChanged(SwitchButton.this, isOpen);
                                        }

                                        invalidate();//强制控件重新绘制，ondraw方法就会重新被调用
                                }

                        }
                });
        }

        //测量方法
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                //        setMeasuredDimension(100,100);
                setMeasuredDimension(mSwitchBitmap.getWidth(), mSwitchBitmap.getHeight());
        }

        //绘制方法

        /**
         * @param canvas 画布：在画布上绘制控件后，就能够在屏幕上显示
         */
        @Override
        protected void onDraw(Canvas canvas) {

                //        canvas.drawRect(0,0,200,200,mPaint);
                //把滑动开关的背景图片绘制到画布上
                canvas.drawBitmap(mSwitchBitmap, 0, 0, null);
                //绘制滑动块的背景
                canvas.drawBitmap(mSlideBitmap, mSlideLeft, 0, null);
        }

        //设置set方法
        public void setOnCheckChangeListener(OnCheckChangeListener listener) {
                this.mOnCheckChangeListener = listener;
        }

        //创建出状态改变接口
        public interface OnCheckChangeListener {
                public void onCheckChanged(View view, boolean isOpen);
        }

        //事件处理方法，手指触摸控件时，该方法就会被调用
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN://手指按下
                                //1、记录起始点
                                mStartX = (int) event.getX();
                                break;
                        case MotionEvent.ACTION_MOVE://手指移动
                                //2、记录结束点
                                int endX = (int) event.getX();
                                //3、计算出间距
                                int diffX = endX - mStartX;
                                moveX = moveX + Math.abs(diffX);

                                //4、更新mslidleft
                                mSlideLeft = mSlideLeft + diffX;
                                if (mSlideLeft < 0) {//设置左边界
                                        mSlideLeft = 0;
                                }
                                if (mSlideLeft > mMAX_left) {//设置右边界
                                        mSlideLeft = mMAX_left;
                                }
                                //5、刷新界面
                                invalidate();
                                //6、重置起始点
                                mStartX = endX;
                                break;
                        case MotionEvent.ACTION_UP://手指抬起

                                if (moveX > 5) {
                                        System.out.println("触摸事件");
                                        isClick = false;
                                } else {
                                        System.out.println("点击事件");
                                        isClick = true;
                                }

                                moveX = 0;

                                if (!isClick) {//如果是触摸事件则执行下面的代码
                                        int center = mMAX_left / 2;//中心线
                                        if (mSlideLeft > center) {
                                                isOpen = true;
                                                mSlideLeft = mMAX_left;
                                        } else {
                                                isOpen = false;
                                                mSlideLeft = 0;
                                        }
                                        invalidate();

                                        if (mOnCheckChangeListener != null) {
                                                mOnCheckChangeListener.onCheckChanged(SwitchButton.this, isOpen);
                                        }
                                }


                                break;
                }
                return super.onTouchEvent(event);//触摸事件由我们自己处理
        }
}
