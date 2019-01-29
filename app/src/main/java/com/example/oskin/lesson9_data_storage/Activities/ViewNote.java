package com.example.oskin.lesson9_data_storage.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.oskin.lesson9_data_storage.DatabaseManagement.LoadDataAsyncTask;
import com.example.oskin.lesson9_data_storage.Model.Note;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.DeletedNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.GotNoteCallback;
import com.example.oskin.lesson9_data_storage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ViewNote extends AppCompatActivity implements GotNoteCallback, DeletedNoteCallback {

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_NOTE = "KEY_NOTE";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_CONTENT = "KEY_CONTENT";

    private FloatingActionButton mFab;
    private TextView mTitle;
    private TextView mContent;
    private LoadDataAsyncTask mAsyncTask;
    private Note mNote;
    private boolean mDisableButtonFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        initViews();
        initFab();

        //сделать в остальных такой же
        initToolbar();

        mNote = getIntent().getParcelableExtra(KEY_NOTE);
        loadNote();
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchAvailabilityViews(true);
    }

    private void initViews() {
        mTitle = findViewById(R.id.view_note_layout_name);
        mContent = findViewById(R.id.view_note_layout_text);
    }

    private void getNote() {
        mAsyncTask = new LoadDataAsyncTask(getApplicationContext(), this, LoadDataAsyncTask.GET_NOTE, mNote);
        mAsyncTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CreateEditNote.RESULT_UPDATE_NOTE){
            if (resultCode == RESULT_OK){
                getNote();
            }
        }
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.note);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initFab() {
        mFab = findViewById(R.id.edit_note_fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                switchAvailabilityViews(false);

                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_NOTE, mNote);
                startActivityForResult(CreateEditNote.newIntent(
                        ViewNote.this, CreateEditNote.RESULT_UPDATE_NOTE, bundle),
                        CreateEditNote.RESULT_UPDATE_NOTE);
            }
        });
    }

    private void loadNote() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingActivity.TEXT_PREFERENCES,
                Context.MODE_PRIVATE);

        mTitle.setText(mNote.getName());
        if (sharedPreferences.contains(SettingActivity.TEXT_PREFERENCES_TITLE_SIZE))
            mTitle.setTextSize(sharedPreferences.getInt(SettingActivity.TEXT_PREFERENCES_TITLE_SIZE,
                    SettingActivity.TEXT_PREFERENCES_TITLE_SMALL));

        mContent.setText(mNote.getText());
        if (sharedPreferences.contains(SettingActivity.TEXT_PREFERENCES_CONTENT_SIZE)) {
            mContent.setTextSize(sharedPreferences.getInt(SettingActivity.TEXT_PREFERENCES_CONTENT_SIZE,
                    SettingActivity.TEXT_PREFERENCES_CONTENT_SMALL));
        }
    }

    public static Intent newIntent(Context context, Note note) {
        Intent intent = new Intent(context, ViewNote.class);
        intent.putExtra(KEY_NOTE, note);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
        menu.findItem(R.id.delete_item).setEnabled(mDisableButtonFlag);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_item){
            LoadDataAsyncTask asyncTask = new LoadDataAsyncTask(ViewNote.this, this,
                    LoadDataAsyncTask.DELETE_NOTE, mNote);
            asyncTask.execute();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onNoteReceived(Note note) {
        mNote = note;
        loadNote();
    }

    private void switchAvailabilityViews(boolean state){
        /**
         * Disable / Enable all view on layout
         */
        ConstraintLayout layout = findViewById(R.id.view_note_layout);
        ViewStateSwitcher viewStateSwitcher = new ViewStateSwitcher();
        viewStateSwitcher.switchViewState(layout,state);

        /**
         * Disable / Enable menu items
         */
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(state);
        mDisableButtonFlag = state;
        invalidateOptionsMenu();
    }

    @Override
    public void onNoteDeleted() {
        onBackPressed();
    }
}
