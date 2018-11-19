package com.market.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.market.R;
import com.market.vo.Good;

import java.util.List;

public class CustomerGoodListAdapter extends BaseAdapter {

    private List<Good> data;

    private LayoutInflater mInflater;

    private Context context;

    public CustomerGoodListAdapter(List data, Context context) {
        this.data = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public class ViewHolder {
        TextView nameText;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.customer_good_listview, null);
            holder.nameText = convertView.findViewById(R.id.cart_good_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        holder.nameText.setText(data.get(position).getGoodName());

        return convertView;

    }
}
