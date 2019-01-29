package com.example.oskin.lesson9_data_storage.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oskin.lesson9_data_storage.DatabaseManagement.LoadDataAsyncTask;
import com.example.oskin.lesson9_data_storage.Model.Note;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.LoadedNoteCallback;
import com.example.oskin.lesson9_data_storage.R;
import com.example.oskin.lesson9_data_storage.Utils.KeyboardManagment;

import java.util.Objects;

public class CreateEditNote extends AppCompatActivity implements LoadedNoteCallback {

    public static final int RESULT_ADD_NOTE = 13000;
    public static final int RESULT_UPDATE_NOTE = 13001;
    public static final int RESULT_DEFAULT = 13002;

    public static final String REQUEST_KEY = "REQUEST_KEY";
    public static final String BUNDLE_KEY = "BUNDLE_KEY";

    private TextView mName;
    private TextView mText;
    private Toolbar myToolbar;
    private int mRequestCode;
    private Note mNote;
    private LoadDataAsyncTask mLoadDataAsyncTask;
    private ProgressBar mProgressBar;
    private boolean mDisableButtonFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_note);
        initViews();
        initToolBar();
    }

    private void initToolBar() {
        myToolbar = findViewById(R.id.create_note_toolbar);
        myToolbar.setTitle(getString(R.string.edit_note_toolbar_title));
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setHomeAsUpIndicator(R.drawable.ic_baseline_close_24px);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                onBackPressed();
            }
        });
    }

    private void initViews() {
        mName = findViewById(R.id.note_layout_name);
        mName.requestFocus();

        mText = findViewById(R.id.note_layout_text);
        mProgressBar = findViewById(R.id.create_edit_progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra(BUNDLE_KEY);
        if (bundle != null){
            mNote = bundle.getParcelable(ViewNote.KEY_NOTE);
            assert mNote != null;
            mName.setText(mNote.getName());
            mText.setText(mNote.getText());
        }
    }

    public static Intent newIntent(Context context, Integer requestCode, Bundle bundle) {
        Intent intent = new Intent(context, CreateEditNote.class);
        if (requestCode != null)
            intent.putExtra(REQUEST_KEY,requestCode);
        if (bundle != null)
            intent.putExtra(BUNDLE_KEY,bundle);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_edit_setting_menu, menu);
        menu.findItem(R.id.item_save).setEnabled(mDisableButtonFlag);
        return true;
    }

    private void switchAvailabilityViews(boolean state){
        /**
         * Disable / Enable all view on layout
         */
        ConstraintLayout layout = findViewById(R.id.create_note_layout);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_save){

            switchAvailabilityViews(false);
            mProgressBar.setVisibility(View.VISIBLE);

            mRequestCode = getIntent().getIntExtra(REQUEST_KEY,RESULT_DEFAULT);
            Note note;
            switch (mRequestCode){
                case RESULT_ADD_NOTE:
                    /**
                     * Create new note
                     */
                    note = new Note(mName.getText().toString(),mText.getText().toString());
                    mLoadDataAsyncTask = new LoadDataAsyncTask(CreateEditNote.this,
                            CreateEditNote.this, LoadDataAsyncTask.ADD_NOTE, note);
                    mLoadDataAsyncTask.execute();
                    break;
                case RESULT_UPDATE_NOTE:
                    /**
                     * Update note
                     */
                    mNote.setName(mName.getText().toString());
                    mNote.setText(mText.getText().toString());
                    mLoadDataAsyncTask = new LoadDataAsyncTask(CreateEditNote.this, this,
                            LoadDataAsyncTask.UPDATE_NOTE, mNote);
                    mLoadDataAsyncTask.execute();
                    break;
            }
        }
        return true;
    }

    @Override
    public void onNoteLoaded() {
        setResult(RESULT_OK);
        mProgressBar.setVisibility(View.GONE);
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mLoadDataAsyncTask != null)
        mLoadDataAsyncTask.cancel(true);

        super.onDestroy();
    }
}
