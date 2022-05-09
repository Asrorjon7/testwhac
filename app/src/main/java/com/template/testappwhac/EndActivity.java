package com.template.testappwhac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class EndActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        TextView ScoreCheck = (TextView) findViewById(R.id.mScoreValue);

        Button mSubmit = (Button) findViewById(R.id.button_again);
        Button mMenu = (Button) findViewById(R.id.button_Menu);


        Intent intent = getIntent();
        final int ScoreValue = intent.getExtras().getInt("score");


        if (ScoreCheck != null){
            ScoreCheck.setText(String.valueOf(ScoreValue));
        }
        if (mSubmit != null){
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),GameActivity.class));
                }
            });
        }

        if (mMenu != null){
            mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
