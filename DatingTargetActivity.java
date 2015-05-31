package com.ponggan.phishing;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class DatingTargetActivity extends Activity {

    GridView gridViewTitle;
    public static ArrayList<String> ArrayofTitle = new ArrayList<String>();
    ArrayAdapter<String> adapterTitle;
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    private DatingTargetCursorAdapter datingAdapter;
    ListView listViewDating;
    long targetID;
    TextView textViewTitle;
    com.ponggan.phishing.DatabaseHandler dbHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_target);

        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);

        Bundle bundle = getIntent().getExtras();
        targetID = bundle.getLong("id");

        //標題文字設定
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewTitle.setText("你和" + dbHandler.getDatingTargetName((int)targetID) + "的所有約會" );

        //Grid上面標題
        gridViewTitle = (GridView) findViewById(R.id.gridViewTitle);
        adapterTitle = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ArrayofTitle);
        adapterTitle.clear();
        if(ArrayofTitle.isEmpty()){
            ArrayofTitle.add(new String("約會時間"));
            ArrayofTitle.add(new String("約會內容"));
            ArrayofTitle.add(new String("約會成績"));
        }
        gridViewTitle.setAdapter(adapterTitle);

        //ListView約會內容
        listViewDating = (ListView)findViewById(R.id.listViewDating);

        db= (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        cursor =db.rawQuery("SELECT _id,date,content,scoreSelf,scoreTarget,target_id FROM Datings WHERE target_id = " + targetID + " ORDER BY _id", null);
        if (cursor != null) {
            datingAdapter = new DatingTargetCursorAdapter(getApplicationContext(), cursor);
            listViewDating.setAdapter(datingAdapter);
        }

        db.close();

    }

    protected void onDestroy() {
        super.onDestroy();
        cursor.close();  //我们在onCreate()中没有关闭游标，因为需要和ListView进行数据关联，关闭curosr，会导致List无数据，故在最后释放资源
        db.close(); //断开和数据库的连接，释放相关资源
    }

    //步骤1：通过后台线程AsyncTask来读取数据库，放入更换Cursor
    private class RefreshList extends AsyncTask<Void, Void ,Cursor> {
        //步骤1.1：在后台线程中从数据库读取，返回新的游标newCursor
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor =  db.rawQuery("SELECT _id,date,content,score from Datings ORDER BY _id", null);
            return newCursor;
        }
        //步骤1.2：线程最后执行步骤，更换adapter的游标，并奖原游标关闭，释放资源
        protected void onPostExecute(Cursor newCursor) {
            datingAdapter.changeCursor(newCursor);//网上看到很多问如何更新ListView的信息，采用CusorApater其实很简单，换cursor就可以
            cursor.close();
            cursor = newCursor;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_del_dating, menu);
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
