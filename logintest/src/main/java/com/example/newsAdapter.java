package com.example;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.logintest.R;

import java.util.List;
import java.util.Map;

public class newsAdapter extends BaseAdapter {

    private List<Map<String,Object>> list;
    private homeActivity homeActivity;

    private LayoutInflater layoutInflater;
    public newsAdapter(List<Map<String, Object>> res, homeActivity homeActivity) {
        this.homeActivity=homeActivity;
        this.list=res;
        this.layoutInflater=LayoutInflater.from(homeActivity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String,Object> getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=layoutInflater.inflate(R.layout.item, null);
            view.setTag(new Viewholder(view));
        }
        initViews(getItem(i),(Viewholder) view.getTag());
        return view;
    }

    private void initViews(Map<String,Object> data,Viewholder viewholder) {
        viewholder.titleview.setText(data.get("title").toString().length()>=13?data.get("title").toString().replaceAll("[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "").substring(0,14):data.get("title").toString().replaceAll("[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""));
        viewholder.contextview.setText(data.get("content").toString().replaceAll("[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "").substring(0,30));
        viewholder.imageView.setImageBitmap((Bitmap) data.get("bitmap"));
    }

    private  class Viewholder{
        TextView titleview;
        ImageView imageView;
        TextView contextview;
        public Viewholder(View view){
            titleview=view.findViewById(R.id.newstitle);
            imageView=view.findViewById(R.id.newsimg);
            contextview=view.findViewById(R.id.newscontext);
        }
    }
}
