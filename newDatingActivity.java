package com.ponggan.phishing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class newDatingActivity extends Activity {

    String date,content;
    int target;
    EditText etDate,etContent;
    Button btnNext, btnCancel;
    com.ponggan.phishing.DatabaseHandler dbHandler;
    SQLiteDatabase db;
    Spinner spinnerTarget;
    Cursor cursor;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dating);

        dbHandler = new com.ponggan.phishing.DatabaseHandler(this);
        db = (new com.ponggan.phishing.DatabaseHandler(getApplicationContext() ) ).getWritableDatabase();

        //日期設定跳出date picker視窗
        calendar = calendar.getInstance();
        etDate = (EditText)findViewById(R.id.editDate);
        etDate.setOnTouchListener(new View.OnTouchListener() {
            //剛碰到的時候，onTouch會+1，鬆開也會+1，為了避免開兩次dialog，用這個來控制
            int touch_flag = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag ++;
                if(touch_flag == 2){
                    touch_flag = 0;
                    DatePickerDialog dialog =
                            new DatePickerDialog(newDatingActivity.this,
                                    datepicker,
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH));
                    dialog.show();
                }
                return false;
            }
        });

        etContent = (EditText)findViewById(R.id.editContent);
        spinnerTarget = (Spinner)findViewById(R.id.spinnerTarget);

        //下一步按鈕
        btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //new一個intent物件，並指定Activity切換的class
                Intent intentNext = new Intent();
                intentNext.setClass(newDatingActivity.this, QuestionnaireActivity.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundleNext = new Bundle();
                bundleNext.putString( "date", etDate.getText().toString() );
                bundleNext.putString( "content", etContent.getText().toString() );
                bundleNext.putInt( "target", target );
                //將Bundle物件傳給intent
                intentNext.putExtras(bundleNext);
                //切換Activity
                //一定要填日期、內容
                if (!etDate.getText().toString().equals("") && !etContent.getText().toString().equals("") &&
                        target > 0 ) {
                    startActivity(intentNext);
                    newDatingActivity.this.finish();
                } else {
                    Toast.makeText(newDatingActivity.this, "是不是少填了什麼？日期、內容為必填，沒目標的話記得先去新增目標喔！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                date = etDate.getText().toString();
                content = etContent.getText().toString();
                score = etScore.getText().toString();
                dbHandler.addDating(new com.ponggan.phishing.Dating(date, content, score, target) );
                newDatingActivity.this.finish();
            }
        });*/

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                newDatingActivity.this.finish();
            }
        });

        //把DB的資料導入下拉式選單
        cursor =db.rawQuery("SELECT _id,name from Files ORDER BY _id", null);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.spinner_target ,cursor, new String[] { "_id", "name" },
                                                    new int[] {R.id.text_target_id, R.id.text_target_name});
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTarget.setAdapter(adapter);
        spinnerTarget.setOnItemSelectedListener(new OnItemSelectedListener() {
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
