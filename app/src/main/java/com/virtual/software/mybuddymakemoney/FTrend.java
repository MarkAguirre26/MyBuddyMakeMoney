package com.virtual.software.mybuddymakemoney;

import java.util.HashMap;
import java.util.Map;

public class FTrend {
    public static final Map<String, String> patternMap = new HashMap<>();

    static {

        patternMap.put("PBP", "B");
        patternMap.put("BPB", "P");
        patternMap.put("PPP", "P");
        patternMap.put("BBB", "B");


    }
}
