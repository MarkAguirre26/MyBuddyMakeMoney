package com.virtual.software.mybuddymakemoney;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CardDataSource {
    private final CardDbHelper dbHelper;

    public CardDataSource(Context context) {
        dbHelper = new CardDbHelper(context);
    }

    public long insertCard(Card card) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(CardContract.CardEntry.COLUMN_NAME, card.getName());
            values.put(CardContract.CardEntry.COLUMN_PREDICTION, card.getPrediction());
            values.put(CardContract.CardEntry.COLUMN_BRAIN, card.getBrain());
            values.put(CardContract.CardEntry.COLUMN_INITIALIZE, card.getInitialize());
            values.put(CardContract.CardEntry.COLUMN_SKIP, card.getSkip());
            values.put(CardContract.CardEntry.COLUMN_WAIT, card.getWait());
            return database.insert(CardContract.CardEntry.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Or handle the exception as needed
        }
    }

    public void deleteAllCards() {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            database.delete(CardContract.CardEntry.TABLE_NAME, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    public boolean deleteCard(int cardId) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            String whereClause = CardContract.CardEntry._ID + " = ?";
            String[] whereArgs = {String.valueOf(cardId)};
            int rowsDeleted = database.delete(CardContract.CardEntry.TABLE_NAME, whereClause, whereArgs);
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Or handle the exception as needed
        }
    }

    public List<String> getAllCardNames() {
        List<String> cardNames = new ArrayList<>();
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            String[] columns = {CardContract.CardEntry.COLUMN_NAME};
            try (Cursor cursor = database.query(CardContract.CardEntry.TABLE_NAME, columns, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME));
                        cardNames.add(name);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return cardNames;
    }

    public List<Card> getAllCards() {
        List<Card> cardList = new ArrayList<>();
        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            try (Cursor cursor = database.query(CardContract.CardEntry.TABLE_NAME, null, null, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
                        String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME));
                        String prediction = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_PREDICTION));
                        String brain = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_BRAIN));
                        String Initialize = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INITIALIZE));
                        String skip = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_SKIP));
                        String wait = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_WAIT));

                        Card card = new Card(id, name, prediction, brain, Initialize, skip, wait);
                        cardList.add(card);
                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return cardList;
    }
}
