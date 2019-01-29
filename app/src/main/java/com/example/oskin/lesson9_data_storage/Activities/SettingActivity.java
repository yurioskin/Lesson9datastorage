package com.example.oskin.lesson9_data_storage.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oskin.lesson9_data_storage.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    public static final String TEXT_PREFERENCES = "mysettings";
    public static final String TEXT_PREFERENCES_SIZE = "text_size";
    public static final String TEXT_PREFERENCES_TITLE_SIZE = "title_size";
    public static final String TEXT_PREFERENCES_CONTENT_SIZE = "content_size";

    public static final int TEXT_PREFERENCES_SMALL = 0;
    public static final int TEXT_PREFERENCES_NORMAL = 1;
    public static final int TEXT_PREFERENCES_BIG = 2;

    public static final int TEXT_PREFERENCES_TITLE_SMALL = 18;
    public static final int TEXT_PREFERENCES_CONTENT_SMALL = 16;

    public static final int TEXT_PREFERENCES_TITLE_NORMAL = 22;
    public static final int TEXT_PREFERENCES_CONTENT_NORMAL = 20;

    public static final int TEXT_PREFERENCES_TITLE_BIG = 26;
    public static final int TEXT_PREFERENCES_CONTENT_BIG = 24;

    private TextView mTextView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSharedPreferences = getApplicationContext().getSharedPreferences(TEXT_PREFERENCES, Context.MODE_PRIVATE);
        mTextView = findViewById(R.id.textView);
        mTextView.setText(getString(R.string.note_text_size,
                mSharedPreferences.getInt(TEXT_PREFERENCES_CONTENT_SIZE,TEXT_PREFERENCES_CONTENT_SMALL)));
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        initToolBar();
    }

    private void initToolBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.setting_toolbar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        return  intent;

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,
                R.style.MyDialogLightTheme);
        builder.setTitle(getString(R.string.choose_text_size));

        //list of items
        final String[] items = getResources().getStringArray(R.array.text_size_array);

        // set single choice items
        builder.setSingleChoiceItems(items,
                mSharedPreferences.getInt(TEXT_PREFERENCES_SIZE,0),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                    }
                });

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic here
                        // dismiss dialog too
                        ListView lw = ((AlertDialog)dialog).getListView();
                        int what = lw.getCheckedItemPosition();

                        @SuppressLint("CommitPrefEdits")
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        String s = "";
                        switch (what){
                            case TEXT_PREFERENCES_SMALL:
                                editor.putInt(TEXT_PREFERENCES_SIZE, TEXT_PREFERENCES_SMALL);
                                editor.putInt(TEXT_PREFERENCES_TITLE_SIZE, TEXT_PREFERENCES_TITLE_SMALL);
                                editor.putInt(TEXT_PREFERENCES_CONTENT_SIZE, TEXT_PREFERENCES_CONTENT_SMALL);
                                editor.apply();
                                s = "SMALL";
                                break;
                            case TEXT_PREFERENCES_NORMAL:
                                editor.putInt(TEXT_PREFERENCES_SIZE, TEXT_PREFERENCES_NORMAL);
                                editor.putInt(TEXT_PREFERENCES_TITLE_SIZE, TEXT_PREFERENCES_TITLE_NORMAL);
                                editor.putInt(TEXT_PREFERENCES_CONTENT_SIZE, TEXT_PREFERENCES_CONTENT_NORMAL);
                                editor.apply();
                                s = "NORMAL";
                                break;
                            case TEXT_PREFERENCES_BIG:
                                editor.putInt(TEXT_PREFERENCES_SIZE, TEXT_PREFERENCES_BIG);
                                editor.putInt(TEXT_PREFERENCES_TITLE_SIZE, TEXT_PREFERENCES_TITLE_BIG);
                                editor.putInt(TEXT_PREFERENCES_CONTENT_SIZE, TEXT_PREFERENCES_CONTENT_BIG);
                                editor.apply();
                                s = "BIG";
                                break;
                        }
                        Toast.makeText(getApplicationContext(), "Text size changed: " + s, Toast.LENGTH_SHORT).show();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
