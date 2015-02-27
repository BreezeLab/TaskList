package com.breezelab.tasklist.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author BreezeLab Valiulov I.
 * Class for saving ListFiles in XML files.
 */

@Root(name = "ListFile")
public class ListFile {

    @Attribute
    private String title;
    @Attribute
    private Date createDate;
    @Attribute
    private Date lastChangeDate;
    @ElementList
    private List<ListItem> items = new ArrayList();

    public List<ListItem> getList() {
        return this.items;
    }

    public void setList(ArrayList<ListItem> newList) {
        this.items = newList;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void fixTimeOfChanges(){
        this.setLastChangeDate(new Date());
    }

    private void setLastChangeDate(Date lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public Date getLastChangeDate() {
        return lastChangeDate;
    }


    public ListFile(String newTitle, List<ListItem> itemList){
        super();
        title = newTitle;
        createDate = new Date();
        items.addAll(itemList);
    }

    public ListFile(){
        super();
    }


    @Override
    public String toString() {
        String returnableString = "";
        for (int i = 0; i < items.size(); i++){
            returnableString += items.toString() + "\n";
        }
        return returnableString;
    }
}



