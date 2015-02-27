package com.breezelab.tasklist.entity;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 *
 * @author Breeze
 */
@Root(name = "item")
public class ListItem {

    @Element
    private String text;
    @Attribute
    private boolean checked;
    @Attribute
    private int index;

    public String getText() {
        return text;
    }

    public int getIndex() {
        return index;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setText(String t) {
        text = t;
    }

    public void setIndex(int i) {
        index = i;
    }

    public void setChecked(boolean c) {
        checked = c;
    }

    public ListItem() {
        super();
    }

    @Override
    public String toString() {
        return checked + "|" + index + "|" + text;
    }
}
