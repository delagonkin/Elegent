package com.example;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logintest.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et1;
    private EditText et2;
    private Intent intent;
    private static OkHttpClient okHttpClient=new OkHttpClient();
    private Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences=getSharedPreferences("MainActivity",MODE_PRIVATE);
         if(!preferences.getString("token","").equals("")){
            intent=new Intent(this,homeActivity.class);
            startActivity(intent);
         }
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        findViewById(R.id.btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(et1.getText().toString()==null||et1.getText().toString().trim().equals("")||
                et2.getText().toString()==null||et2.getText().toString().trim().equals("")){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            JSONObject json=new JSONObject();
            try {
                json.put("username",et1.getText().toString());
                json.put("password",et2.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
            Request request=new Request.Builder().url("http://124.93.196.45:10001"+"/prod-api/api/login").post(requestBody).build();
            ContentProviderOperation okHttpClient = null;
            Call call=new OkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                    if(map.get("msg").equals("操作成功")){
                        SharedPreferences.Editor editor=getSharedPreferences("MainActivity",MODE_PRIVATE).edit();
                        editor.putString("token", (String) map.get("token"));
                        editor.apply();
                        intent=new Intent(MainActivity.this,homeActivity.class);
                        startActivity(intent);
                    }else {
                        System.out.println("登录失败请重新登录");
                    }
                }
            });

        }
    }

}