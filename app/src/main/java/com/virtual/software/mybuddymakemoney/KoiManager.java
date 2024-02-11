package com.virtual.software.mybuddymakemoney;

import java.util.ArrayList;
import java.util.List;

public class KoiManager {
    private int id;
    private int level;
    private int base;
    private double value;

    // Constructors

    public KoiManager(int id, int level, int base, double value) {
        this.id = id;
        this.level = level;
        this.base = base;
        this.value = value;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    // toString method
    @Override
    public String toString() {
        return "KoiManager{" +
                "id=" + id +
                ", level=" + level +
                ", base=" + base +
                ", value=" + value +
                '}';
    }


    public List<KoiManager> getAllKoi(double baseBet) {
        List<KoiManager> koiManagerList = new ArrayList<>();
        koiManagerList.add(new KoiManager(1, 1, 1, baseBet * 1));
        koiManagerList.add(new KoiManager(2, 2, 1, baseBet * 1));
        koiManagerList.add(new KoiManager(3, 3, 1, baseBet * 1));
        koiManagerList.add(new KoiManager(4, 4, 2, baseBet * 2));
        koiManagerList.add(new KoiManager(5, 5, 2, baseBet * 2));
        koiManagerList.add(new KoiManager(6, 6, 2, baseBet * 2));
        koiManagerList.add(new KoiManager(7, 7, 3, baseBet * 3));
        koiManagerList.add(new KoiManager(8, 8, 3, baseBet * 3));
        koiManagerList.add(new KoiManager(9, 9, 3, baseBet * 3));
        koiManagerList.add(new KoiManager(10, 10, 4, baseBet * 4));
        koiManagerList.add(new KoiManager(11, 11, 4, baseBet * 4));
        koiManagerList.add(new KoiManager(12, 12, 4, baseBet * 4));
        koiManagerList.add(new KoiManager(13, 13, 5, baseBet * 5));
        koiManagerList.add(new KoiManager(14, 14, 5, baseBet * 5));
        koiManagerList.add(new KoiManager(15, 15, 5, baseBet * 5));
        koiManagerList.add(new KoiManager(16, 16, 6, baseBet * 6));
        koiManagerList.add(new KoiManager(17, 17, 6, baseBet * 6));
        koiManagerList.add(new KoiManager(18, 18, 6, baseBet * 6));
        koiManagerList.add(new KoiManager(19, 19, 7, baseBet * 7));
        koiManagerList.add(new KoiManager(20, 20, 7, baseBet * 7));
        koiManagerList.add(new KoiManager(21, 21, 7, baseBet * 7));
        koiManagerList.add(new KoiManager(22, 22, 8, baseBet * 8));
        koiManagerList.add(new KoiManager(23, 23, 8, baseBet * 8));
        koiManagerList.add(new KoiManager(24, 24, 8, baseBet * 8));
        return koiManagerList;
    }
}
