package com.breezelab.tasklist.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

import com.breezelab.tasklist.data.FileManager;
import com.breezelab.tasklist.R;
import com.breezelab.tasklist.entity.ListFile;
import com.breezelab.tasklist.entity.ListItem;
import com.breezelab.tasklist.secondary.ClickableString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Breeze on 29.12.2014.
 */
public class ListViewerActivity extends Activity {
    final String TAG = "ListFileView";
    boolean isMarkingMode = false;
    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    LinearLayout mainLayout;
    EditText editWidget;
    TextView addButton;
    TextView deleteButton;
    TextView modeButton;
    ArrayList<Integer> editIdList = new ArrayList<Integer>();
    ArrayList<Integer> layoutIdList = new ArrayList<Integer>();
    ArrayList<Integer> checkboxIdList = new ArrayList<Integer>();
    TextView titleText;

    LinearLayout getFocusedLayout(){
        EditText focusedEdit = null;
        ArrayList<Integer> toDelete = new ArrayList<Integer>();
        for (int i = 0; i < editIdList.size(); i++)
        {
            Log.d(TAG, "Finding focused edit: " + i);
            EditText curEdit = (EditText)findViewById(editIdList.get(i));
            if(curEdit != null && curEdit.isFocused())
            {
                Log.d(TAG, "Edit: " + curEdit.getId() + " is focused");
                focusedEdit = curEdit;
                break;
            }
            else if(curEdit == null){
                toDelete.add(i);
            }
        }

        for(int i = 0; i < toDelete.size(); i++){
            editIdList.remove(i);
        }
        if(focusedEdit == null)
        {
            Log.d(TAG,"There is no focus edit");
            return null;
        }
        Log.d(TAG,"There is focus edit");
        return (LinearLayout)focusedEdit.getParent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listfileviewer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Text View
        editWidget = (EditText)findViewById(R.id.editText);
        mainLayout = (LinearLayout)findViewById(R.id.mainlayout);
        titleText = (TextView)findViewById(R.id.title);

        addButton = (TextView)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {
                    LinearLayout newLayout = new LinearLayout(ListViewerActivity.this);
                    newLayout.setId(newLayout.generateViewId());
                    layoutIdList.add(newLayout.getId());
                    int parentId = newLayout.getId();
                    boolean isContains = layoutIdList.contains(parentId);
                    Log.d(TAG, "child count:" + newLayout.getChildCount() +", parentLayoutID " + parentId + ", " + isContains + "");
                    CheckBox newCheckBox = new CheckBox(ListViewerActivity.this);
                    newCheckBox.setId(newCheckBox.generateViewId());
                    checkboxIdList.add(newCheckBox.getId());
                    newCheckBox.setText("");
                    newLayout.addView(newCheckBox);
                    EditText newEditText = new EditText(ListViewerActivity.this);
                    newEditText.setId(newEditText.generateViewId());
                    editIdList.add(newEditText.getId());
                    newEditText.addTextChangedListener(new CustomTextWatcher(newEditText));
                    newLayout.addView(newEditText);
                    mainLayout.refreshDrawableState();
                    newEditText.requestFocus();

                    LinearLayout focusedLayout = getFocusedLayout();
                    if(focusedLayout == null)
                    {
                        mainLayout.addView(newLayout, mainLayout.indexOfChild(focusedLayout)+1);
                    }
                    else {
                        mainLayout.addView(newLayout);
                    }

                }
            });
        deleteButton = (TextView)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int focusIndex = -1;
                LinearLayout parentLayout = getFocusedLayout();
                if(parentLayout==null){
                    return;
                }
                int parentId = parentLayout.getId();
                boolean isContains = layoutIdList.contains(parentId);
                Log.d(TAG, "child count:" + parentLayout.getChildCount() +", parentLayoutID " + parentId + ", " + isContains + "");
                int curIndex = mainLayout.indexOfChild(parentLayout);
                if (mainLayout.getChildCount() > 1){
                    if(curIndex == 0){
                        focusIndex = 0;
                    }
                    else {
                        focusIndex = curIndex - 1;
                    }
                }
                Log.d(TAG, "focusIndex" + focusIndex + ", curIndex" + curIndex + ", child count:" + mainLayout.getChildCount() +"");
                Log.d(TAG, "Removing view: " + mainLayout.getChildCount() +"");
                mainLayout.removeView(parentLayout);
                Log.d(TAG, "Removed view:" + mainLayout.getChildCount() +"");
                if(focusIndex != -1) {
                    LinearLayout newFocusLayout = (LinearLayout) mainLayout.getChildAt(focusIndex);
                    View newFocusEdit = newFocusLayout.getChildAt(1);
                    if(newFocusEdit != null)
                    {
                        newFocusEdit.requestFocus();
                    }
                }
            }
        });
        modeButton = (TextView)findViewById(R.id.modeButton);
        modeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isMarkingMode == false){
                    Log.d(TAG, "IsMarking else" + isMarkingMode + "");
                    isMarkingMode = true;
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.listFileViewer);
                    linearLayout.setBackgroundResource(R.drawable.marking_background);

                    LinearLayout bottomBarLayout = (LinearLayout)findViewById(R.id.bottomBar);
                    bottomBarLayout.setBackgroundResource(R.drawable.marking_background);

                    Iterator it = editIdList.iterator();
                    while(it.hasNext()){
                        Integer editId = (Integer)it.next();
                        EditText curEdit = (EditText)findViewById(editId);
                        curEdit.setEnabled(false);
                    }

                    it = checkboxIdList.iterator();
                    while(it.hasNext()){
                        Integer checkBoxId = (Integer)it.next();
                        CheckBox curCheckbox = (CheckBox)findViewById(checkBoxId);
                        curCheckbox.setEnabled(false);
                    }

//                    ArrayList<Integer> layoutIdList = new ArrayList<Integer>();
                } else {
                    Log.d(TAG, "IsMarking else" + isMarkingMode + "");
                    isMarkingMode = false;
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.listFileViewer);
                    linearLayout.setBackgroundResource(R.drawable.editing_background);

                    LinearLayout bottomBarLayout = (LinearLayout)findViewById(R.id.bottomBar);
                    bottomBarLayout.setBackgroundResource(R.drawable.editing_background);

                    Iterator it = editIdList.iterator();
                    while(it.hasNext()){
                        Integer editId = (Integer)it.next();
                        EditText curEdit = (EditText)findViewById(editId);
                        curEdit.setEnabled(true);
                    }

                    it = checkboxIdList.iterator();
                    while(it.hasNext()){
                        Integer checkBoxId = (Integer)it.next();
                        CheckBox curCheckbox = (CheckBox)findViewById(checkBoxId);
                        curCheckbox.setEnabled(true);
                    }
                }
            }
        });


        editWidget.addTextChangedListener(new CustomTextWatcher(editWidget));

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            String fileName = b.getString("FileName");
            if (fileName.equals("")) {
                editIdList.add(editWidget.getId());
            } else {
                Log.d(TAG, "Removing edit widget");
                LinearLayout fristLine = (LinearLayout)findViewById(R.id.linelayout);
                mainLayout.removeView(fristLine);
                readFile(fileName);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void readFile(String fileName)
    {
        Log.d(TAG, "Opening file");
        FileManager lfManager = FileManager.getInstance();
        ListFile listFile = lfManager.getListFileByName(fileName);
        Log.d(TAG, "Listfile " + listFile.toString());
        titleText.setText(listFile.getTitle());
        List<ListItem> itemList = listFile.getList();
        if(itemList.isEmpty()) {
            return;
        }
        for(ListItem curItem : itemList){
            LinearLayout newLayout = new LinearLayout(ListViewerActivity.this);
            newLayout.setId(newLayout.generateViewId());
            layoutIdList.add(newLayout.getId());
            int parentId = newLayout.getId();
            boolean isContains = layoutIdList.contains(parentId);
            Log.d(TAG, "child count:" + newLayout.getChildCount() +", parentLayoutID " + parentId + ", " + isContains + "");
            CheckBox newCheckBox = new CheckBox(ListViewerActivity.this);
            newCheckBox.setId(newCheckBox.generateViewId());
            checkboxIdList.add(newCheckBox.getId());
            newCheckBox.setChecked(curItem.isChecked());
            newLayout.addView(newCheckBox);
            EditText newEditText = new EditText(ListViewerActivity.this);
            newEditText.setText(curItem.getText());
            newEditText.setId(newEditText.generateViewId());
//            newEditText.setTextColor(getResources().getDrawable(R.drawable.edittext_style));
            editIdList.add(newEditText.getId());
            newEditText.addTextChangedListener(new CustomTextWatcher(newEditText));
            newLayout.addView(newEditText);

            mainLayout.addView(newLayout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

//        Log.d(TAG, "Item select " + item.toString());
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);
//        return true;


        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroy activity");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (titleText.getText().toString().equals("")) {
        } else

        {
            String title = titleText.getText().toString();
            List<ListItem> lineList = new ArrayList<ListItem>();
            int lineCount = mainLayout.getChildCount();
            for (int i = 0; i < lineCount; i++) {
                LinearLayout lineLayout = (LinearLayout) mainLayout.getChildAt(i);
                CheckBox lineCheckBox = (CheckBox) lineLayout.getChildAt(0);
                EditText lineEdit = (EditText) lineLayout.getChildAt(1);
                if (lineEdit.getText().toString().equals(""))
                    continue;
                ListItem lineItem = new ListItem();
                lineItem.setChecked(lineCheckBox.isChecked());
                lineItem.setText(lineEdit.getText().toString());
                lineItem.setIndex(i);
                lineList.add(lineItem);
            }
            ListFile newListFile = new ListFile(title, lineList);
            FileManager fileManager = FileManager.getInstance();
            fileManager.writeListFile(title + ".xml", newListFile);
        }
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Do you want to save?");
//        // alert.setMessage("Message");
//
//        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
////                if(titleText.getText().toString().equals("")){
////                    Toast.makeText(getApplicationContext(), "Fill title.", Toast.LENGTH_SHORT);
////                }
//                String title = titleText.getText().toString();
//                List<ListFileItem> lineList = new ArrayList<ListFileItem>();
//                int lineCount = mainLayout.getChildCount();
//                for(int i = 0; i < lineCount; i++)
//                {
//                    LinearLayout lineLayout = (LinearLayout)mainLayout.getChildAt(i);
//                    CheckBox lineCheckBox = (CheckBox)lineLayout.getChildAt(0);
//                    EditText lineEdit = (EditText)lineLayout.getChildAt(1);
//                    if(lineEdit.getText().toString().equals(""))
//                        continue;
//                    ListFileItem lineItem = new ListFileItem();
//                    lineItem.setChecked(lineCheckBox.isChecked());
//                    lineItem.setText(lineEdit.getText().toString());
//                    lineItem.setIndex(i);
//                    lineList.add(lineItem);
//                }
//                ListFileXML newListFile = new ListFileXML(title, lineList);
//                ListFileManager fileManager = ListFileManager.getInstance();
//                fileManager.writeListFile(title + ".xml",newListFile);
//            }
//        });
//
//        alert.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    }
//                });
//
//        alert.show();


        Log.d(TAG, "Pause activity");
        super.onPause();
    }



    class CustomOnKeyListener implements OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if(keyCode == KeyEvent.KEYCODE_DEL){
                if(v.toString().isEmpty()){
                    int focusIndex = -1;
                    LinearLayout parentLayout = (LinearLayout)v.getParent();
                    int curIndex = mainLayout.indexOfChild(parentLayout);
                    if (mainLayout.getChildCount() > 1){
                        if(curIndex == mainLayout.getChildCount()-1){
                            focusIndex = curIndex - 1;
                        }
                        else {
                            focusIndex = 0;
                        }
                    }

                    mainLayout.removeView((LinearLayout)v.getParent());
                    LinearLayout newFocusLayout = (LinearLayout)mainLayout.getChildAt(focusIndex);
                    newFocusLayout.getChildAt(1).requestFocus();

                }
            }
            return false;
        }
    }

    class CustomTextWatcher implements TextWatcher {
        protected EditText currentEditText;

        public CustomTextWatcher(EditText e) {
            currentEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().substring(start).contains("\n")){
                LinearLayout newLayout = new LinearLayout(ListViewerActivity.this);
                newLayout.setId(newLayout.generateViewId());
                layoutIdList.add(newLayout.getId());
                int parentId = newLayout.getId();
                boolean isContains = layoutIdList.contains(parentId);
                Log.d(TAG, "child count:" + newLayout.getChildCount() +", parentLayoutID " + parentId + ", " + isContains + "");
                CheckBox newCheckBox = new CheckBox(ListViewerActivity.this);
                newCheckBox.setId(newCheckBox.generateViewId());
                newCheckBox.setText("");
                newLayout.addView(newCheckBox);
                EditText newEditText = new EditText(ListViewerActivity.this);
                newEditText.setId(newEditText.generateViewId());
                editIdList.add(newEditText.getId());
                newEditText.addTextChangedListener(new CustomTextWatcher(newEditText));
                newLayout.addView(newEditText);
                mainLayout.refreshDrawableState();
                newEditText.requestFocus();

                LinearLayout focusedLayout = getFocusedLayout();
                mainLayout.addView(newLayout, mainLayout.indexOfChild(focusedLayout)+1);
                s = s.subSequence(0, s.length()-1);
            }
        }

        public void afterTextChanged(Editable s) {
            if (s.toString().contains("\n")){
                Log.d(TAG, "consist new line char");
                currentEditText.setText(s.toString().substring(0, s.length() - 1));
            }
        }
    }
}

