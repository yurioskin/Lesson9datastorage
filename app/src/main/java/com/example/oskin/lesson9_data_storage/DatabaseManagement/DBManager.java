package com.example.oskin.lesson9_data_storage.DatabaseManagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.oskin.lesson9_data_storage.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private DBHelper mDBHelper;

    public DBManager(Context context){
        mDBHelper = new DBHelper(context);
    }

    public void addNote(Note note){
        SQLiteDatabase db = null;
        try{
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = getContentValues(note);
            db.insert(DBHelper.TABLE_NAME,null,contentValues);
            db.setTransactionSuccessful();
        }catch (SQLException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
         if (db != null){
             if (db.inTransaction()) {
                 db.endTransaction();
             }
             db.close();
            }
        }
    }

    public void updateNote(Note note){
        SQLiteDatabase db = null;
        try{
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = getContentValues(note);
            db.update(DBHelper.TABLE_NAME,contentValues,
                    DBHelper.COLUMN_ID + " = ?",
                    new String[]{note.getId()});

            db.setTransactionSuccessful();
        }catch (SQLException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    public void deleteNote(String id){
        SQLiteDatabase db = null;
        try{
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(DBHelper.TABLE_NAME,DBHelper.COLUMN_ID + " = ? ", new String[] {id});
            db.setTransactionSuccessful();
        }catch(SQLException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
    }

    private ContentValues getContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_ID, note.getId());
        contentValues.put(DBHelper.COLUMN_NAME, note.getName());
        contentValues.put(DBHelper.COLUMN_CONTENT, note.getText());
        return contentValues;
    }

    public Note getNote(String id){
        Note note = null;
        SQLiteDatabase db = null;
        try{
            db = mDBHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(
                    DBHelper.TABLE_NAME,
                    new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_CONTENT},
                    DBHelper.COLUMN_ID + " = ?",
                    new String[]{id},
                    null,
                    null,
                    null);
            note = parseCursorForNote(cursor);
            db.setTransactionSuccessful();
        }catch (SQLException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return note;
    }

    private Note parseCursorForNote(Cursor cursor) {
            Note note = null;
        if (cursor.moveToFirst()) {
            note = new Note();
            note.setId(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
            note.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
            note.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT)));
        }
        return note;
    }

    public List<Note> getNotes(){
        List<Note> noteList = null;
        SQLiteDatabase db = null;
        try{
            db = mDBHelper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(DBHelper.TABLE_NAME,null,null,
                    null, null, null, null);
            noteList = parseCursor(cursor);
            db.setTransactionSuccessful();
        }catch (SQLException e){
            Log.v("SQLiteException", e.getMessage());
        } finally {
            if (db != null){
                if (db.inTransaction()) {
                    db.endTransaction();
                }
                db.close();
            }
        }
        return noteList;
    }

    private List<Note> parseCursor(Cursor cursor) {
        List<Note> noteList = new ArrayList<>();
        Note note;
        if (cursor.moveToFirst()){
            do{
                note = new Note();
                note.setId(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
                note.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT)));
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }
}
