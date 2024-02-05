package com.virtual.software.mybuddymakemoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CardDataSource {
    private SQLiteDatabase database;
    private CardDbHelper dbHelper;

    public CardDataSource(Context context) {
        dbHelper = new CardDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertCard(Card card) {
        ContentValues values = new ContentValues();
        values.put(CardContract.CardEntry.COLUMN_NAME, card.getName());
        values.put(CardContract.CardEntry.COLUMN_PREDICTION, card.getPrediction());
        values.put(CardContract.CardEntry.COLUMN_BRAIN, card.getBrain());
        values.put(CardContract.CardEntry.COLUMN_INITIALIZE, card.getInitialize());
        values.put(CardContract.CardEntry.COLUMN_WAIT, card.getWait());

        return database.insert(CardContract.CardEntry.TABLE_NAME, null, values);
    }

    public void deleteAllCards() {
        database.delete(CardContract.CardEntry.TABLE_NAME, null, null);
    }
    public boolean deleteCard(int cardId) {
        String whereClause = CardContract.CardEntry._ID + " = ?";
        String[] whereArgs = {String.valueOf(cardId)};

        int rowsDeleted = database.delete(CardContract.CardEntry.TABLE_NAME, whereClause, whereArgs);

        return rowsDeleted > 0;
    }
    public List<String> getAllCardNames() {
        List<String> cardNames = new ArrayList<>();

        String[] columns = {CardContract.CardEntry.COLUMN_NAME};
        Cursor cursor = database.query(
                CardContract.CardEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME));
                cardNames.add(name);

                cursor.moveToNext();
            }

            cursor.close();
        }

        return cardNames;
    }
    public List<Card> getAllCards() {
        List<Card> cardList = new ArrayList<>();

        Cursor cursor = database.query(
                CardContract.CardEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
                String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_NAME));
                String prediction = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_PREDICTION));
                String brain = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_BRAIN));
                String Initialize = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_INITIALIZE));
                String wait = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_WAIT));

                Card card = new Card(id, name, prediction, brain, Initialize, wait);
                cardList.add(card);

                cursor.moveToNext();
            }

            cursor.close();
        }

        return cardList;
    }
}
