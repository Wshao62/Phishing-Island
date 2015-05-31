package com.ponggan.phishing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class RuleActivity extends Activity {
    Button btnClothes,btnAvoid,btnGift,btnPlace,btnTalk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        btnClothes= (Button)findViewById(R.id.btnClothes);
        btnAvoid= (Button)findViewById(R.id.btnAvoid);
        btnGift= (Button)findViewById(R.id.btnGift);
        btnPlace= (Button)findViewById(R.id.btnPlace);
        btnTalk= (Button)findViewById(R.id.btnTalk);

        btnClothes.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentClothes = new Intent();
                intentClothes.setClass(RuleActivity.this, RuleListActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleClothes = new Bundle();
                bundleClothes.putString("type",btnClothes.getText().toString());
                //將Bundle物件傳給intent
                intentClothes.putExtras(bundleClothes);
                //切換Activity
                startActivity(intentClothes);
            }
        });

        btnAvoid.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentAvoid = new Intent();
                intentAvoid.setClass(RuleActivity.this,RuleListActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleAvoid = new Bundle();
                bundleAvoid.putString("type",btnAvoid.getText().toString());
                //將Bundle物件傳給intent
                intentAvoid.putExtras(bundleAvoid);
                //切換Activity
                startActivity(intentAvoid);
            }
        });

        btnGift.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentGift = new Intent();
                intentGift.setClass(RuleActivity.this,RuleListActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleGift = new Bundle();
                bundleGift.putString("type",btnGift.getText().toString());
                //將Bundle物件傳給intent
                intentGift.putExtras(bundleGift);
                //切換Activity
                startActivity(intentGift);
            }
        });

        btnPlace.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentPlace = new Intent();
                intentPlace.setClass(RuleActivity.this,RuleListActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundlePlace = new Bundle();
                bundlePlace.putString("type",btnPlace.getText().toString());
                //將Bundle物件傳給intent
                intentPlace.putExtras(bundlePlace);
                //切換Activity
                startActivity(intentPlace);
            }
        });

        btnTalk.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intentTalk = new Intent();
                intentTalk.setClass(RuleActivity.this, RuleListActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleTalk = new Bundle();
                bundleTalk.putString("type",btnTalk.getText().toString());
                //將Bundle物件傳給intent
                intentTalk.putExtras(bundleTalk);
                //切換Activity
                startActivity(intentTalk);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rule, menu);
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
