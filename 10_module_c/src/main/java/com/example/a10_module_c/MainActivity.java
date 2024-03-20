package com.example.a10_module_c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private EditText username ;
    private EditText password ;
    private CheckBox save;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        save=findViewById(R.id.save);
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
         sharedPreferences=getSharedPreferences("Main",MODE_PRIVATE);
        save.setChecked(false);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=getSharedPreferences("Main",MODE_PRIVATE).edit();
                editor.putBoolean("issave",save.isChecked());
                editor.apply();
            }
        });
        if(sharedPreferences.getBoolean("issave",false)==true){
          username.setText(sharedPreferences.getString("username",""));
          password.setText(sharedPreferences.getString("password",""));
          save.setChecked(true);
        }


        Button login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(username.getText().toString().trim().equals("")||username.getText()==null||
                password.getText().toString().trim().equals("")||password.getText()==null){
            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("username",username.getText());
                jsonObject.put("password",password.getText());
            }catch (Exception e){
                e.printStackTrace();
            }
            MediaType type=MediaType.parse("application/json");

            Request request=new Request.Builder()
                    .post(RequestBody.create(type,jsonObject.toString()))
                    .url("http://124.93.196.45:10001/"+"/prod-api/api/login")
                    .build();
            Call call=new OkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Map<String,Object> map=new Gson().fromJson(response.body().string(),new TypeToken<Map<String,Object>>(){}.getType());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Double.valueOf(String.valueOf(map.get("code"))).intValue()==200){
                                if(sharedPreferences.getBoolean("issave",false)){
                                    SharedPreferences.Editor editor=getSharedPreferences("Main",MODE_PRIVATE).edit();
                                    editor.putString("username",username.getText().toString());
                                    editor.putString("password",password.getText().toString());
                                    editor.apply();
                                }
                                Toast.makeText(MainActivity.this,username.getText()+"欢迎登录",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                                password.setText("");
                            }
                        }
                    });
                }
            });
        }
    }
}