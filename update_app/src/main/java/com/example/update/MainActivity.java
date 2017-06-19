package com.example.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.update.inner.InnerUpdateActivity;
import com.example.update.outer.OuterUpdateActivity;

public class MainActivity extends AppCompatActivity {
    Button btn_inner,btn_outer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_inner=(Button)findViewById(R.id.btn_inner);
        btn_outer=(Button)findViewById(R.id.btn_outer);
        btn_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,InnerUpdateActivity.class));
            }
        });
        btn_outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OuterUpdateActivity.class));
            }
        });
    }
}
