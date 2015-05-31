package com.ponggan.phishing;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;


public class delDatingActivity extends ListActivity {
    private SQLiteDatabase db = null;
    private Cursor cursor = null;
    private SimpleCursorAdapter adapter = null;
    private static final int DELETE_ID = Menu.FIRST +1;
    private static final int UPDATE_ID = Menu.FIRST +2;
    EditText etDate;
    EditText etContent;
    EditText etScore;

    // Datings table name
    private static final String TABLE_DATINGS = "Datings";

    // Datings Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SCORE = "score";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        cursor =db.rawQuery("SELECT _id,date,content,score from Datings ORDER BY _id", null);
        adapter = new SimpleCursorAdapter(this,
                R.layout.listview_dating,
                cursor,
                new String[] {"date","content","score"},
                new int[] {
                        R.id.listTextView_date,R.id.listTextView_content,R.id.listTextView_score });
        setListAdapter(adapter);

        //向ListView注册Context Menu，当系统检测到用户长按某单元是，触发Context Menu弹出
        registerForContextMenu(getListView());
    }

    protected void onDestroy() {
        super.onDestroy();
        cursor.close();  //我们在onCreate()中没有关闭游标，因为需要和ListView进行数据关联，关闭curosr，会导致List无数据，故在最后释放资源
        db.close(); //断开和数据库的连接，释放相关资源
    }

    //创建ContextMenu同OptionMenu，用户长按元素后，会弹出菜单
    public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE,DELETE_ID,Menu.NONE,"刪除");
        menu.add(Menu.NONE,UPDATE_ID,Menu.NONE,"更改");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //步骤 3: ContextMenu的触发操作，例子将触发delete()
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case DELETE_ID:
            /* 在此处，我们关键引入 AdapterView.AdapterContextMenuInfo来获取单元的信息。在有三个重要的信息。 1、id：The row id of the item for which the context menu is being displayed ，在cursorAdaptor中，实际就是表格的_id序号； 2、position 是list的元素的顺序；3、view就可以获得list中点击元素的View，通过view可以获取里面的显示的信息   */
                AdapterView.AdapterContextMenuInfo infoDel = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                delete(infoDel.id);
                return true;
            case UPDATE_ID:
                AdapterView.AdapterContextMenuInfo infoUpd = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                update(infoUpd.id);
                return true;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    //步骤4: 对触发弹框，和Add的相似，确定后，更新数据库和更新ListView的显示，上次学习已有相类的例子，不再重复。其中getNameById是通过id查名字的方法。值得注意的是，为了内部类中使用，delete的参数采用来final的形式。
    private void delete(final long  rowId){
        if(rowId>0){
            new AlertDialog.Builder(this)
                    .setTitle("刪除嗎？" /*+ getNameById(rowId)*/ )
                    .setPositiveButton("好", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteData(rowId);
                        }
                    })
                    .setNegativeButton("不好", null)
                    .show();
        }
    }

    private void deleteData(long rowId){
        String[] str = {String.valueOf(rowId)};
        db.delete("Datings","_id=?",str);
        new RefreshList().execute();  //采用后台方式，当然也可以用crusor.requery()来处理。
    }

    private void update(final long  rowId){
        String[] str = {String.valueOf(rowId)};
        LayoutInflater inflater = getLayoutInflater();
        View layoutUpdateDialog = inflater.inflate(R.layout.dialog_update_dating,(ViewGroup) findViewById(R.id.dialogUpdateDating));

        etDate = (EditText) layoutUpdateDialog.findViewById(R.id.DialogEditDate);
        etContent = (EditText) layoutUpdateDialog.findViewById(R.id.DialogEditContent);
        etScore = (EditText) layoutUpdateDialog.findViewById(R.id.DialogEditScore);

        Cursor cursor = db.query(TABLE_DATINGS, new String[] { KEY_ID,
                        KEY_DATE, KEY_CONTENT, KEY_SCORE }, KEY_ID + "=?",
                str, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            etDate.setText(cursor.getString(1));
            etContent.setText(cursor.getString(2));
            etScore.setText(cursor.getString(3));
        }


        if(rowId>0){
            new AlertDialog.Builder(this).setTitle("更改").setView(layoutUpdateDialog)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateData(rowId, etDate.getText().toString(), etContent.getText().toString(), etScore.getText().toString());
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    private void updateData(long rowId,String date,String content,String score){
        String[] str = {String.valueOf(rowId)};
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_CONTENT, content);
        values.put(KEY_SCORE, score);

        // updating row
        db.update(TABLE_DATINGS, values, KEY_ID + " = ?",str);

        new RefreshList().execute();  //采用后台方式，当然也可以用crusor.requery()来处理。
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
            adapter.changeCursor(newCursor);//网上看到很多问如何更新ListView的信息，采用CusorApater其实很简单，换cursor就可以
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
