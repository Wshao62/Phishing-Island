package com.ponggan.phishing;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * 這是用來綁定DatingTargetActivity中，可以列出所有跟這個目標的約會的List view用的Cursor adaptor
 */
public class DatingTargetCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;
    com.ponggan.phishing.DatabaseHandler dbHandler;

    public DatingTargetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public void bindView (View view, Context context, Cursor cursor){
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        int score = (cursor.getInt(cursor.getColumnIndex("scoreSelf")) + cursor.getInt(cursor.getColumnIndex("scoreTarget")) ) / 2;

        dbHandler = new com.ponggan.phishing.DatabaseHandler(context);

        TextView textViewDate = (TextView) view.findViewById(R.id.listTextView_date);
        TextView textViewContent = (TextView) view.findViewById(R.id.listTextView_content);
        TextView textViewScore = (TextView) view.findViewById(R.id.listTextView_score);

        //文字欄位綁定
        textViewDate.setText(date);
        textViewContent.setText(content);
        textViewScore.setText(score + "分");
    }
    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent){
        return mInflater.inflate(R.layout.listview_dating, parent, false);  //一般都这样写，返回列表行元素，注意这里返回的就是bindView中的view
    }
}