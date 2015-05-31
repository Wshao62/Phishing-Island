package com.ponggan.phishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class QuestionnaireActivity extends Activity {

    TextView textViewQuestion1,textViewQuestion2,textViewQuestion3,textViewQuestion4,textViewQuestion5;
    ToggleButton toggleButtonQ1,toggleButtonQ2,toggleButtonQ3,toggleButtonQ4,toggleButtonQ5;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    Button btnNext, btnCancel;
    int scoreSelf = 0;
    String date, content;
    int target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        dbHandler.initializeQuestion();

        //取得intent中的bundle物件
        Bundle bundle = getIntent().getExtras();
        //取得bundle拿來的資料填上變數，才能繼續傳送給下一步
        date = bundle.getString("date");
        content = bundle.getString("content");
        target = bundle.getInt("target");

        //隨機決定這次要出的題號，且不能重複，一一放入Array中
        final ArrayList<Integer> integerList = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i <5; i++){
            int randomInt = random.nextInt(5) + 1;
            if (!integerList.contains(randomInt)) {
                integerList.add(randomInt);
            }
            else{
                i--;
            }
        }
        //開始出題！還有具體化toggle button
        textViewQuestion1 = (TextView)findViewById(R.id.textViewQuestion1);
        textViewQuestion1.setText(dbHandler.getQuestion(integerList.get(0), "self"));
        toggleButtonQ1 = (ToggleButton)findViewById(R.id.toggleButtonQ1);

        textViewQuestion2 = (TextView)findViewById(R.id.textViewQuestion2);
        textViewQuestion2.setText(dbHandler.getQuestion(integerList.get(1), "self"));
        toggleButtonQ2 = (ToggleButton)findViewById(R.id.toggleButtonQ2);

        textViewQuestion3 = (TextView)findViewById(R.id.textViewQuestion3);
        textViewQuestion3.setText(dbHandler.getQuestion(integerList.get(2), "self"));
        toggleButtonQ3 = (ToggleButton)findViewById(R.id.toggleButtonQ3);

        textViewQuestion4 = (TextView)findViewById(R.id.textViewQuestion4);
        textViewQuestion4.setText(dbHandler.getQuestion(integerList.get(3), "self"));
        toggleButtonQ4 = (ToggleButton)findViewById(R.id.toggleButtonQ4);

        textViewQuestion5 = (TextView)findViewById(R.id.textViewQuestion5);
        textViewQuestion5.setText(dbHandler.getQuestion(integerList.get(4), "self"));
        toggleButtonQ5 = (ToggleButton)findViewById(R.id.toggleButtonQ5);

        btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //new一個intent物件，並指定Activity切換的class
                Intent intentNext = new Intent();
                intentNext.setClass(QuestionnaireActivity.this, QuestionnaireActivityP2.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleNext = new Bundle();
                if(toggleButtonQ1.isChecked())
                    scoreSelf += 20;
                if(toggleButtonQ2.isChecked())
                    scoreSelf += 20;
                if(toggleButtonQ3.isChecked())
                    scoreSelf += 20;
                if(toggleButtonQ4.isChecked())
                    scoreSelf += 20;
                if(toggleButtonQ5.isChecked())
                    scoreSelf += 20;
                //把值放入bundle
                bundleNext.putString("date", date);
                bundleNext.putString("content", content);
                bundleNext.putInt("scoreSelf", scoreSelf);
                bundleNext.putInt("target", target);
                bundleNext.putInt("selfQuestion", integerList.get(0));
                //將Bundle物件傳給intent
                intentNext.putExtras(bundleNext);
                //切換Activity
                startActivity(intentNext);
                QuestionnaireActivity.this.finish();
            }
        });

        //取消按鈕
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                QuestionnaireActivity.this.finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_questionnaire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
