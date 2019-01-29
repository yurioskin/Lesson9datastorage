package com.example.oskin.lesson9_data_storage.MainRecycler;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oskin.lesson9_data_storage.Activities.ViewNote;
import com.example.oskin.lesson9_data_storage.DatabaseManagement.LoadDataAsyncTask;
import com.example.oskin.lesson9_data_storage.Model.Note;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.DeleteNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.NoteCallback;
import com.example.oskin.lesson9_data_storage.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private List<Note> mNotes;
    private NoteCallback mNoteCallback;
    private boolean isClickable = true;

    public CustomAdapter(Context context, NoteCallback noteCallback) {
        mContext = context;
        mNotes = new ArrayList<>();
        mNoteCallback = noteCallback;
    }

    public void loadData(List<Note> newNotes){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(mNotes,newNotes));
        diffResult.dispatchUpdatesTo(this);
        mNotes.clear();
        mNotes.addAll(newNotes);
        isClickable = true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.note_item_layout,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
        noteViewHolder.name.setText(mNotes.get(position).getName());
        noteViewHolder.text.setText(mNotes.get(position).getText());
        noteViewHolder.deleteImage.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView text;
        public ImageView deleteImage;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            text = itemView.findViewById(R.id.item_text);
            deleteImage = itemView.findViewById(R.id.note_item_layout_delete);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (!isClickable)
                        return;
                    isClickable = false;*/
                    deleteImage.setEnabled(false);
                    LoadDataAsyncTask asyncTask = new LoadDataAsyncTask(mContext, mNoteCallback,
                            LoadDataAsyncTask.DELETE_NOTE,mNotes.get(getAdapterPosition()));
                    asyncTask.execute();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClickable)
                        return;
                    isClickable = false;
                    mContext.startActivity(ViewNote.newIntent(mContext,
                            mNotes.get(getAdapterPosition())));
                }
            });
        }
    }


}
