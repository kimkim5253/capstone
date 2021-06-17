package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{
    private Button TranslateBtn, CameraBtn, ImageBtn;
    private TextView textView;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button CameraBtn = (Button) findViewById(R.id.CameraButton);
        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
            startActivity(intent);
            }
        });
        Button ImageBtn = (Button) findViewById(R.id.ImageButton);
        ImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ImageActivity.class);
                startActivity(intent);
            }
        });

        EditText editText = (EditText) findViewById((R.id.Inputtext));

    }




}