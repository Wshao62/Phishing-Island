package com.ponggan.phishing;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.text.NumberFormat;

/**
 * 這是用來綁定目標Activity中，可以列出所有目標的List view用的Cursor adaptor
 */
public class fileCursorAdapter extends CursorAdapter{
    private LayoutInflater mInflater;

        public fileCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
            mInflater = LayoutInflater.from(context);
        }
        @Override
        public void bindView (View view, Context context, Cursor cursor){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String figure = cursor.getString(cursor.getColumnIndex("figure"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex("photo"));
            double avg = cursor.getDouble(cursor.getColumnIndex("avg_score"));

            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);

            TextView textViewName = (TextView) view.findViewById(R.id.listview_file_textViewName);
            TextView textViewFigure = (TextView) view.findViewById(R.id.listview_file_textViewFig);
            TextView textViewAvg = (TextView) view.findViewById(R.id.listview_file_textViewAvg);
            ImageView image = (ImageView) view.findViewById(R.id.listview_file_Image);
            //文字欄位綁定
            textViewName.setText(name);
            textViewFigure.setText(figure);
            textViewAvg.setText("總平均：" + numberFormat.format(avg));
            //圖像綁定
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            //小螢幕用 限制圖片大小
            image.setAdjustViewBounds(true);
            image.setMaxHeight(480);
            image.setMaxWidth(480);

            image.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        }
        @Override
        public View newView (Context context, Cursor cursor, ViewGroup parent){
            return mInflater.inflate(R.layout.listview_file, parent, false);  //一般都这样写，返回列表行元素，注意这里返回的就是bindView中的view
        }
}