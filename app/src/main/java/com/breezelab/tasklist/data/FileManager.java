package com.breezelab.tasklist.data;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.breezelab.tasklist.entity.ListFile;
import com.breezelab.tasklist.entity.ListItem;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author Breeze
 */
public class FileManager {
    private String directory = "";
    private static String TAG = "ListFileManager";
    static FileManager instance = new FileManager();
    static Serializer serializer = new Persister();
    private List<ListFile> fileList = new ArrayList<ListFile>();

    public void setDirectory(String newDir){
        directory = newDir;
    }

    public ListItem createItem(int index, boolean checked, String text) {
        ListItem newItem = new ListItem();
        newItem.setChecked(checked);
        newItem.setIndex(index);
        newItem.setText(text);
        return newItem;
    }


    private void getFilesFromDir(File path) {
        Log.d(TAG, "Reading dir " + path);
        if(!path.exists()){
            Log.d(TAG, "Doesn't exist");
            try {
                path.mkdirs();
            } catch (Exception ex){

            }
        }
        Log.d(TAG, "Files in path - " + path.listFiles().length);
        for (File fileEntry : path.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFilesFromDir(fileEntry);
            } else {
                String extension = "";
                String filePath = fileEntry.getPath();
                int i = filePath.lastIndexOf('.');
                if (i > 0) {
                    extension = filePath.substring(i + 1);
                }
                if (extension.equals("xml")) {
                    ListFile listFile = getListFile(filePath);
                    if(listFile != null) {
                        fileList.add(listFile);
                    }
                }
            }
        }
    }

    private ListFile getListFile(String filePath) {
        Log.d(TAG, "Convert xml file" + filePath + "");
        try {
            File source = new File(filePath);
            ListFile listFile = serializer.read(ListFile.class, source);
            Log.d(TAG, "Convert success");
            return listFile;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Convert failed");
            return null;
        }
    }

    public ListFile getListFileByName(String fileName){
        Log.d(TAG, "Getting list file by name");
        return getListFile(directory + "/" + fileName + ".xml");
    }

    public boolean writeListFile(String fileName, ListFile list){
        Log.d(TAG, "Writing file " + directory + "/" + fileName);
        Log.d(TAG, list.toString());
        list.fixTimeOfChanges();
        try {
            File result = new File(directory + "/" + fileName);
            if (!result.exists()){
                result.createNewFile();
            }
            serializer.write(list, result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Map<String, String>> convertToListItems() {
        Log.d(TAG, "Converting to list items");
        Log.d(TAG, "List size" + fileList.size());
        final List<Map<String, String>> listItem =
                new ArrayList<Map<String, String>>(fileList.size());

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        for (final ListFile listFile: fileList) {
            final Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put("Name", listFile.getTitle());
            listItemMap.put("Date", df.format(listFile.getLastChangeDate()));
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }

        return Collections.unmodifiableList(listItem);
    }

    public ListAdapter createListAdapter(Context context) {
        fileList.clear();
        getFilesFromDir(new File(directory));

        final String[] fromMapKey = new String[] {"Name", "Date"};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems();

        return new SimpleAdapter(context, list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
    }

    public static FileManager getInstance(){
        if(instance == null){
            instance = new FileManager();
        }
        return instance;
    }

//        ListFileXML xmlList = new listfilexml.ListFileXML();
//        xmlList.setTitle("Meals");
//        xmlList.setCreateDate(new Date());
//        xmlList.setLastChangeDate(new Date());
//        xmlList.getList().add(createItem(0, true, "Milk"));
//        xmlList.getList().add(createItem(1, false,"Bread"));
//        xmlList.getList().add(createItem(2, false,"Meat"));
//        xmlList.getList().add(createItem(3, false,"Apples"));
//        xmlList.getList().add(createItem(4, false,"Banana"));
//
//        writeListFile("firstList.xml",xmlList);

}



