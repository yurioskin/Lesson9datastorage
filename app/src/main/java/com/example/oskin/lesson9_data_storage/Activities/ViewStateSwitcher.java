package com.example.oskin.lesson9_data_storage.Activities;

import android.view.View;
import android.view.ViewGroup;

public class ViewStateSwitcher {

    public void switchViewState(ViewGroup layout, Boolean state){
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setEnabled(state);
        }
    }
}
