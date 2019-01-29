package com.example.oskin.lesson9_data_storage.DatabaseManagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION_DB = 1;
    private static final String DB_NAME = "notes.db";
    public static final String TABLE_NAME = "NOTES";

    public static final String COLUMN_ID = "noteID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_CONTENT = "CONTENT";

    public DBHelper(Context context){
        super(context, DB_NAME, null, VERSION_DB);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NOTES (noteID text primary key, NAME text, CONTENT text)");
    }

    private void deleteTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS NOTES");
    }
}
