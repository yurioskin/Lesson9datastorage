package com.example.oskin.lesson9_data_storage.Activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.oskin.lesson9_data_storage.DatabaseManagement.LoadDataAsyncTask;
import com.example.oskin.lesson9_data_storage.MainRecycler.CustomAdapter;
import com.example.oskin.lesson9_data_storage.Model.Note;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.DeletedNoteCallback;
import com.example.oskin.lesson9_data_storage.NoteCallbacks.GotNotesCallback;
import com.example.oskin.lesson9_data_storage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements GotNotesCallback, DeletedNoteCallback {

    private FloatingActionButton mAddFab;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Note> mNoteList;
    private LoadDataAsyncTask mLoadDataAsyncTask;
    private boolean mDisableButtonFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFab();
        initToolbar();
        initRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadDataAsyncTask = new LoadDataAsyncTask(MainActivity.this,this,
                LoadDataAsyncTask.LOAD_NOTES, null);
        mLoadDataAsyncTask.execute();

    }

    private void initRecycler() {
        mRecyclerView = findViewById(R.id.main_note_recycler);
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        mAdapter = new CustomAdapter(MainActivity.this, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setTitle(getString(R.string.main_toolbar_title));
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
    }

    private void initFab() {
        mAddFab = findViewById(R.id.create_note_fab);

        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchAvailabilityViews(false);
                startActivity(CreateEditNote.newIntent(MainActivity.this,
                        CreateEditNote.RESULT_ADD_NOTE,null));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.setting_item).setEnabled(mDisableButtonFlag);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_item){
            startActivity(SettingActivity.newIntent(MainActivity.this));
        }
        return true;
    }

    @Override
    public void onNotesReceived(List<Note> notes) {
        mNoteList = notes;
        mAdapter.loadData(mNoteList);
        switchAvailabilityViews(true);
    }

    @Override
    public void onNoteDeleted() {
        mLoadDataAsyncTask = new LoadDataAsyncTask(MainActivity.this, this,
                LoadDataAsyncTask.LOAD_NOTES, null);
        mLoadDataAsyncTask.execute();
    }

    private void switchAvailabilityViews(boolean state){
        /**
         * Disable / Enable all view on layout
         */
        ConstraintLayout layout = findViewById(R.id.main_layout);
        ViewStateSwitcher viewStateSwitcher = new ViewStateSwitcher();
        viewStateSwitcher.switchViewState(layout,state);

        /**
         * Disable / Enable menu items
         */
        mDisableButtonFlag = state;
        invalidateOptionsMenu();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
