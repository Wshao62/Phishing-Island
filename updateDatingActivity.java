package com.ponggan.phishing;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;


public class updateDatingActivity extends Activity {

    String date,content;
    int targetID;
    EditText etDate,etContent;
    Button btnDone, btnCancel;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    SQLiteDatabase db;
    Spinner spinnerTarget;
    Cursor cursorSpinner;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dating);

        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        db = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        //初始化物件們
        etDate = (EditText)findViewById(R.id.editDate);
        etContent = (EditText)findViewById(R.id.editContent);
        spinnerTarget = (Spinner)findViewById(R.id.spinnerTarget);
        //取得intent中的bundle物件
        Bundle bundle = getIntent().getExtras();
        //取得bundle拿來的資料
        id = bundle.getInt("id");
        //填上去
        etDate.setText(bundle.getString("date"));
        etContent.setText(bundle.getString("content"));
        //把DB的資料導入下拉式選單
        cursorSpinner =db.rawQuery("SELECT _id,name from Files ORDER BY _id", null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.spinner_target ,cursorSpinner, new String[] { "_id", "name" },
                new int[] {R.id.text_target_id, R.id.text_target_name});
        spinnerTarget.setAdapter(adapter);
        //設定下拉式選單的預設值
        spinnerTarget.setSelection(bundle.getInt("target_id") - 1);
        //不讓人點選etDate
        etDate.setEnabled(false);
        etDate.setClickable(false);
        //不讓人點選spinner
        spinnerTarget.setClickable(false);
        spinnerTarget.setEnabled(false);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                date = etDate.getText().toString();
                content = etContent.getText().toString();
                //在Database裡面Update指定目標的資料
                dbHandler.updateDating(id, date, content, targetID);
                updateDatingActivity.this.finish();
            }
        });

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                updateDatingActivity.this.finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_dating, menu);
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
