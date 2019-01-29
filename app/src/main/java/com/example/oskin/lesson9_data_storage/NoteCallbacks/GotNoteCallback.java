package com.example.oskin.lesson9_data_storage.NoteCallbacks;

import com.example.oskin.lesson9_data_storage.Model.Note;

public interface GotNoteCallback extends NoteCallback{
    void onNoteReceived(Note note);
}
