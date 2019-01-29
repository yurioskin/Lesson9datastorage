package com.example.oskin.lesson9_data_storage.NoteCallbacks;

import com.example.oskin.lesson9_data_storage.Model.Note;

import java.util.List;

public interface GotNotesCallback extends NoteCallback {
    void onNotesReceived(List<Note> notes);
}
