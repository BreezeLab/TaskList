package com.breezelab.tasklist.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Breeze on 28.02.2015.
 */
public class SettingsViewerActivity extends ListActivity {
    String[] settingsItemsString = new String[] { "Category"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, settingsItemsString);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        if(item == "Category"){
            Intent intent = new Intent(SettingsViewerActivity.this, CategoryViewerActivity.class);
            startActivity(intent);
        }
//        super.onListItemClick(l, v, position, id);
    }

}
