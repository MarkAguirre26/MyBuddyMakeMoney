package com.virtual.software.mybuddymakemoney;

import java.util.ArrayList;
import java.util.List;

public class Brains {
    public static String STAR_BLAZE = "Star Blaze";
    public static String ONE_TWO_THREE = "One Two Go";
    public static String CHOP_STREAK = "Chop Streak";
    public static String ZIGZAG_STREAK = "ZigZag Streak";

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

    public Brains(Integer id, String name, Boolean isSelected) {
        this.id = id;
        Name = name;
        this.isSelected = isSelected;
    }

    public List<Brains> getBrains() {
        List<Brains> items = new ArrayList<>();
        items.add(new Brains(1, STAR_BLAZE, true));
        items.add(new Brains(2, ONE_TWO_THREE, false));
        items.add(new Brains(3, CHOP_STREAK, false));
        items.add(new Brains(4, ZIGZAG_STREAK, false));
        return items;
    }


}
