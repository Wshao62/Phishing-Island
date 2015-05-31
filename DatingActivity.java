package com.ponggan.phishing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class DatingActivity extends Activity {

    GridView gridViewTitle;
    public static ArrayList<String> ArrayofTitle = new ArrayList<String>();
    ArrayAdapter<String> adapterTitle;
    ImageButton btnCreate,btnDel,btnUpdate,btnRead;

    private SQLiteDatabase db = null;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    ListView listview;
    Cursor cursor = null;
    Cursor cursorFunction = null;
    private DatingCursorAdapter datingAdapter;
    private int Function = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating);

        gridViewTitle = (GridView) findViewById(R.id.gridViewTitle);
        //Grid上面標題
        adapterTitle = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, ArrayofTitle);
        adapterTitle.clear();
        if(ArrayofTitle.isEmpty()){
            ArrayofTitle.add(new String("約會時間"));
            ArrayofTitle.add(new String("約會對象"));
            ArrayofTitle.add(new String("約會成績"));
        }
        gridViewTitle.setAdapter(adapterTitle);

        //ListView約會內容
        db= (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        listview = (ListView)findViewById(R.id.listViewDating);

        cursor =db.rawQuery("SELECT _id,date,content,scoreSelf,scoreTarget,target_id from Datings ORDER BY date", null);
        if (cursor != null) {
            datingAdapter = new DatingCursorAdapter(getApplicationContext(), cursor);
            listview.setAdapter(datingAdapter);

            //listview按下後的各種反應
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3){
                    cursorFunction = (Cursor) datingAdapter.getItem(position);
                    cursorFunction.moveToPosition(position);
                    final int rowId = cursorFunction.getInt(cursorFunction.getColumnIndexOrThrow("_id"));
                    //沒有選擇功能時
                    if(Function == 0){
                        Toast.makeText(getApplicationContext(), "你在" +
                                cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("date")) + "時的約會沒有任何反應", Toast.LENGTH_SHORT).show();
                    }
                    //選擇刪除der功能時
                    else if(Function == 1){
                        if(rowId > 0) {
                            new AlertDialog.Builder(DatingActivity.this)
                                    .setTitle("刪除你在" + cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("date")) +
                                            "時跟" + dbHandler.getDatingTargetName(cursorFunction.getInt(cursorFunction.getColumnIndexOrThrow("target_id"))) + "的約會嗎？")
                                    .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dbHandler.deleteDating(rowId); //刪除某次約會
                                            dbHandler.updateFileAvg(rowId); // 更新目標的平均分
                                            Toast.makeText(getApplicationContext(), "你在"
                                                    + cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("date")) +
                                                    "時跟" + dbHandler.getDatingTargetName(cursorFunction.getInt(cursorFunction.getColumnIndexOrThrow("target_id"))) +
                                                    "的約會被刪除了", Toast.LENGTH_SHORT).show();
                                            new RefreshList().execute();  //采用后台方式，当然也可以用crusor.requery()来处理。
                                        }
                                    })
                                    .setNegativeButton("不好", null)
                                    .show();
                        }
                    }

                    //選擇更改der功能時
                    else if(Function == 2){
                        if(rowId > 0) {
                            //new一個intent物件，並指定Activity切換的class
                            Intent intentUpdate = new Intent();
                            intentUpdate.setClass(DatingActivity.this, updateDatingActivity.class);
                            //new一個Bundle物件，並將要傳遞的資料傳入
                            Bundle bundleUpdate = new Bundle();
                            bundleUpdate.putInt("id", rowId);//傳遞Id(Int)
                            bundleUpdate.putString("date", cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("date")));//傳遞Name(String)
                            bundleUpdate.putString("content", cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("content")));//傳遞Figure(String)
                            bundleUpdate.putInt("target_id", cursorFunction.getInt(cursorFunction.getColumnIndexOrThrow("target")));//傳遞Id(Int)
                            //將Bundle物件傳給intent
                            intentUpdate.putExtras(bundleUpdate);
                            //切換Activity
                            startActivity(intentUpdate);
                        }
                    }

                    //選擇查詢der功能時
                    else if(Function == 3){
                        if(rowId > 0) {
                            //把這次約會的內容導入
                            AlertDialog.Builder builder = new AlertDialog.Builder(DatingActivity.this);
                            builder.setMessage(cursorFunction.getString(cursorFunction.getColumnIndexOrThrow("content")));
                            builder.setTitle("約會內容");
                            builder.setPositiveButton("看完了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    }
                }
            });
        }


        //按鈕"新增"
        btnCreate = (ImageButton)findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Function = 0;
                setBtnColor();

                Intent intentCreate = new Intent();
                intentCreate.setClass(DatingActivity.this, newDatingActivity.class);
                startActivity(intentCreate);
            }
        });


        //按鈕"刪除"
        btnDel = (ImageButton)findViewById(R.id.btnDelete);
        btnDel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(Function != 1) {
                    Function = 1;
                    setBtnColor();
                }
                else{
                    Function = 0;
                    setBtnColor();
                }
            }
        });

        //按鈕"更改"
        btnUpdate = (ImageButton)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(Function != 2) {
                    Function = 2;
                    setBtnColor();
                }
                else{
                    Function = 0;
                    setBtnColor();
                }
            }
        });

        //按鈕"查詢"
        btnRead = (ImageButton)findViewById(R.id.btnRead);
        btnRead.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(Function != 3) {
                    Function = 3;
                    setBtnColor();
                }
                else{
                    Function = 0;
                    setBtnColor();
                }
            }
        });


    }

    protected void onResume (){
        super.onResume();
        //更新列表
        new RefreshList().execute();
    }

    //通过后台线程AsyncTask来读取数据库，放入更换Cursor
    private class RefreshList extends AsyncTask<Void, Void ,Cursor> {
        //步骤1.1：在后台线程中从数据库读取，返回新的游标newCursor
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor =  db.rawQuery("SELECT _id,date,target_id,scoreSelf,scoreTarget,content from Datings ORDER BY date", null);
            return newCursor;
        }
        //步骤1.2：线程最后执行步骤，更换adapter的游标，並將原游標關閉，释放资源
        protected void onPostExecute(Cursor newCursor) {
            datingAdapter.changeCursor(newCursor);//网上看到很多问如何更新ListView的信息，采用CusorApater其实很简单，换cursor就可以
            cursor.close();
            cursor = newCursor;
        }
    }

    private void setBtnColor(){
        //漸漸變淡
        AlphaAnimation alphaUp = new AlphaAnimation(1.0f, 0.5f);
        alphaUp.setDuration(500);
        alphaUp.setFillAfter(true);
        //漸漸變深
        AlphaAnimation alphaDown = new AlphaAnimation(0.5f, 1.0f);
        alphaDown.setDuration(500);
        alphaDown.setFillAfter(true);

        switch (Function){
            case 0: //全部還原
                btnDel.startAnimation(alphaDown);
                btnUpdate.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                break;
            case 1: //刪除按鈕變色，其他還原
                btnDel.startAnimation(alphaUp);
                btnUpdate.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                break;
            case 2:  //更改按鈕變色，其他還原
                btnUpdate.startAnimation(alphaUp);
                btnDel.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                break;
            case 3:  //查詢按鈕變色，其他還原
                btnRead.startAnimation(alphaUp);
                btnDel.startAnimation(alphaDown);
                btnUpdate.startAnimation(alphaDown);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dating, menu);
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