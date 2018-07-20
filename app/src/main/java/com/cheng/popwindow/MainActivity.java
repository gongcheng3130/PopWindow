package com.cheng.popwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv_down_left, tv_down_right, tv_down_left_off_right, tv_down_right_off_left, tv_up_left, tv_up_right, tv_up_left_off_right
            , tv_up_right_off_left, tv_normal_1, tv_normal_2, tv_normal_3, tv_normal_4, tv_top, tv_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_down_left:
                showPopWindow(tv_down_left, Gravity.LEFT, 0, 0);
                break;
            case R.id.tv_down_right:
                showPopWindow(tv_down_right, Gravity.RIGHT, 0, 0);
                break;
            case R.id.tv_down_left_off_right:
                showPopWindow(tv_down_left_off_right, Gravity.LEFT, 20, 20);
                break;
            case R.id.tv_down_right_off_left:
                showPopWindow(tv_down_right_off_left, Gravity.RIGHT, 20, 20);
                break;
            case R.id.tv_up_left:
                showPopWindow(tv_up_left, Gravity.LEFT, 0, 0);
                break;
            case R.id.tv_up_right:
                showPopWindow(tv_up_right, Gravity.RIGHT, 0, 0);
                break;
            case R.id.tv_up_left_off_right:
                showPopWindow(tv_up_left_off_right, Gravity.LEFT, 20, 20);
                break;
            case R.id.tv_up_right_off_left:
                showPopWindow(tv_up_right_off_left, Gravity.RIGHT, 20, 20);
                break;
            case R.id.tv_normal_1:
                showPopWindow(tv_normal_1, Gravity.NO_GRAVITY, 0, 0);
                break;
            case R.id.tv_normal_2:
                showPopWindow(tv_normal_2, Gravity.NO_GRAVITY, 0, 0);
                break;
            case R.id.tv_normal_3:
                showPopWindow(tv_normal_3, Gravity.NO_GRAVITY, 0, 0);
                break;
            case R.id.tv_normal_4:
                showPopWindow(tv_normal_4, Gravity.NO_GRAVITY, 0, 0);
                break;
            case R.id.tv_top:
                showPopWindow(tv_top, Gravity.TOP | Gravity.RIGHT, 0, 0);
                break;
            case R.id.tv_bottom:
                showPopWindow(tv_bottom, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
                break;
        }
    }

    private void showPopWindow(View anchor, int gravity, int xOff, int yOff){
        View contentView = getContentView();
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(this, gravity, anchor, contentView, xOff, yOff);
        //后面两个参数默认是靠边显示，可以自己加减数值调整显示边距
        popWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);
    }

    private View getContentView(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popwindow, null);
        ListView popwindow_lv = view.findViewById(R.id.popwindow_lv);
        SimpleAdapter adapter = new SimpleAdapter(this, getData());
        popwindow_lv.setAdapter(adapter);
        return view;
    }

    private List<bean> getData(){
        List<bean> lists = new ArrayList<>();
        lists.add(new bean(R.mipmap.icon_contact_white, "联系客服"));
        lists.add(new bean(R.mipmap.icon_explain_white, "获取帮助"));
        lists.add(new bean(R.mipmap.icon_eyes_hide, "隐藏订单"));
        lists.add(new bean(R.mipmap.icon_eyes_visi, "显示订单"));
        lists.add(new bean(R.mipmap.ic_launcher, "其他"));
        return lists;
    }

    private void initView(){
        tv_down_right = findViewById(R.id.tv_down_right);
        tv_down_right.setOnClickListener(this);
        tv_down_left = findViewById(R.id.tv_down_left);
        tv_down_left.setOnClickListener(this);
        tv_down_left_off_right = findViewById(R.id.tv_down_left_off_right);
        tv_down_left_off_right.setOnClickListener(this);
        tv_down_right_off_left = findViewById(R.id.tv_down_right_off_left);
        tv_down_right_off_left.setOnClickListener(this);
        tv_up_left = findViewById(R.id.tv_up_left);
        tv_up_left.setOnClickListener(this);
        tv_up_right = findViewById(R.id.tv_up_right);
        tv_up_right.setOnClickListener(this);
        tv_up_left_off_right = findViewById(R.id.tv_up_left_off_right);
        tv_up_left_off_right.setOnClickListener(this);
        tv_up_right_off_left = findViewById(R.id.tv_up_right_off_left);
        tv_up_right_off_left.setOnClickListener(this);
        tv_normal_1 = findViewById(R.id.tv_normal_1);
        tv_normal_1.setOnClickListener(this);
        tv_normal_2 = findViewById(R.id.tv_normal_2);
        tv_normal_2.setOnClickListener(this);
        tv_normal_3 = findViewById(R.id.tv_normal_3);
        tv_normal_3.setOnClickListener(this);
        tv_normal_4 = findViewById(R.id.tv_normal_4);
        tv_normal_4.setOnClickListener(this);
        tv_top = findViewById(R.id.tv_top);
        tv_top.setOnClickListener(this);
        tv_bottom = findViewById(R.id.tv_bottom);
        tv_bottom.setOnClickListener(this);
    }

}
