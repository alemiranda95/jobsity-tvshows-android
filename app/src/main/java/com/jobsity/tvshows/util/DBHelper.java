package com.jobsity.tvshows.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jobsity.tvshows.TvShowApp;
import com.jobsity.tvshows.domain.model.show.FavoriteShow;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    //Constants
    private static final String DATABASE_NAME = "TVShows.db";
    private static final String SHOWS_TABLE_NAME = "shows";
    private static final String SHOW_COLUMN_ID = "id";
    private static final String SHOW_COLUMN_NAME = "name";
    private static final String SHOW_COLUMN_RATING = "rating";
    private static final String SHOW_COLUMN_GENRE = "genre";
    private static final String SHOW_COLUMN_TIME = "time";
    private static final String SHOW_COLUMN_DAYS = "days";
    private static final String SHOW_COLUMN_SUMMARY = "summary";
    private static final String SHOW_COLUMN_PREMIER = "premier";
    private static final String SHOW_COLUMN_POSTER_ORIGINAL = "poster_original";
    private static final String SHOW_COLUMN_POSTER_MEDIUM = "poster_medium";

    private static DBHelper instance;

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper(TvShowApp.getInstance().getApplicationContext());
        }

        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s( " +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT)",
                SHOWS_TABLE_NAME,
                SHOW_COLUMN_ID,
                SHOW_COLUMN_NAME,
                SHOW_COLUMN_RATING,
                SHOW_COLUMN_GENRE,
                SHOW_COLUMN_TIME,
                SHOW_COLUMN_DAYS,
                SHOW_COLUMN_SUMMARY,
                SHOW_COLUMN_PREMIER,
                SHOW_COLUMN_POSTER_ORIGINAL,
                SHOW_COLUMN_POSTER_MEDIUM
        ));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Nothing to do
    }

    public void insertFavoriteShow(FavoriteShow favoriteShow) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHOW_COLUMN_ID, favoriteShow.getId());
        contentValues.put(SHOW_COLUMN_NAME, favoriteShow.getName());
        contentValues.put(SHOW_COLUMN_RATING, favoriteShow.getRating());
        contentValues.put(SHOW_COLUMN_GENRE, favoriteShow.getGenre());
        contentValues.put(SHOW_COLUMN_TIME, favoriteShow.getTime());
        contentValues.put(SHOW_COLUMN_DAYS, favoriteShow.getDays());
        contentValues.put(SHOW_COLUMN_SUMMARY, favoriteShow.getSummary());
        contentValues.put(SHOW_COLUMN_PREMIER, favoriteShow.getPremier());
        contentValues.put(SHOW_COLUMN_POSTER_ORIGINAL, favoriteShow.getPosterOriginal());
        contentValues.put(SHOW_COLUMN_POSTER_MEDIUM, favoriteShow.getPosterMedium());
        db.insert(SHOWS_TABLE_NAME, null, contentValues);
    }

    public void removeFavoriteShow(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SHOWS_TABLE_NAME, SHOW_COLUMN_ID+" IN(?)", new String[] {String.valueOf(id)});
    }

    public List<FavoriteShow> getFavoriteShowList() {
        SQLiteDatabase db = getReadableDatabase();
        List<FavoriteShow> favoriteShowList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+SHOWS_TABLE_NAME+" ORDER BY "+SHOW_COLUMN_NAME, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(SHOW_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_NAME));
            String rating = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_RATING));
            String genre = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_GENRE));
            String time = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_TIME));
            String days = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_DAYS));
            String summary = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_SUMMARY));
            String premier = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_PREMIER));
            String posterOriginal = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_POSTER_ORIGINAL));
            String posterMedium = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_POSTER_MEDIUM));
            FavoriteShow favoriteShow = new FavoriteShow(
                    id,
                    name,
                    posterOriginal,
                    posterMedium,
                    time,
                    days,
                    genre,
                    summary,
                    rating,
                    premier
            );
            favoriteShowList.add(favoriteShow);
            cursor.moveToNext();
        }
        cursor.close();

        return favoriteShowList;
    }

    public FavoriteShow getFavoriteShowbyId(int id) {
        SQLiteDatabase db = getReadableDatabase();
        FavoriteShow favoriteShow = null;

        Cursor cursor = db.rawQuery("SELECT * FROM "+SHOWS_TABLE_NAME+" WHERE "+SHOW_COLUMN_ID+" = ?",
                new String[] {String.valueOf(id)});
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            String name = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_NAME));
            String rating = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_RATING));
            String genre = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_GENRE));
            String time = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_TIME));
            String days = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_DAYS));
            String summary = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_SUMMARY));
            String premier = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_PREMIER));
            String posterOriginal = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_POSTER_ORIGINAL));
            String posterMedium = cursor.getString(cursor.getColumnIndex(SHOW_COLUMN_POSTER_MEDIUM));
            favoriteShow = new FavoriteShow(
                    id,
                    name,
                    posterOriginal,
                    posterMedium,
                    time,
                    days,
                    genre,
                    summary,
                    rating,
                    premier
            );
        }
        cursor.close();

        return favoriteShow;
    }
}
