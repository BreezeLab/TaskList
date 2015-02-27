package com.breezelab.tasklist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.breezelab.tasklist.data.FileManager;
import com.breezelab.tasklist.R;

import java.util.Map;


public class FileViewerActivity extends Activity {
    private static String TAG = "MainActivity";
    FileManager lfManager = FileManager.getInstance();

    private TextView mAddButton;
    private TextView mExitButton;
//    private Button mEditButton;
    private ListView mainList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String packageName = getApplicationContext().getPackageName();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/" + packageName;
        lfManager.setDirectory(path);

        ListAdapter adapter = lfManager.createListAdapter(this);
        mainList = (ListView) findViewById(R.id.listView);
        mainList.setAdapter(adapter);
        mainList.setSelection(0);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(FileViewerActivity.this, ListViewerActivity.class);
                    Bundle b = new Bundle();
                    Log.d(TAG, ((Map<String,String>)parent.getItemAtPosition(position)).get("Name"));
                    Log.d(TAG, "" + parent.getItemAtPosition(position).getClass() + "");
//                    Log.d(TAG, "" + parent.getChildCount() + "");
//                    Log.d(TAG, ((TwoLineListItem) parent.getChildAt(1)).getText2().toString());

                    b.putString("FileName", ((Map<String, String>) parent.getItemAtPosition(position)).get("Name")); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
//                    finish();



                }
        });




        mAddButton = (TextView)findViewById(R.id.addButton);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FileViewerActivity.this,
                               R.string.add_toast,
                               Toast.LENGTH_SHORT).show();
                Log.d("Add", "add new list");
                Intent i = new Intent(FileViewerActivity.this, ListViewerActivity.class);
                Bundle b = new Bundle();
                b.putString("FileName", ""); //Your id
                i.putExtras(b);
                startActivity(i);
            }
        });

        mExitButton = (TextView)findViewById(R.id.exitButton);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FileViewerActivity.this,
                        R.string.exit_toast,
                        Toast.LENGTH_SHORT).show();
                Log.d("Com", "exit");
                finish();
                System.exit(0);
            }
        });

//        mEditButton = (TextView)findViewById(R.id.editButton);
//        mEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,
//                        R.string.edit_toast,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resume main activity");
        refreshList();
    }

    private void refreshList(){
//        mainList.setAdapter(lfManager.createListAdapter(getApplicationContext()));
    }

    private void getList(){
        String workingDirectory = System.getProperty("user.dir");
//        System.out.println(workingDirectory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
