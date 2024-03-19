package com.example.datepickeractivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private DatePicker dpdate;
    private  TextView tv_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_date).setOnClickListener(this);
         dpdate = findViewById(R.id.dp_date);
         tv_date = findViewById(R.id.tv_date);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_ok){
            String desc= String.format("您选择的日期是:%d年%d月%d日",dpdate.getYear(),dpdate.getMonth()+1,dpdate.getDayOfMonth());
            tv_date.setText(desc);
        } else if (view.getId()==R.id.btn_date) {
            //获取日历一个实例，里面包含了当前的年月日
            Calendar calendar = Calendar.getInstance();
//            calendar.get(Calendar.YEAR);
//            calendar.get(Calendar.MONTH);
//            calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog=new DatePickerDialog(this,this,2021,5,11);
            //显示日期对话框
            dialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
        String desc= String.format("您选择的日期是:%d年%d月%d日",year,month,dayofMonth);
        tv_date.setText(desc);
    }
}