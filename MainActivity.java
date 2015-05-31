package com.ponggan.phishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Space;

public class MainActivity extends Activity {

    ImageButton btnDating,btnFile,btnMessage,btnRule,btnRanking;
    Space spaceUp, spaceDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDating = (ImageButton)findViewById(R.id.btnDating);
        btnFile = (ImageButton)findViewById(R.id.btnFile);
        btnMessage = (ImageButton)findViewById(R.id.btnMessage);
        btnRule = (ImageButton)findViewById(R.id.btnRule);
        btnRanking = (ImageButton)findViewById(R.id.btnRanking);

        spaceUp = (Space)findViewById(R.id.spaceUp);
        spaceDown = (Space)findViewById(R.id.spaceDown);

        spaceUp.setMinimumWidth(btnFile.getWidth());
        spaceUp.setMinimumHeight(btnFile.getHeight());
        spaceDown.setMinimumWidth(btnFile.getWidth());
        spaceDown.setMinimumHeight(btnFile.getHeight());

        btnDating.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentDating = new Intent();
                intentDating.setClass(MainActivity.this, DatingActivity.class);
                startActivity(intentDating);
            }
        });

        btnFile.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentFile = new Intent();
                intentFile.setClass(MainActivity.this, FileActivity.class);
                startActivity(intentFile);
            }
        });

        btnMessage.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentMessage = new Intent();
                intentMessage.setClass(MainActivity.this, MessageActivity.class);
                startActivity(intentMessage);
            }
        });

        btnRule.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentRule = new Intent();
                intentRule.setClass(MainActivity.this, RuleActivity.class);
                startActivity(intentRule);
            }
        });

        btnRanking.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentRanking = new Intent();
                intentRanking.setClass(MainActivity.this, RankingActivity.class);
                startActivity(intentRanking);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
