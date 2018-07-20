package com.cheng.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 自定义PopWindow类，封装了PopWindow的一些常用属性，用Builder模式支持链式调用
 *  示例：
 *  View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_test, null);
    CustomPopWindow pw = new CustomPopWindow.PopupWindowBuilder(this, getWindow())
         .setView(view)
         .setTouchable(true)
         .setOutsideTouchable(true)
         .setFocusable(true)
         .setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rec_bg_gray_c8_border))
         .create()
         .showAsDropDown(toolbarLayout.right_icon, 0, 5);
 */
public class CustomPopWindow {

    private Context mContext;
    private Window window;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable = true;
    private boolean mIsOutside = true;
    private int mResLayoutId = -1;
    private View mContentView;
    private Drawable mBackgroundDrawable;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle = -1;

    private boolean mClippEnable = true;//default is true
    private boolean mIgnoreCheekPress = false;
    private int mInputMode = -1;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode = -1;
    private boolean mTouchable = true;//default is ture
    private View.OnTouchListener mOnTouchListener;
    private boolean outColorDark = true;

    protected CustomPopWindow(Context context){
        mContext = context;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public CustomPopWindow showAsDropDown(View anchor){
        if(mPopupWindow!=null){
            if(Build.VERSION.SDK_INT >= 24){
                Rect visibleFrame = new Rect();
                anchor.getGlobalVisibleRect(visibleFrame);
                mHeight = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            }
            setBackDark();
            mPopupWindow.showAsDropDown(anchor);
        }
        return this;
    }

    /**
     * 相对于父控件的下方
     * @param anchor
     * @param xOff the popup's x location offset
     * @param yOff the popup's y location offset
     * @param gravity
     * @return
     */
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity){
        if(mPopupWindow!=null){
            if(Build.VERSION.SDK_INT >= 24){
                Rect visibleFrame = new Rect();
                anchor.getGlobalVisibleRect(visibleFrame);
                mHeight = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            }
            setBackDark();
            mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }
        return this;
    }

    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     * @param parent
     * @param gravity
     * @param x the popup's x location offset
     * @param y the popup's y location offset
     * @return
     */
    public CustomPopWindow showAtLocation(View parent, int gravity, int x, int y){
        if(mPopupWindow!=null){
            if(Build.VERSION.SDK_INT >= 24){
                Rect visibleFrame = new Rect();
                parent.getGlobalVisibleRect(visibleFrame);
                mHeight = parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            }
            setBackDark();
            mPopupWindow.showAtLocation(parent, gravity, x, y);
        }
        return this;
    }

    private void setBackDark(){
        if(window!=null){
            WindowManager.LayoutParams lp = window.getAttributes();
            if(outColorDark){
                lp.alpha = 0.7f;
            }else{
                lp.alpha = 1f;
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
        }
    }

    /**
     * 添加一些属性设置
     * @param popupWindow
     */
    private void apply(PopupWindow popupWindow){
        popupWindow.setClippingEnabled(mClippEnable);
        if(mIgnoreCheekPress){
            popupWindow.setIgnoreCheekPress();
        }
        if(mInputMode!=-1){
            popupWindow.setInputMethodMode(mInputMode);
        }
        if(mSoftInputMode!=-1){
            popupWindow.setSoftInputMode(mSoftInputMode);
        }
        if(mOnDismissListener!=null){
            popupWindow.setOnDismissListener(mOnDismissListener);
        }
        if(mOnTouchListener!=null){
            popupWindow.setTouchInterceptor(mOnTouchListener);
        }
        popupWindow.setTouchable(mTouchable);
    }

    private PopupWindow build(){
        if(mContentView == null){
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null);
        }
        if(mWidth != 0 && mHeight!=0 ){
            mPopupWindow = new PopupWindow(mContentView, mWidth, mHeight);
        }else{
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if(mAnimationStyle!=-1){
            mPopupWindow.setAnimationStyle(mAnimationStyle);
        }
        apply(mPopupWindow);//设置一些属性
        mPopupWindow.setFocusable(mIsFocusable);
        if(mBackgroundDrawable!=null){
            mPopupWindow.setBackgroundDrawable(mBackgroundDrawable);
        }else{
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mPopupWindow.setOutsideTouchable(mIsOutside);
        if(mWidth == 0 || mHeight == 0){
            mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            mWidth = mPopupWindow.getContentView().getMeasuredWidth();
            mHeight = mPopupWindow.getContentView().getMeasuredHeight();
        }
        mPopupWindow.update();
        if(window!=null){
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.alpha = 1.0f;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    window.setAttributes(lp);
                }
            });
        }
        return mPopupWindow;
    }

    /**
     * 关闭popWindow
     */
    public void dissmiss(){
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        if(window!=null){
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 1.0f;
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
        }
        mPopupWindow.dismiss();
    }

    public static class PopupWindowBuilder{

        private CustomPopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context){
            mCustomPopWindow = new CustomPopWindow(context);
        }

        public PopupWindowBuilder(Context context, Window window){
            mCustomPopWindow = new CustomPopWindow(context);
            mCustomPopWindow.window = window;
        }

        public PopupWindowBuilder size(int width, int height){
            mCustomPopWindow.mWidth = width;
            mCustomPopWindow.mHeight = height;
            return this;
        }

        public PopupWindowBuilder setFocusable(boolean focusable){
            mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        public PopupWindowBuilder setView(int resLayoutId){
            mCustomPopWindow.mResLayoutId = resLayoutId;
            mCustomPopWindow.mContentView = null;
            return this;
        }

        public PopupWindowBuilder setView(View view){
            mCustomPopWindow.mContentView = view;
            mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable){
            mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        /**
         * 设置弹窗动画
         * @param animationStyle
         * @return
         */
        public PopupWindowBuilder setAnimationStyle(int animationStyle){
            mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public PopupWindowBuilder setClippingEnable(boolean enable){
            mCustomPopWindow.mClippEnable =enable;
            return this;
        }

        public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress){
            mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public PopupWindowBuilder setInputMethodMode(int mode){
            mCustomPopWindow.mInputMode = mode;
            return this;
        }

        public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener){
            mCustomPopWindow.mOnDismissListener = onDissmissListener;
            return this;
        }

        public PopupWindowBuilder setSoftInputMode(int softInputMode){
            mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public PopupWindowBuilder setTouchable(boolean touchable){
            mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter){
            mCustomPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        //设置背景BackgroundDrawable
        public PopupWindowBuilder setBackgroundDrawable(Drawable backgroundDrawable){
            mCustomPopWindow.mBackgroundDrawable = backgroundDrawable;
            return this;
        }

        //设置外部背景色是否变暗
        //注：需要配合构造参数传入window使用
        public PopupWindowBuilder setOutColorDark(boolean outColorDark){
            mCustomPopWindow.outColorDark = outColorDark;
            return this;
        }

        public CustomPopWindow create(){
            //构建PopWindow
            mCustomPopWindow.build();
            return mCustomPopWindow;
        }

    }

}
