package com.virtual.software.mybuddymakemoney;

import java.util.ArrayList;
import java.util.List;

public class Brains {
    public static final String STAR_BLAZE = "Star Blaze";
    public static final String CHOP_STREAK = "Chop Streak";
    public static  final String ZIGZAG_STREAK = "ZigZag Streak";
    public static  final String TIAMAT = "Tiamat";

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String Name;
    private Boolean isSelected;

    public Brains() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Brains (String name, Boolean isSelected) {

        Name = name;
        this.isSelected = isSelected;
    }


    @Override
    public String toString() {
        return "Brains{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}

