package com.example.oskin.lesson9_data_storage.Utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardManagment {

    private Context mContext;

    public KeyboardManagment(Context context){
        mContext = context;
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
