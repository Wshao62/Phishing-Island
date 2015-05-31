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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FileActivity extends Activity {

    ImageButton btnCreate,btnDelete,btnUpdate,btnRead,btnOrder;
    private SQLiteDatabase db = null;
    ListView listview;
    Cursor cursor = null;
    Cursor curFunction = null;
    private fileCursorAdapter fileAdaptor;
    private int Function = 0;
    com.ponggan.phishing.DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        db= (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        listview = (ListView)findViewById(R.id.listViewFile);

        cursor =db.rawQuery("SELECT _id,name,figure,photo,avg_score FROM Files ORDER BY _id", null);
        if (cursor != null) {
            cursor.moveToFirst();
            fileAdaptor = new fileCursorAdapter(getApplicationContext(), cursor);
            listview.setAdapter(fileAdaptor);
            //listview按下後的各種反應
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long arg3){
                    curFunction = (Cursor) fileAdaptor.getItem(position);
                    curFunction.moveToPosition(position);
                    final long rowId = Long.parseLong(curFunction.getString(curFunction.getColumnIndexOrThrow("_id")));
                    //沒有選擇功能時
                    if(Function == 0){
                        Toast.makeText(getApplicationContext(),curFunction.getString(curFunction.getColumnIndexOrThrow("name")) + "沒有任何反應", Toast.LENGTH_SHORT).show();
                    }
                    //選擇刪除der功能時
                    else if(Function == 1){
                        if(rowId > 0) {
                            new AlertDialog.Builder(FileActivity.this)
                                    .setTitle("蛤？" + "刪除" + curFunction.getString(curFunction.getColumnIndexOrThrow("name")) + "好嗎？")
                                    .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String[] str = {String.valueOf(rowId)};
                                            dbHandler.deleteGrade((int)rowId);
                                            db.delete("Datings", "target_id=?", str);//刪除所有此目標的約會
                                            db.delete("Files", "_id=?", str);//刪除你的目標
                                            Toast.makeText(getApplicationContext(), curFunction.getString(curFunction.getColumnIndexOrThrow("name")) +
                                                    "被刪除了", Toast.LENGTH_SHORT).show();
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
                            intentUpdate.setClass(FileActivity.this, updateFileActivity.class);
                            //new一個Bundle物件，並將要傳遞的資料傳入
                            Bundle bundleUpdate = new Bundle();
                            bundleUpdate.putLong("id", rowId);//傳遞Id(Long)
                            bundleUpdate.putString("name", curFunction.getString(curFunction.getColumnIndexOrThrow("name")));//傳遞Name(String)
                            bundleUpdate.putString("figure", curFunction.getString(curFunction.getColumnIndexOrThrow("figure")));//傳遞Figure(String)
                            bundleUpdate.putByteArray("photo", curFunction.getBlob(curFunction.getColumnIndex("photo")));//傳遞Photo(ByteArray)
                            //將Bundle物件傳給intent
                            intentUpdate.putExtras(bundleUpdate);
                            //切換Activity
                            startActivity(intentUpdate);
                        }
                    }
                    //選擇查詢der功能時
                    else if(Function == 3){
                        if(rowId > 0) {
                            //new一個intent物件，並指定Activity切換的class
                            Intent intentRead = new Intent();
                            intentRead.setClass(FileActivity.this, DatingTargetActivity.class);
                            //new一個Bundle物件，並將要傳遞的資料傳入
                            Bundle bundleRead = new Bundle();
                            bundleRead.putLong("id", rowId);//傳遞Id(Long)
                            bundleRead.putString("name", curFunction.getString(curFunction.getColumnIndexOrThrow("name")));//傳遞Name(String)
                            //將Bundle物件傳給intent
                            intentRead.putExtras(bundleRead);
                            //切換Activity
                            startActivity(intentRead);
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
                intentCreate.setClass(FileActivity.this, newFileActivity.class);
                startActivity(intentCreate);
            }
        });

        //按鈕"刪除"
        btnDelete = (ImageButton)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new Button.OnClickListener(){
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

        //按鈕"排行"
        btnOrder = (ImageButton)findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if(Function != 4) {
                    Function = 4;
                    setBtnColor();
                    new RefreshList().execute();
                }
                else{
                    Function = 0;
                    setBtnColor();
                    new RefreshList().execute();
                }
            }
        });

    }

    protected void onResume (){
        super.onResume();
        //更新列表
        new RefreshList().execute();
    }

    protected void onDestroy() {
        super.onDestroy();
        cursor.close();  //我们在onCreate()中没有关闭游标，因为需要和ListView进行数据关联，关闭curosr，会导致List无数据，故在最后释放资源
        db.close(); //断开和数据库的连接，释放相关资源
    }

    //通过后台线程AsyncTask来读取数据库，放入更换Cursor
    private class RefreshList extends AsyncTask<Void, Void ,Cursor> {
        //步骤1.1：在后台线程中从数据库读取，返回新的游标newCursor
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor;
            if(Function == 4){ //照分數排行的狀態
                newCursor =  db.rawQuery("SELECT _id,name,figure,photo,avg_score from Files ORDER BY avg_score DESC", null);
            }
            else {
                newCursor = db.rawQuery("SELECT _id,name,figure,photo,avg_score from Files ORDER BY _id", null);
            }
            return newCursor;
        }
        //步骤1.2：线程最后执行步骤，更换adapter的游标，并奖原游标关闭，释放资源
        protected void onPostExecute(Cursor newCursor) {
            fileAdaptor.changeCursor(newCursor);//网上看到很多问如何更新ListView的信息，采用CusorApater其实很简单，换cursor就可以
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
                btnDelete.startAnimation(alphaDown);
                btnUpdate.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                btnOrder.startAnimation(alphaDown);
                break;
            case 1: //刪除按鈕變色，其他還原
                btnDelete.startAnimation(alphaUp);
                btnUpdate.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                break;
            case 2:  //更改按鈕變色，其他還原
                btnUpdate.startAnimation(alphaUp);
                btnDelete.startAnimation(alphaDown);
                btnRead.startAnimation(alphaDown);
                break;
            case 3:  //查詢按鈕變色，其他還原
                btnRead.startAnimation(alphaUp);
                btnDelete.startAnimation(alphaDown);
                btnUpdate.startAnimation(alphaDown);
                break;
            case 4:  //排行按鈕變色
                btnOrder.startAnimation(alphaUp);
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file, menu);
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
