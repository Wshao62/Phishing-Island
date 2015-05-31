package com.ponggan.phishing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class newFileActivity extends Activity {

    String name,figure;
    EditText etName,etFigure;
    Button btnDone, btnCancel, btnPhoto;
    ImageView imageView_save;
    com.ponggan.phishing.DatabaseHandler db;
    private static int RESULT_LOAD_IMAGE = 1;
    Uri selectedImage = null;
    InputStream imageStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);

        db = new com.ponggan.phishing.DatabaseHandler(this);

        etName = (EditText)findViewById(R.id.editName);
        etFigure = (EditText)findViewById(R.id.editFigure);
        imageView_save = (ImageView) findViewById(R.id.imageViewNewFile);
        btnDone = (Button)findViewById(R.id.btnDone);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnPhoto =  (Button)findViewById(R.id.btnPhoto);

        imageView_save.setAdjustViewBounds(true);
        imageView_save.setMaxHeight(650);
        imageView_save.setMaxWidth(650);

        btnPhoto.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btnDone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                name = etName.getText().toString();
                figure = etFigure.getText().toString();

                //imageView轉Bitmap
                imageView_save.setDrawingCacheEnabled(true);
                //建立圖片的緩存，圖片的緩存本身就是一個Bitmap
                imageView_save.buildDrawingCache();
                //取得緩存圖片的Bitmap檔
                Bitmap bmp=imageView_save.getDrawingCache();

                //轉換為圖片指定大小
                //獲得圖片的寬高
                int width = bmp.getWidth();
                int height = bmp.getHeight();
                // 設置想要的大小
                int newWidth = 480;
                int newHeight = 480;
                // 計算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 取得想要缩放的matrix參數
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的圖片
                Bitmap newbmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
                //把BMP圖檔轉成output stream
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                newbmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                //Get the underlying array containing the data.
                byte[] imgBytes = baos.toByteArray();
                //在Database裡面Insert一欄目標的資料
                //一定要有名字，可以沒有特徵，沒有照片
                if (!name.equals("")) {
                    db.addFile(name, figure, imgBytes);
                    newFileActivity.this.finish();
                }
                else {
                    Toast.makeText(newFileActivity.this, "是不是少填了什麼？名字一定要填", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                newFileActivity.this.finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                imageView_save.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_file, menu);
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

    private static Bitmap loadBitmapFromView(View yourView) {
        Bitmap snapshot = null;
        Drawable drawable = null;
        yourView.setDrawingCacheEnabled(true);
        yourView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH); //Quality of the snpashot
        try {
            yourView.buildDrawingCache(); // UPDATE HERE
            snapshot = Bitmap.createBitmap(yourView.getDrawingCache());
            drawable = new BitmapDrawable(snapshot);
        } finally {
            yourView.setDrawingCacheEnabled(false);
        }
        return snapshot;
    }
}
