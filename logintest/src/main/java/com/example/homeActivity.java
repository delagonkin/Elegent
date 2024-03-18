package com.example;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.logintest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    private Gson gson=new Gson();
    private OkHttpClient okHttpClient=new OkHttpClient();
    private String TAG="DelagonKing";
    private String token;
    private LinearLayout llhome,llapps,llnews,llperson;
    private ImageView ivhome,ivapps,ivnews,ivperson,ivcurrnet;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences preferences=getSharedPreferences("MainActivity",MODE_PRIVATE);
        token = preferences.getString("token","");
        getlbt();
        gethotnews();
        getlist();
        gettype();

        LayoutInflater lf=getLayoutInflater().from(this);
        View home=lf.inflate(R.layout.layout_home,null);
        View servers = lf.inflate(R.layout.layout_servers,null);
        View news = lf.inflate(R.layout.layout_news, null);
        View person = lf.inflate(R.layout.layout_person, null);
        List<View> views=new ArrayList<>();
        views.add(home);
        views.add(servers);
        views.add(news);
        views.add(person);
        viewPager = findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdpter(views));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        llhome = findViewById(R.id.id_tab_home);
        llhome.setOnClickListener(this);
        llapps = findViewById(R.id.id_tab_apps);
        llapps.setOnClickListener(this);
        llnews = findViewById(R.id.id_tab_news);
        llnews.setOnClickListener(this);
        llperson = findViewById(R.id.id_tab_person);
        llperson.setOnClickListener(this);
        ivhome=findViewById(R.id.tab_id_home);
        ivapps=findViewById(R.id.tab_id_apps);
        ivnews=findViewById(R.id.tab_id_news);
        ivperson=findViewById(R.id.tab_id_person);
        ivcurrnet=ivhome;
        ivhome.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        changeTab(view.getId());
    }

    private void changeTab(int id) {
        ivcurrnet.setSelected(false);
        if (id==R.id.id_tab_home||id==0){
            ivhome.setSelected(true);
            ivcurrnet=ivhome;
            viewPager.setCurrentItem(0);
        }else if (id==R.id.id_tab_apps||id==1){
            ivapps.setSelected(true);
            ivcurrnet=ivapps;
            viewPager.setCurrentItem(1);
        }else if (id==R.id.id_tab_news||id==2){
            ivnews.setSelected(true);
            ivcurrnet=ivnews;
            viewPager.setCurrentItem(2);
        }else if (id==R.id.id_tab_person||id==3){
            ivperson.setSelected(true);
            ivcurrnet=ivperson;
            viewPager.setCurrentItem(3);
        }
    }

    private void getnews(String url) {
        Log.d(TAG, "getnews: "+url);
        Request request=new Request.Builder()
                .header("Authorization",token)
                .url("http://124.93.196.45:10001"+url)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> map=gson.fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                List<Map<String,Object>> res=(List<Map<String,Object>>)map.get("rows");
                for (int i = 0; i < res.size(); i++) {
                    if(res.get(i).get("cover")!=null){
                        URL url=new URL("http://124.93.196.45:10001"+res.get(i).get("cover"));
                        Bitmap bitmap=BitmapFactory.decodeStream(url.openStream());
                        res.get(i).put("bitmap",bitmap);
                    }else{
                        URL url=new URL("http://124.93.196.45:10001"+res.get(1).get("cover"));
                        Bitmap bitmap=BitmapFactory.decodeStream(url.openStream());
                        res.get(i).put("bitmap",bitmap);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+map);
                        ListView listView=findViewById(R.id.newslv);
                           newsAdapter newsAdapter=new newsAdapter(res, homeActivity.this);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                              res.size()*390);
                           listView.setLayoutParams(param);
                           listView.setAdapter(newsAdapter);
                           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                               @Override
                               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                   gptnews(res.get(i).get("id").toString());
                               }
                           });
                    }
                });
            }
        });
    }

    private void gethotnews() {
        Request request=new Request.Builder()
                .header("Authorization",token)
                .url("http://124.93.196.45:10001"+"/prod-api/press/press/list")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> res=gson.fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                List<Map<String,Object>> list=(List<Map<String,Object>>) res.get("rows");
                String tv1=list.get(0).get("title").toString();
                String tv2=list.get(1).get("title").toString();
                URL url=new URL("http://124.93.196.45:10001"+list.get(0).get("cover"));
                URL url1=new URL("http://124.93.196.45:10001"+list.get(1).get("cover"));
                Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
                Bitmap bitmap1= BitmapFactory.decodeStream(url1.openStream());
                list.get(0).put("bitmap",bitmap);
                list.get(1).put("bitmap",bitmap1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView im1 = findViewById(R.id.hotimg1);
                        ImageView im2=findViewById(R.id.hotimg2);
                        im1.setImageBitmap(bitmap);
                        im2.setImageBitmap(bitmap1);
                        TextView hottv1 = findViewById(R.id.hottv1);
                        TextView hottv2 = findViewById(R.id.hottv2);
                        hottv1.setText(tv1);
                        hottv2.setText(tv2);
                        LinearLayout layout = findViewById(R.id.hotnews1);
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gptnews(list.get(0).get("id").toString());
                            }
                        });
                        LinearLayout layout2 = findViewById(R.id.hotnews2);
                        layout2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gptnews(list.get(1).get("id").toString());
                            }
                        });
                    }
                });
            }
        });
    }

    private View.OnClickListener gptnews(String id) {
        SharedPreferences.Editor editor=getSharedPreferences("news",MODE_PRIVATE).edit();
        editor.putString("id",id);
        editor.apply();
        Intent intent=new Intent(homeActivity.this,NewsActivity.class);
        startActivity(intent);
        return null;
    }

    private void gettype() {
        Request request=new Request.Builder()
                .addHeader("Authorization",token)
                .url("http://124.93.196.45:10001"+"/prod-api/press/category/list").build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                List<Map<String,Object>> res=(List<Map<String,Object>>) map.get("data");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Map<String,Object>> typelist=new ArrayList<>();
                        typelist=res;
                        LinearLayout scrollView = findViewById(R.id.scv);
                        for (int i = 0; i < typelist.size(); i++) {
                            TextView tv=new TextView(homeActivity.this);
                            tv.setClickable(true);
                            tv.setId(i);
                            List<Map<String, Object>> finalTypelist = typelist;
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   currenttype(finalTypelist,tv.getId());
                                }
                            });
                            tv.setText(typelist.get(i).get("name").toString());
                            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(10,10,10,10);
                            tv.setLayoutParams(lp);
                            scrollView.addView(tv);
                        }
                        currenttype(typelist,0);
                    }
                });
            }
        });

    }

    private void currenttype(List<Map<String,Object>> list,int id) {
        for (int i = 0; i <list.size() ; i++) {
            if(i==id){
                TextView tv = findViewById(i);
                tv.setTextColor(getColor(R.color.blue));
                ListView lv = findViewById(R.id.newslv);
                this.getnews("/prod-api/press/press/list?type="+Double.valueOf(String.valueOf(list.get(i).get("id"))).intValue());
            }else{
                TextView tv = findViewById(i);
                tv.setTextColor(getColor(R.color.gray));
            }
        }
    }

    private void getlist() {
        Request request=new Request.Builder()
                .header("Authorization",token)
                .url("http://124.93.196.45:10001"+"/prod-api/api/service/list").build();
        Call call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                List<Map<String,Object>> res2=new ArrayList<>();
                List<Bitmap> bitmaps=new ArrayList<>();
                List<String> texts=new ArrayList<>();
                res2=(List<Map<String,Object>>)map.get("rows");
                for (int i = 0; i < res2.size(); i++) {
                    URL url=new URL("http://124.93.196.45:10001"+res2.get(i).get("imgUrl"));
                    Bitmap bitmap=BitmapFactory.decodeStream(url.openStream());
                    bitmaps.add(bitmap);
                }
                for (int i = 0; i < res2.size(); i++) {
                    texts.add(res2.get(i).get("serviceName").toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Bitmap> bitmapList=new ArrayList<>();
                        List<String> textlist=new ArrayList<>();
                        textlist=texts;
                        bitmapList=bitmaps;
                        ImageView im1 = findViewById(R.id.ser1);
                        TextView tv1 = findViewById(R.id.tx1);
                        im1.setImageBitmap(bitmapList.get(0));
                        tv1.setText(texts.get(0));

                        ImageView im2 = findViewById(R.id.ser2);
                        TextView tv2 = findViewById(R.id.tx2);
                        im2.setImageBitmap(bitmapList.get(1));
                        tv2.setText(texts.get(1));

                        ImageView im3 = findViewById(R.id.ser3);
                        TextView tv3 = findViewById(R.id.tx3);
                        im3.setImageBitmap(bitmapList.get(2));
                        tv3.setText(texts.get(2));

                        ImageView im4 = findViewById(R.id.ser4);
                        TextView tv4 = findViewById(R.id.tx4);
                        im4.setImageBitmap(bitmapList.get(3));
                        tv4.setText(texts.get(3));

                        ImageView im5 = findViewById(R.id.ser5);
                        TextView tv5 = findViewById(R.id.tx5);
                        im5.setImageBitmap(bitmapList.get(4));
                        tv5.setText(texts.get(4));

                        ImageView im7 = findViewById(R.id.ser7);
                        TextView tv7 = findViewById(R.id.tx7);
                        im7.setImageBitmap(bitmapList.get(6));
                        tv7.setText(texts.get(6));

                        ImageView im8 = findViewById(R.id.ser8);
                        TextView tv8 = findViewById(R.id.tx8);
                        im8.setImageBitmap(bitmapList.get(7));
                        tv8.setText(texts.get(7));

                        ImageView im6 = findViewById(R.id.ser6);
                        TextView tv6 = findViewById(R.id.tx6);
                        im6.setImageBitmap(bitmapList.get(5));
                        tv6.setText(texts.get(5));
                    }
                });
            }
        });
    }

    private void getlbt() {
        Request request=new Request.Builder().url("http://124.93.196.45:10001"+"/prod-api/api/rotation/list").build();
        Call call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                List<Map<String,Object>> res2=new ArrayList<>();
                res2=(List<Map<String,Object>>)map.get("rows");
                URL url=new URL("http://124.93.196.45:10001"+res2.get(0).get("advImg"));
                URL url2=new URL("http://124.93.196.45:10001"+res2.get(1).get("advImg"));
                Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
                Bitmap bitmap2= BitmapFactory.decodeStream(url2.openStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView img1 = findViewById(R.id.imageView1);
                        ImageView img2 = findViewById(R.id.imageView2);
                        img1.setImageBitmap(bitmap);
                        img2.setImageBitmap(bitmap2);
                        ViewFlipper viewFlipper=findViewById(R.id.flipper);
                        viewFlipper.startFlipping();
                    }
                });
            }
        });
    }


}