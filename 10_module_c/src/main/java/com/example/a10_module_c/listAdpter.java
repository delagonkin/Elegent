package com.example.a10_module_c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class listAdpter extends BaseAdapter {
    private List<Map<String,Object>> lists;
    private LayoutInflater lf;
    public listAdpter(List<Map<String, Object>> maps, SearchActivity searchActivity) {
        lists=maps;
        lf=LayoutInflater.from(searchActivity);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Map<String, Object> getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            view=lf.inflate(R.layout.item,null);
            view.setTag(new ViewHolder(view));
        }
        inintedView((ViewHolder) view.getTag(),getItem(i));
        return view;
    }

    private void inintedView(ViewHolder views, Map<String,Object> item) {
        views.time.setText(item.get("starttime").toString());
        views.price.setText("¥"+item.get("price").toString());
    }

    private class ViewHolder {
        TextView time;
        TextView price;
        public ViewHolder(View view) {
            time=view.findViewById(R.id.starttime);
            price=view.findViewById(R.id.price);
        }
    }
}
