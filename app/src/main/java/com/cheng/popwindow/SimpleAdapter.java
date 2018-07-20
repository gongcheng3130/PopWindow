package com.cheng.popwindow;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class SimpleAdapter extends CommonAdapter<bean> {

    public SimpleAdapter(Context context, List<bean> lists) {
        super(context, lists);
    }

    @Override
    public int getLayout(int position) {
        return R.layout.item_popwindow;
    }

    @Override
    public void setLayoutInfo(BaseViewHolder holder, int position) {
        holder.<ImageView>getView(R.id.popwindow_iv_icon).setImageResource(lists.get(position).resouce);
        holder.<TextView>getView(R.id.popwindow_tv_title).setText(lists.get(position).title);
    }

    @Override
    public void setLayoutClick(BaseViewHolder holder, int position) {

    }

}
