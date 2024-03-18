package com.example.padtest;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;
    private  float x=0;
    private  float currentx=0;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawlayout);;
        LinearLayout primary = findViewById(R.id.primary);
        RelativeLayout left = findViewById(R.id.left_menu);
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(left);
            }
        });
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    x=motionEvent.getX();
                } else if (motionEvent.getAction()==motionEvent.ACTION_MOVE) {
                    currentx=motionEvent.getX();
                } else if (motionEvent.getAction()==motionEvent.ACTION_UP) {
                    if(currentx-x>200){
                        drawerLayout.openDrawer(left);
                    }
                }
                return false;
            }
        });
    }

}