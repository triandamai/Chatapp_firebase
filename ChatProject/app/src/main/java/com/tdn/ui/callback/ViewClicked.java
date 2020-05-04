package com.tdn.ui.callback;

import android.view.View;

import java.util.Objects;

interface ViewClicked {
    void onClick(View v);
}
interface AdapterItemClicked {
    void ItemClicked(Objects data,int position);
}

interface ActionBarClicked{
    void onHomeClick(View v);
    void onActionClick(View v);
}
