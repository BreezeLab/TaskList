package com.breezelab.tasklist.entity;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Breeze on 28.02.2015.
 */
@Root(name = "CategoriesFile")
public class CategoriesList {

    @ElementList(entry = "category", inline = true, name = "category")
    private ArrayList<String> categories = new ArrayList<String>();

    public ArrayList<String> getList() {
        return this.categories;
    }

    public void setList(ArrayList<String> newList) {
        this.categories = newList;
    }

    public void init(){
        categories.add("Vegetable");
        categories.add("Milk");
        categories.add("Meat");
    }

    @Override
    public String toString() {
        String returnableString = "";
        for (int i = 0; i < categories.size(); i++){
            returnableString += categories.get(i) + "\n";
        }
        return returnableString;

    }
}
