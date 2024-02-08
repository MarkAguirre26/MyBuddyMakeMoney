package com.virtual.software.mybuddymakemoney;

import android.provider.BaseColumns;

public class CardContract {
    private CardContract() {
        // private constructor to prevent instantiation
    }

    public static class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PREDICTION = "prediction";
        public static final String COLUMN_BRAIN = "brain";
        public static final String COLUMN_INITIALIZE = "Initialize";
        public static final String COLUMN_SKIP = "skip";
        public static final String COLUMN_WAIT = "wait";
    }
}
