package com.example.a10_module_c;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditText from;
    private EditText to;
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        SharedPreferences sharedPreferences=getSharedPreferences("fromto",MODE_PRIVATE);
        from.setText(sharedPreferences.getString("from",""));
        to.setText(sharedPreferences.getString("to",""));
         date= findViewById(R.id.date);
        date.setOnClickListener(this);
        CheckBox kid = findViewById(R.id.kid);
        CheckBox baby = findViewById(R.id.baby);
        kid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kid.isChecked()){
                    baby.setChecked(false);
                }
            }
        });
        baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (baby.isChecked()){
                    kid.setChecked(false);
                }
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.getText().toString().trim().equals("")||from.getText()==null
                ||to.getText().toString().equals("")||to.getText()==null||date.getText()==null){
                    Toast.makeText(HomeActivity.this,"请补充完整信息",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor=getSharedPreferences("fromto",MODE_PRIVATE).edit();
                    editor.putString("date",date.getText().toString());
                    editor.apply();
                    startActivity(new Intent(HomeActivity.this,SearchActivity.class));
                }
            }
        });
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor=getSharedPreferences("fromto",MODE_PRIVATE).edit();
                editor.putString("from",from.getText().toString());
                editor.apply();
            }
        });
        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SharedPreferences.Editor editor=getSharedPreferences("fromto",MODE_PRIVATE).edit();
                editor.putString("to",from.getText().toString());
                editor.apply();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(this,this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date.setText(datePicker.getYear()+"年"+datePicker.getMonth()+"月"+datePicker.getDayOfMonth()+"日");
    }
}