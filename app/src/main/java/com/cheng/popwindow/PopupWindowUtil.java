package com.cheng.popwindow;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;

public class PopupWindowUtil {

    public static int[] calculatePopWindowPos(Context context, View anchorView, View contentView) {
        return calculatePopWindowPos(context, Gravity.NO_GRAVITY, anchorView, contentView, 0, 0);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * 注：调用该方法应配合showAtLocation使用，且showAtLocation中gravity参数必为Gravity.NO_GRAVITY，此计算出的坐标已经是需要显示的位置
     * 注：屏幕宽高并不意味着可用空间的宽高，比如状态栏/导航栏等高度，是不可用的，需要根据自己项目合理计算出可用空间
     * @param context  上下文
     * @param gravity   重心，除了靠左靠右其余自动跟随控件右对齐
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @param xOff  水平偏移 相对于锚点为正，反之为负
     * @param yOff  垂直偏移 相对于锚点为正，反之为负
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(Context context, int gravity, View anchorView, View contentView, int xOff, int yOff) {
        int windowPos[] = new int[2];
        int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        // 获取屏幕的宽高
        int[] screenSize = getScreenSize(context);
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的宽高
        int windowWidth = contentView.getMeasuredWidth();
        int windowHeight = contentView.getMeasuredHeight();
        // 判断需要向上弹出还是向下弹出显示
        if ((gravity & Gravity.LEFT) == Gravity.LEFT || (gravity & Gravity.START) == Gravity.START) {
            windowPos[0] = 0;
            windowPos[0] = windowPos[0] + xOff;
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT || (gravity & Gravity.END) == Gravity.END) {
            windowPos[0] = screenSize[0] - windowWidth;
            windowPos[0] = windowPos[0] - xOff;
        } else {
            if (screenSize[0] - anchorLoc[0] - anchorView.getWidth() < windowWidth) {//右方距离不够显示弹窗
                windowPos[0] = anchorLoc[0] + anchorView.getWidth() - windowWidth;
                windowPos[0] = windowPos[0] - yOff;
            } else {
                windowPos[0] = anchorLoc[0];
                windowPos[0] = windowPos[0] + xOff;
            }
        }
        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            //如果使用沉侵式则windowPos[1]=0，这里锚点应该为状态栏高度的坐标
            windowPos[1] = getStatusBarHeight(context);
            windowPos[1] = windowPos[1] + yOff;
        } else if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            windowPos[1] = screenSize[1] - windowHeight;
            windowPos[1] = windowPos[1] - yOff;
        } else {
            if (screenSize[1] - anchorLoc[1] - anchorView.getHeight() < windowHeight) {//下方距离不够显示弹窗
                windowPos[1] = anchorLoc[1] - windowHeight;
                windowPos[1] = windowPos[1] - yOff;
            } else {
                windowPos[1] = anchorLoc[1] + anchorView.getHeight();
                windowPos[1] = windowPos[1] + yOff;
            }
        }
        return windowPos;
    }

    //获取屏幕宽高
    public static int[] getScreenSize(Context context) {
        Resources mResources;
        if (context == null) {
            mResources = Resources.getSystem();
        } else {
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return new int[]{mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels};
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
