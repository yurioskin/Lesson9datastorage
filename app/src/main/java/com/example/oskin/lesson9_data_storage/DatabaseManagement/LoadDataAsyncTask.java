package com.example.oskin.lesson9_data_storage.DatabaseManagement;

import android.content.Context;
import android.os.AsyncTask;
import com.example.oskin.lesson9_data_storage.Model.Note;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.DeletedNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.GotNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.GotNotesCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.LoadedNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.NoteCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

    public static final int LOAD_NOTES = 1;
    public static final int ADD_NOTE = 2;
    public static final int DELETE_NOTE = 3;
    public static final int UPDATE_NOTE = 4;
    public static final int GET_NOTE = 5;

    private int mAction;
    private Note mNote;
    private List<Note> noteList;
    private Context mContext;

    private NoteCallback mNoteCallback;
    public LoadDataAsyncTask (Context context, NoteCallback noteCallback, int action, Note note){
        mContext = context;
        mNoteCallback = noteCallback;
        mAction = action;
        mNote = note;
    }

    @Override
    protected Void doInBackground(Void... voids) {
            switch (mAction) {
                case LOAD_NOTES:
                    getNotes();
                    break;
                case ADD_NOTE:
                    addNote();
                    break;
                case UPDATE_NOTE:
                    updateNote();
                    break;
                case DELETE_NOTE:
                    deleteNote();
                    break;
                case GET_NOTE:
                    getNote();
                    break;
        }
        return null;
    }

    private void getNotes(){
        DBManager dbManager = new DBManager(mContext);
        noteList = dbManager.getNotes();
    }

    private void addNote(){
        DBManager dbManager = new DBManager(mContext);
        dbManager.addNote(mNote);
    }

    private void updateNote(){
        DBManager dbManager = new DBManager(mContext);
        dbManager.updateNote(mNote);

    }

    private void deleteNote(){
            /*try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        DBManager dbManager = new DBManager(mContext);
        dbManager.deleteNote(mNote.getId());
    }

    private void getNote(){
        DBManager dbManager = new DBManager(mContext);
        mNote = dbManager.getNote(mNote.getId());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        switch (mAction){
            case LOAD_NOTES:
                ((GotNotesCallback) mNoteCallback).onNotesReceived(noteList);
                break;
            case ADD_NOTE:
                ((LoadedNoteCallback) mNoteCallback).onNoteLoaded();
                break;
            case UPDATE_NOTE:
                ((LoadedNoteCallback) mNoteCallback).onNoteLoaded();
                break;
            case DELETE_NOTE:
                ((DeletedNoteCallback) mNoteCallback).onNoteDeleted();
                break;
            case GET_NOTE:
                ((GotNoteCallback) mNoteCallback).onNoteReceived(mNote);
                break;
        }
    }
}
