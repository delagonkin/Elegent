package com.example;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        SharedPreferences sharedPreferences=getSharedPreferences("news",MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        Log.d("TAG", "onCreate: "+Double.valueOf(id).intValue());
        getnewsdetail(id);

    }

    private void getnewsdetail(String id) {
        Request request=new Request.Builder()
                .url("http://124.93.196.45:10001/prod-api/press/press/"+Double.valueOf(id).intValue())
                .build();
        Call call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken <Map<String,Object>>(){}.getType());
                Map<String,Object> res =(Map<String, Object>)map.get("data");
                URL url=new URL("http://124.93.196.45:10001/"+res.get("cover"));
                Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView newcontent = findViewById(R.id.contentnews);
                        TextView titlenews = findViewById(R.id.titlenews);
                        TextView likenum = findViewById(R.id.likenum);
                        ImageView imgnews = findViewById(R.id.imgnews);
                        newcontent.setText(res.get("content").toString().replaceAll("[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""));
                        titlenews.setText(res.get("title").toString());
                        likenum.setText("点赞数:"+res.get("likeNum")+"     "+"发布日期:"+res.get("publishDate"));
                        imgnews.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}