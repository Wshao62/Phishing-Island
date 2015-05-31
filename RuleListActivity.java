package com.ponggan.phishing;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class RuleListActivity extends Activity {
    TextView title,text;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    SQLiteDatabase db;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_list);

        title=(TextView)findViewById(R.id.title);
        text=(TextView)findViewById(R.id.text);

        Bundle bundle = getIntent().getExtras();
        title.setText(bundle.getString("type"));
        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        db = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        cursor=db.rawQuery("SELECT _id,type,data  from Rule WHERE type = '" + bundle.getString("type") + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
            int i = cursor.getColumnIndex("data");
            text.setText(cursor.getString(i));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rule_list, menu);
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