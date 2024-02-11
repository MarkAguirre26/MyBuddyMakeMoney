package com.virtual.software.mybuddymakemoney;


import java.util.HashMap;
import java.util.Map;

public class BodyGuard {
    public static final Map<String, String> patternMap = new HashMap<>();

    static {
        patternMap.put("PP", "P");
        patternMap.put("BB", "B");
        patternMap.put("PB", "P");
        patternMap.put("BP", "B");
    }
}
