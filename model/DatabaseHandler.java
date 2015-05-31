package com.ponggan.phishing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "PhishingManager";

    // Datings table name
    private static final String TABLE_DATINGS = "Datings";

    // Datings table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_SCORE_SELF = "scoreSelf";
    private static final String KEY_SCORE_TARGET = "scoreTarget";
    private static final String KEY_TARGET = "target_id";

    // Files table name
    private static final String TABLE_FILES = "Files";

    // Files table Columns names
    private static final String FILE_ID = "_id";
    private static final String FILE_NAME = "name";
    private static final String FILE_FIGURE = "figure";
    private static final String FILE_PHOTO = "photo";
    private static final String FILE_AVG = "avg_score";

    // Questions table name
    private static final String TABLE_QUESTIONS = "Questions";

    // Questions table Columns names
    private static final String QUESTION_ID = "_id";
    private static final String QUESTION_TYPE = "type";
    private static final String QUESTION = "question";

    // Message table name
    private static final String TABLE_MESSAGE = "Message";

    // Message table Columns names
    private static final String MESSAGE_ID = "_id";
    private static final String MESSAGE_MODE = "mode";
    private static final String MESSAGE_DATA_A = "dataA";
    private static final String MESSAGE_DATA_B = "dataB";
    private static final String MESSAGE_DATA_C = "dataC";


    // Rule table name
    private static final String TABLE_RULE = "Rule";

    // Rule table Columns names
    private static final String RULE_ID = "_id";
    private static final String RULE_TYPE = "type";
    private static final String RULE_DATA = "data";

    // Grades table name
    private static final String TABLE_GRADES = "Grades";

    // Grades table Columns names
    private static final String GRADES_ID = "_id";
    private static final String GRADES_datingID = "dating_id";
    private static final String GRADES_QID = "question_id";
    private static final String GRADES_VALUE = "value";

    // debuguy時，改變DB的檔案位置
    public DatabaseHandler(Context context) {
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, "/mnt/sdcard/phishing.db", null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATINGS_TABLE = "CREATE TABLE " + TABLE_DATINGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT NOT NULL,"
                + KEY_CONTENT + " TEXT NOT NULL," + KEY_SCORE_SELF + " INTEGER NOT NULL," + KEY_SCORE_TARGET + " INTEGER NOT NULL,"
                + KEY_TARGET + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_DATINGS_TABLE);

        String CREATE_FILES_TABLE = "CREATE TABLE " + TABLE_FILES + "("
                + FILE_ID + " INTEGER PRIMARY KEY," + FILE_NAME + " TEXT NOT NULL,"
                + FILE_FIGURE + " TEXT NOT NULL," + FILE_PHOTO + " BLOB," + FILE_AVG + " REAL NOT NULL" + ")";
        db.execSQL(CREATE_FILES_TABLE);

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + QUESTION_ID + " INTEGER PRIMARY KEY," + QUESTION_TYPE + " TEXT NOT NULL,"
                + QUESTION + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_GRADE_TABLE = "CREATE TABLE " + TABLE_GRADES + "("
                + GRADES_ID + " INTEGER PRIMARY KEY," + GRADES_datingID + " INTEGER NOT NULL,"
                + GRADES_QID + " INTEGER NOT NULL," + GRADES_VALUE + " REAL NOT NULL"+ ")";
        db.execSQL(CREATE_GRADE_TABLE);



        String CREATE_MESSAGE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY," + MESSAGE_MODE+ " TEXT NOT NULL,"
                + MESSAGE_DATA_A + " TEXT NOT NULL," + MESSAGE_DATA_B + " TEXT NOT NULL,"+ MESSAGE_DATA_C + " TEXT NOT NULL"  + ")";
        db.execSQL(CREATE_MESSAGE_TABLE);

        ContentValues values = new ContentValues();
        values.put(MESSAGE_MODE, "吃飯"); // Dating date
        values.put(MESSAGE_DATA_A,  "你好,不知道你"); // Dating content
        values.put(MESSAGE_DATA_B, "有空嗎?想要約你去"); // Dating score
        values.put(MESSAGE_DATA_C, "吃飯~"); // Dating target
        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);

        ContentValues values2 = new ContentValues();
        values2.put(MESSAGE_MODE, "玩"); // Dating date
        values2.put(MESSAGE_DATA_A,  "你好,不知道你"); // Dating content
        values2.put(MESSAGE_DATA_B, "有空嗎?想要約你去"); // Dating score
        values2.put(MESSAGE_DATA_C, "玩~"); // Dating target
        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values2);

        String CREATE_RULE_TABLE = "CREATE TABLE " + TABLE_RULE + "("
                + RULE_ID + " INTEGER PRIMARY KEY," + RULE_TYPE+ " TEXT NOT NULL,"
                + RULE_DATA + " TEXT NOT NULL"+ ")";
        db.execSQL(CREATE_RULE_TABLE);

        ContentValues values3 = new ContentValues();
        values3.put(RULE_TYPE, "穿搭"); // Dating date
        values3.put(RULE_DATA,  "白襯衫+深色針織外套+直挺長褲"); // Dating content
        // Inserting Row
        db.insert(TABLE_RULE, null, values3);

        ContentValues values4 = new ContentValues();
        values4.put(RULE_TYPE, "禁忌"); // Dating date
        values4.put(RULE_DATA,  "狂講自己的戀愛史,炫富"); // Dating content
        // Inserting Row3
        db.insert(TABLE_RULE, null, values4);

        ContentValues values5 = new ContentValues();
        values5.put(RULE_TYPE, "送禮指南"); // Dating date
        values5.put(RULE_DATA,  "小禮物"); // Dating content
        // Inserting Row
        db.insert(TABLE_RULE, null, values5);

        ContentValues values6 = new ContentValues();
        values6.put(RULE_TYPE, "地點"); // Dating date
        values6.put(RULE_DATA,  "宜蘭冬山河-永恆水教堂"); // Dating content
        // Inserting Row
        db.insert(TABLE_RULE, null, values6);

        ContentValues values7 = new ContentValues();
        values7.put(RULE_TYPE, "話題寶盒"); // Dating date
        values7.put(RULE_DATA,  "老師:下星期五,學校要舉行春季遠足,要大家提出一個可以烤肉的地點...\n" +
                "小明:老師到動物園烤肉最好了,因為那裡什麼肉都有.....^^-^^"); // Dating content
        // Inserting Row
        db.insert(TABLE_RULE, null, values7);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new dating
    void addDating(com.ponggan.phishing.Dating dating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesDating = new ContentValues();
        valuesDating.put(KEY_DATE, dating.getDate()); // com.ponggan.phishing.Dating date
        valuesDating.put(KEY_CONTENT, dating.getContent()); // com.ponggan.phishing.Dating content
        valuesDating.put(KEY_SCORE_SELF, dating.getScoreSelf()); // com.ponggan.phishing.Dating self score
        valuesDating.put(KEY_SCORE_TARGET, dating.getScoreTarget()); // com.ponggan.phishing.Dating target score
        valuesDating.put(KEY_TARGET, dating.getTarget()); // com.ponggan.phishing.Dating target

        // Inserting Row
        // 沒有約會對象就不新增約會
        if (dating.getTarget() >= 0) {
            db.insert(TABLE_DATINGS, null, valuesDating);
        } else {
        }
        db.close(); // Closing database connection
    }

    // Getting single dating
    com.ponggan.phishing.Dating getDating(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DATINGS, new String[] { KEY_ID,
                        KEY_DATE, KEY_CONTENT, KEY_SCORE_SELF, KEY_SCORE_TARGET, KEY_SCORE_TARGET }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        com.ponggan.phishing.Dating dating = new com.ponggan.phishing.Dating(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5) );
        // return dating
        return dating;
    }

    // Getting All Datings
    public List<com.ponggan.phishing.Dating> getAllDatings() {
        List<com.ponggan.phishing.Dating> datingList = new ArrayList<com.ponggan.phishing.Dating>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DATINGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                com.ponggan.phishing.Dating dating = new com.ponggan.phishing.Dating();
                dating.setID(cursor.getInt(0));
                dating.setDate(cursor.getString(1));
                dating.setContent(cursor.getString(2));
                dating.setScoreSelf(cursor.getInt(3));
                dating.setScoreTarget(cursor.getInt(4));
                dating.setTarget(cursor.getInt(5));

                // Adding dating to list
                datingList.add(dating);
            } while (cursor.moveToNext());
        }

        // return dating list
        return datingList;
    }

    // Updating single dating
    void updateDating(int rowID,String date, String content, int targetID){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] str = {String.valueOf(rowID)};
        ContentValues values = new ContentValues();
        //values.put(KEY_DATE, date); // com.ponggan.phishing.Dating date
        values.put(KEY_CONTENT, content); // com.ponggan.phishing.Dating content
        //values.put(KEY_TARGET, targetID); // com.ponggan.phishing.Dating targetID

        // updating row
        db.update(TABLE_DATINGS, values, KEY_ID + " = ?",str);

        db.close(); // Closing database connection
    }

    // Deleting single dating
    public void deleteDating(int rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] str = {String.valueOf(rowID)};

        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + "," + KEY_TARGET + " FROM " + TABLE_DATINGS +
                " WHERE " + KEY_ID + " = " + rowID, null);
        cursor.moveToFirst();
        int datingID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        String[] strGrade = {String.valueOf(datingID)};
        db.delete(TABLE_GRADES, GRADES_datingID + "=?", strGrade);//刪除約會有關的所有成績

        int targetID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TARGET));
        db.delete(TABLE_DATINGS, "_id=?", str);//刪除某次約會

        Cursor cursorAvg = db.rawQuery("SELECT " + KEY_SCORE_SELF + "," + KEY_SCORE_TARGET + "," + KEY_TARGET + " FROM " + TABLE_DATINGS +
                " WHERE " + KEY_TARGET + " = " + targetID, null);
        double total = 0;
        cursorAvg.moveToPosition(-1);
        while (cursorAvg.moveToNext()){
            total += (( cursorAvg.getInt(cursorAvg.getColumnIndexOrThrow(KEY_SCORE_SELF)) + cursorAvg.getInt(cursorAvg.getColumnIndexOrThrow(KEY_SCORE_TARGET)) ) / 2);
        }
        double avg = 0;
        try {
            avg = total / cursorAvg.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("刪除後的約會平均分：", avg + "");

        db.close();
    }

    // Get dating target name
    public String getDatingTargetName(int targetID) {
        String getDatingTargetNameQuery = "SELECT "+FILE_ID+","+FILE_NAME+" FROM " + TABLE_FILES + " WHERE "+ FILE_ID + " = " + targetID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getDatingTargetNameQuery, null);
        String targetName;

        // return dating target name
        try {
            cursor.moveToFirst();
            targetName = cursor.getString(cursor.getColumnIndexOrThrow(FILE_NAME));
            cursor.close();
            db.close(); // Closing database connection
            return targetName;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "出錯了";
        } catch (CursorIndexOutOfBoundsException eCursor) {
            eCursor.printStackTrace();
            return "出錯了";
        }
    }


    // Getting dating ID
    public int getDatingID(String date, String content, int scoreSelf, int scoreTarget, int targetID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_DATINGS + " WHERE " + KEY_DATE + " = " + date
                + " AND " + KEY_CONTENT + " = '" + content + "' AND " + KEY_SCORE_SELF + " = " + scoreSelf
                + " AND " + KEY_SCORE_TARGET + " = " + scoreTarget + " AND " + KEY_TARGET + " = " + targetID, null);
        if(cursor != null) {
            cursor.moveToLast();
        }

        int datingID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        cursor.close();
        return datingID;
    }

    // Adding new file
    void addFile(String name, String figure, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FILE_NAME, name); // File name
        values.put(FILE_FIGURE, figure); // File figure
        values.put(FILE_PHOTO, photo); // File photo
        values.put(FILE_AVG, 0); // File average score 初始值

        // Inserting Row
        db.insert(TABLE_FILES, null, values);
        db.close(); // Closing database connection
    }

    // Updating file
    void updateFile(long rowId, String name, String figure, byte[] photo){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] str = {String.valueOf(rowId)};
        ContentValues values = new ContentValues();
        values.put(FILE_NAME, name); // File name
        values.put(FILE_FIGURE, figure); // File figure
        values.put(FILE_PHOTO, photo); // File photo

        // updating row
        db.update(TABLE_FILES, values, FILE_ID + " = ?",str);

        db.close(); // Closing database connection
    }

    // Updating file's average score
    void updateFileAvg(int targetID){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + KEY_SCORE_SELF + "," + KEY_SCORE_TARGET + "," + KEY_TARGET + " FROM " + TABLE_DATINGS +
                " WHERE " + KEY_TARGET + " = " + targetID, null);
        double total = 0;
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()){
            total += (( cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCORE_SELF)) + cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SCORE_TARGET)) ) / 2);
        }
        double avg = 0;
        try {
            avg = total / cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("新增後的約會平均分：", avg + "");

        String[] str = {String.valueOf(targetID)};
        ContentValues values = new ContentValues();
        values.put(FILE_AVG, avg); // File average score

        // updating score column
        db.update(TABLE_FILES, values, FILE_ID + " = ?",str);

        db.close();
    }

    // Initializing questions' values
    void initializeQuestion(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM "+TABLE_QUESTIONS;
        Cursor cursor = db.rawQuery(count, null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        int rowCount = cursor.getInt(0);

        //如果資料表裡面有東西，就不要新增問題
        if(rowCount > 0) {
            //不放入問題
        }
        else{
            //type為自己的感受
            // 新增一條問題
            values.put(QUESTION_TYPE, "self"); // Question type
            values.put(QUESTION, "今天約會你是否開心？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "self"); // Question type
            values.put(QUESTION, "你是否希望還會有下次約會呢？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "self"); // Question type
            values.put(QUESTION, "你是否會時常回想這次的約會呢？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "self"); // Question type
            values.put(QUESTION, "你是否想跟她有進一步發展？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "self"); // Question type
            values.put(QUESTION, "這次約會你是否接受過她不合理的要求？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);

            //type為目標的感受
            // 新增一條問題
            values.put(QUESTION_TYPE, "target"); // Question type
            values.put(QUESTION, "她今天是否有一直對你微笑？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "target"); // Question type
            values.put(QUESTION, "她今天是否對你有任何的肢體接觸？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "target"); // Question type
            values.put(QUESTION, "這次約會結束後,她是否旋即與你聯絡？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "target"); // Question type
            values.put(QUESTION, "這次約會過程中她是否有主動找話題跟你聊？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
            // 新增一條問題
            values.put(QUESTION_TYPE, "target"); // Question type
            values.put(QUESTION, "她是否有透漏她最近想做什麼或想參與什麼活動？"); // Question content
            // Inserting Row
            db.insert(TABLE_QUESTIONS, null, values);
        }
        // Closing database connection
        cursor.close();
        db.close();
    }

    //Getting a question
    String getQuestion(int rowID, String type){
        String getQuestionQuery = "SELECT "+QUESTION_ID+","+QUESTION_TYPE+","+QUESTION+" FROM " + TABLE_QUESTIONS +
                " WHERE "+ QUESTION_ID + " = " + rowID + " AND " + QUESTION_TYPE + " = '" + type + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getQuestionQuery, null);
        String question;

        // return a question
        try {
            if( cursor != null && cursor.moveToFirst() ) {
                question =  cursor.getString(cursor.getColumnIndexOrThrow(QUESTION));
                cursor.close();
                db.close(); // Closing database connection
                return question;
            }
            else
                return "出錯了";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "出錯了";
        }
    }

    // Add new grades
    void addGrade(int dating_id, int question_id, double value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GRADES_datingID, dating_id);
        values.put(GRADES_QID, question_id);
        values.put(GRADES_VALUE, value);

        // Inserting Row
        db.insert(TABLE_GRADES, null, values);

        db.close(); // Closing database connection
    }

    // Delete new grades
    public void deleteGrade(int targetID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + "," + KEY_TARGET + " FROM " + TABLE_DATINGS +
                " WHERE " + KEY_TARGET + " = " + targetID, null);
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                String[] str = {String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)))};
                db.delete(TABLE_GRADES, GRADES_datingID + "=?", str);//刪除某次成績
            }
        }

        db.close();
    }

    // Get grades ID
    public int getGradeID(int datingID, int QID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + GRADES_ID + "," + GRADES_datingID + "," + GRADES_QID + " FROM " + TABLE_GRADES +
                " WHERE " + GRADES_datingID + " = " + datingID + " AND " + GRADES_QID + " = " + QID, null);
        cursor.moveToFirst();
        int gradeID = cursor.getInt(cursor.getColumnIndexOrThrow(GRADES_ID));

        db.close();

        return gradeID;
    }
}