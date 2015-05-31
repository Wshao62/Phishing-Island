package com.ponggan.phishing;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MessageActivity extends Activity {

    String date,location;
    int target;
    EditText etDate,etLocation,editMessage,etName;
    Button btnDone, btnCopy, btnCancel;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    SQLiteDatabase db,db2,db3;
    Spinner spinnerTarget,spinnerMode;
    Cursor cursor,cursor2,cursor3;
    String mode,dataA,dataB,dataC;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        db = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        db2 = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        db3 = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();
        etDate = (EditText)findViewById(R.id.editDate);
        etLocation = (EditText)findViewById(R.id.editLocation);
        editMessage = (EditText)findViewById(R.id.editMessage);
        spinnerTarget = (Spinner)findViewById(R.id.spinnerTarget);
        spinnerMode = (Spinner)findViewById(R.id.spinnerMode);
        etName = (EditText)findViewById(R.id.editName);

        //日期設定跳出date picker視窗
        calendar = calendar.getInstance();
        etDate.setOnTouchListener(new View.OnTouchListener() {
            //剛碰到的時候，onTouch會+1，鬆開也會+1，為了避免開兩次dialog，用這個來控制
            int touch_flag = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag ++;
                if(touch_flag == 2){
                    touch_flag = 0;
                    DatePickerDialog dialog =
                            new DatePickerDialog(MessageActivity.this,
                                    datepicker,
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
                return false;
            }
        });
        /*//把DB的資料導入下拉式選單
        cursor =db.rawQuery("SELECT _id,name from Files ORDER BY _id", null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.spinner_target ,cursor, new String[] {"_id", "name" },
                new int[] { R.id.text_target_id,R.id.text_target_name});
        spinnerTarget.setAdapter(adapter);
        spinnerTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long id) {
                //cursor.moveToPosition(position);
                target = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });*/

        //把DB的資料導入下拉式選單
        cursor2 =db2.rawQuery("SELECT _id,mode,dataA,dataB,dataC   from Message ORDER BY _id", null);
        final SimpleCursorAdapter adapterM = new SimpleCursorAdapter(
                this, R.layout.spinner_mode ,cursor2, new String[] { "_id","mode","dataA","dataB","dataC" },
                new int[] { R.id.text_mode_id, R.id.text_mode_mode});
        spinnerMode.setAdapter(adapterM);
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
            int position, long id) {
              //cursor.moveToPosition(position);
              mode = cursor2.getString(cursor2.getColumnIndex("mode"));
              dataA = cursor2.getString(cursor2.getColumnIndex("dataA"));
              dataB = cursor2.getString(cursor2.getColumnIndex("dataB"));
              dataC = cursor2.getString(cursor2.getColumnIndex("dataC"));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        cursor3=db3.rawQuery("SELECT _id, mode ,dataA , dataB , dataC  from Message WHERE dataA = " +mode+"", null);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                try {
                    //String Msg = dbHandler.getDatingTargetName(target)+dataA+etDate.getText().toString()+dataB+etLocation.getText().toString()+dataC;
                    String Msg = etName.getText().toString() + dataA+etDate.getText().toString()+dataB+etLocation.getText().toString()+dataC;
                    editMessage.setText(Msg);
                } catch (Exception e) {
                    Toast.makeText(MessageActivity.this, "是不是少填了什麼？一定要有目標喔！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCopy = (Button)findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //TODO Auto-generated method stub
                copyToClipboard(editMessage.getText().toString());
                Toast.makeText(MessageActivity.this, "已經複製到剪貼簿", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                MessageActivity.this.finish();
            }
        });
    }


    //time picker的設定監聽器
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyyMMdd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            etDate.setText(sdf.format(calendar.getTime()));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
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

    //複製到剪貼簿
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyToClipboard(String str){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            Log.e("version", "1 version");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
            clipboard.setPrimaryClip(clip);
            Log.e("version","2 version");
        }
    }
}

