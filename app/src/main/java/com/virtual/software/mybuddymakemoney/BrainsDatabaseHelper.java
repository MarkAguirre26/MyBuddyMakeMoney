package com.virtual.software.mybuddymakemoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BrainsDatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "brain_db";

    // Table Name
    private static final String TABLE_NAME = "brain_table";

    // Columns
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_IS_SELECTED = "isSelected";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Create Table Query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT,"
            + COL_IS_SELECTED + " INTEGER" + ")";

    public BrainsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    private boolean isNameExists(SQLiteDatabase db, String name) {
        Cursor cursor = null;
        try {
            // Query to check if the name already exists
            String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_NAME + " = ?";
            cursor = db.rawQuery(query, new String[]{name});
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }
    public boolean save(Brains model) {
        SQLiteDatabase db = this.getWritableDatabase();


        // Check if the name already exists
        if (isNameExists(db, model.getName())) {
            // Name already exists, return false indicating failure to save
            return false;
        }


        ContentValues values = new ContentValues();
        values.put(COL_NAME, model.getName());
        values.put(COL_IS_SELECTED, model.getSelected() ? 1 : 0);

        try {
            // Inserting Row
            long result = db.insert(TABLE_NAME, null, values);
            return result != -1; // if result is -1, insertion failed
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close(); // Closing database connection
        }
    }

    public Brains getFirstItem() {
        Brains model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Select Query for the first item
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";

            cursor = db.rawQuery(selectQuery, null);

            // Check if cursor has any data
            if (cursor.moveToFirst()) {
                model = new Brains();
                model.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                model.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                model.setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return model;
    }


    public List<Brains> getAllSortedByName() {
        List<Brains> models = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_NAME + " ASC";

            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Brains model = new Brains();
                    model.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                    model.setSelected(cursor.getInt(cursor.getColumnIndex(COL_IS_SELECTED)) == 1);
                    // Adding model to list
                    models.add(model);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return models;
    }

    public boolean updateIsSelected(int id, boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IS_SELECTED, isSelected ? 1 : 0);

        try {
            // Updating Row
            int affectedRows = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
            return affectedRows > 0; // if affectedRows > 0, update successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close(); // Closing database connection
        }
    }
    public Brains getSelectedItem() {
        Brains model = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Select Query for an item where isSelected is true
            String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_IS_SELECTED + " = 1 LIMIT 1";

            cursor = db.rawQuery(selectQuery, null);

            // Check if cursor has any data
            if (cursor.moveToFirst()) {
                model = new Brains();
                model.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                model.setName(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                model.setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return model;
    }
    public boolean updateAllIsSelected(boolean isSelected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_IS_SELECTED, isSelected ? 1 : 0);

        try {
            // Updating all rows
            int affectedRows = db.update(TABLE_NAME, values, null, null);
            return affectedRows > 0; // if affectedRows > 0, update successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close(); // Closing database connection
        }
    }

}
