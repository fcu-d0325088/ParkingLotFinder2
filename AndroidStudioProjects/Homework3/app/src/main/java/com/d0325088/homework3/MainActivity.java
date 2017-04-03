package com.d0325088.homework3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button helloBtn;
    private static final int ACTIVITY_INPUT_NAME = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloBtn = (Button)findViewById(R.id.helloBtn);

        helloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, InputNameActivity.class);
                startActivityForResult(intent,ACTIVITY_INPUT_NAME);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(intent == null){
            return;
        }

        String name;

        super.onActivityResult(requestCode,resultCode,intent);
        switch(requestCode){
            case ACTIVITY_INPUT_NAME:
                name = intent.getStringExtra("Name");
                Toast toast = Toast.makeText(MainActivity.this, "Hello " + name,Toast.LENGTH_SHORT);
                toast.show();
        }
    }
}
