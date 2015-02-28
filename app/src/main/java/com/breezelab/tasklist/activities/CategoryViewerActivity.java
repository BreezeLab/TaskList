package com.breezelab.tasklist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.breezelab.tasklist.R;
import com.breezelab.tasklist.data.FileManager;

import java.util.ArrayList;

/**
 * Created by Breeze on 27.02.2015.
 */
public class CategoryViewerActivity  extends Activity {
    private static String TAG = "MainActivity";
    private ListView categoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.categoryviewer);
        //
        FileManager flManager = FileManager.getInstance();

        categoryList = (ListView)findViewById(R.id.categoryList);
        categoryList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, flManager.readCategoryList()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (R.id.categoryList == v.getId()) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String obj = (String) lv.getItemAtPosition(acmi.position);

            menu.add("One");
            menu.add("Two");
            menu.add("Three");
//            menu.add(obj.name);
        }
    }
}
