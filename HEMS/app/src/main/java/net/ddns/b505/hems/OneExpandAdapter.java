 package net.ddns.b505.hems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class OneExpandAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> list;
    private int currentItem = -1; //用於紀錄點擊的Item的position。為控制item展開的核心

    public OneExpandAdapter(Context context,
                            ArrayList<HashMap<String, String>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_dr_history, parent, false);
            holder = new ViewHolder();
            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.tv_history_date = (TextView) convertView
                    .findViewById(R.id.tv_history_date);
            holder.tv_history_start = (TextView) convertView
                    .findViewById(R.id.tv_history_start);
            holder.tv_history_stop = (TextView) convertView
                    .findViewById(R.id.tv_history_stop);
            holder.tv_history_p1 = (TextView) convertView
                    .findViewById(R.id.tv_history_p);
            holder.tv_history_time = (TextView) convertView
                    .findViewById(R.id.tv_history_time);
            holder.hideArea = (LinearLayout) convertView.findViewById(R.id.layout_hideArea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }




        HashMap<String, String> item = list.get(position);
        //注意：我們在此給響應點擊事件的區域 (目前以 showArea 的線性布局) 加入Tag。
        // 為了紀錄點擊的position。使用position設置Tag
        holder.showArea.setTag(position);
        holder.tv_history_date.setText(item.get("history_date"));
        holder.tv_history_start.setText(item.get("history_start"));
        holder.tv_history_stop.setText(item.get("history_stop"));
        holder.tv_history_p1.setText(item.get("history_p1"));
        holder.tv_history_time.setText(item.get("history_time"));
        //holder.tv_history_p2.setText(item.get("history_p2"));
        //holder.tv_history_result.setText(item.get("history_result"));
        //holder.tv_history_cost.setText(item.get("history_cost"));
        //holder.tv_history_cost2.setText(item.get("history_cost2"));
        //holder.tv_history_p3.setText(item.get("history_p3"));

        //根據 currentItem 紀錄的剪輯位置來設置"相應Item"的可見姓
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
        } else {
            holder.hideArea.setVisibility(View.GONE);
        }

        holder.showArea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //用 currentItem 紀錄點擊位置
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次點擊
                    currentItem = -1; //给 currentItem 一個無效值
                } else {
                    currentItem = tag;
                }
                //通知adapter數據改變需要再一次載入
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private LinearLayout showArea;

        private TextView
                tv_history_date, tv_history_start, tv_history_stop,
                tv_history_p1, tv_history_time, tv_history_cost,
                tv_history_p2, tv_history_p3, tv_history_result,
                tv_history_cost2;

        private LinearLayout hideArea;
    }
}