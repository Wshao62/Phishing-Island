package com.ponggan.phishing;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 這是用來綁定約會Activity中，可以列出所有約會的List view用的Cursor adaptor
 */
public class DatingCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;
    com.ponggan.phishing.DatabaseHandler dbHandler;

    public DatingCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public void bindView (View view, Context context, Cursor cursor){
        String date = cursor.getString(cursor.getColumnIndex("date"));
        int targetID = cursor.getInt(cursor.getColumnIndex("target_id"));
        int score = (cursor.getInt(cursor.getColumnIndex("scoreSelf")) + cursor.getInt(cursor.getColumnIndex("scoreTarget")) ) / 2;

        dbHandler = new com.ponggan.phishing.DatabaseHandler(context);

        TextView textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        TextView textViewTargetName = (TextView) view.findViewById(R.id.textViewTargetName);
        TextView textViewScore = (TextView) view.findViewById(R.id.textViewScore);

        //文字欄位綁定
        try {
            textViewDate.setText(date);
            textViewTargetName.setText(dbHandler.getDatingTargetName(targetID));
            textViewScore.setText(score + "分");
        } catch (Exception e) {
            Toast.makeText(context, "資料庫怪怪的，有人沒目標就亂新增！", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent){
        return mInflater.inflate(R.layout.listview_main_dating, parent, false);  //一般都这样写，返回列表行元素，注意这里返回的就是bindView中的view
    }
}