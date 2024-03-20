package com.example.a10_module_c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    List<Map<String,Object>> maps=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ListView lists = findViewById(R.id.list);
        TextView from = findViewById(R.id.from2);
        TextView to = findViewById(R.id.to2);
        TextView date=findViewById(R.id.date2);
        ImageView pimg = findViewById(R.id.priceimg);
        ImageView timg = findViewById(R.id.timeimg);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,HomeActivity.class));
            }
        });
        SharedPreferences sharedPreferences=getSharedPreferences("fromto",MODE_PRIVATE);
        from.setText(sharedPreferences.getString("from",""));
        to.setText(sharedPreferences.getString("to",""));
        date.setText(sharedPreferences.getString("date",""));

        for (int i = 0; i <10 ; i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("starttime",8+i+":00");
            map.put("price",600+i);
            maps.add(map);
        }

        findViewById(R.id.pricesort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timg.setSelected(false);
                if(pimg.isSelected()==true){
                    pimg.setSelected(!pimg.isSelected());
                    Collections.sort(maps, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> m1, Map<String, Object> t1) {
                            if(Double.valueOf(String.valueOf(m1.get("price")))>Double.valueOf(String.valueOf(t1.get("price"))))
                                return 1;
                            else if (Double.valueOf(String.valueOf(t1.get("price")))>Double.valueOf(String.valueOf(m1.get("price"))))
                                return -1;
                            else
                                return 0;
                        }
                    });
                    getlist();
                }else{
                    pimg.setSelected(!pimg.isSelected());
                    Collections.sort(maps, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> m1, Map<String, Object> t1) {
                            if(Double.valueOf(String.valueOf(m1.get("price")))>Double.valueOf(String.valueOf(t1.get("price"))))
                                return -1;
                            else if (Double.valueOf(String.valueOf(t1.get("price")))>Double.valueOf(String.valueOf(m1.get("price"))))
                                return 1;
                            else
                                return 0;
                        }
                    });
                    getlist();
                }
            }
        });

        findViewById(R.id.timesort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pimg.setSelected(false);
                if(timg.isSelected()==true){
                    timg.setSelected(!timg.isSelected());
                    Collections.sort(maps, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> m1, Map<String, Object> t1) {
                            if(Double.valueOf(String.valueOf(m1.get("starttime")).replace(":",""))>Double.valueOf(String.valueOf(t1.get("starttime")).replace(":","")))
                                return 1;
                            else if (Double.valueOf(String.valueOf(t1.get("starttime")).replace(":",""))>Double.valueOf(String.valueOf(m1.get("starttime")).replace(":","")))
                                return -1;
                            else
                                return 0;
                        }
                    });
                    getlist();
                }else{
                    timg.setSelected(!timg.isSelected());
                    Collections.sort(maps, new Comparator<Map<String, Object>>() {
                        @Override
                        public int compare(Map<String, Object> m1, Map<String, Object> t1) {
                            if(Integer.valueOf(String.valueOf(m1.get("starttime")).replace(":",""))>Integer.valueOf(String.valueOf(t1.get("starttime")).replace(":","")))
                                return -1;
                            else if (Integer.valueOf(String.valueOf(t1.get("starttime")).replace(":",""))>Integer.valueOf(String.valueOf(m1.get("starttime")).replace(":","")))
                                return 1;
                            else
                                return 0;
                        }
                    });
                    getlist();
                }
            }
        });
        getlist();
    }

    private void getlist() {
        ListView lv = findViewById(R.id.list);
        lv.setAdapter(new listAdpter(maps,SearchActivity.this));
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,maps.size()*360);
        lv.setLayoutParams(params);
    }
}