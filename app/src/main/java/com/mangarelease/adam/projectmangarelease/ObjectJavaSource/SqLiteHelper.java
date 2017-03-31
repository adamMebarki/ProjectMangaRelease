package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 25/03/2017.
 */

public class SqLiteHelper extends SQLiteOpenHelper {

    private static SqLiteHelper sInstance;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "MangaReleases";

    // Common column names
    private static final String KEY_ID = "id";

    // Tables Names
    private static final String TABLE_MANGA = "mangas";
    private static final String TABLE_TOME = "tomes";
    private static final String TABLE_AUTHOR = "authors";
    private static final String TABLE_FAVORITE = "favorites";

    // MANGAS Table - column names
    private static final String KEY_MANGA_TITLE = "manga_title";
    private static final String KEY_MANGA_PRICE = "manga_price";
    private static final String KEY_MANGA_EDITOR = "manga_editor";
    private static final String KEY_MANGA_CATEGORY = "manga_category";
    private static final String KEY_MANGA_AUTHOR_ID_FK = "author_id";

    // TOMES Table - column names
    private static final String KEY_TOME_PICTURE = "tome_picture";
    private static final String KEY_TOME_DESC = "tome_desc";
    private static final String KEY_TOME_NUM = "tome_numero";
    private static final String KEY_TOME_MANGA_ID_FK = "manga_id";

    // AUTHORS Table - column names
    private static final String KEY_AUTHOR_NAME = "author_name";


    // FAVORITES Table - column names
    private static final String KEY_FAVORITE_MANGA_ID_FK = "manga_id";
    private static final String KEY_FAVORITE_FOLLOWED = "manga_followed";

    // Table Create Statements

    // Manga table create statement
    private static final String CREATE_TABLE_MANGA = "CREATE TABLE " +
            TABLE_MANGA + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_MANGA_TITLE + " TEXT, " + KEY_MANGA_PRICE + " REAL, " +
            KEY_MANGA_EDITOR + " TEXT, " + KEY_MANGA_CATEGORY + " TEXT, " +
            KEY_MANGA_AUTHOR_ID_FK + " INTEGER" + ")";

    // Tome table create statement
    private static final String CREATE_TABLE_TOME = "CREATE TABLE " +
            TABLE_TOME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_TOME_NUM + " TEXT," + KEY_TOME_DESC + " TEXT, " +
            KEY_TOME_PICTURE + " TEXT, " + KEY_TOME_MANGA_ID_FK + " INTEGER" + ")";

    private static final String CREATE_TABLE_AUTHOR = "CREATE TABLE " +
            TABLE_AUTHOR + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_AUTHOR_NAME + " TEXT" + ")";

    private static final String CREATE_TABLE_FAVORITE = "CREATE TABLE " +
            TABLE_FAVORITE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FAVORITE_MANGA_ID_FK + " INTEGER, " + KEY_FAVORITE_FOLLOWED + " INTEGER" + ")";


    public static synchronized SqLiteHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new SqLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private SqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_AUTHOR);
        db.execSQL(CREATE_TABLE_MANGA);
        db.execSQL(CREATE_TABLE_TOME);
        db.execSQL(CREATE_TABLE_FAVORITE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);

        // create new tables
        onCreate(db);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * CREATING A MANGA
     *
     * @param manga
     * @return long
     */
    public long createManga(MangaClass manga) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.MangaExists(manga.getTitle())) {
            ContentValues values = new ContentValues();
            values.put(KEY_MANGA_TITLE, manga.getTitle());
            values.put(KEY_MANGA_PRICE, manga.getPrice());
            values.put(KEY_MANGA_EDITOR, manga.getEditor_name());
            values.put(KEY_MANGA_CATEGORY, manga.getCategory());
            values.put(KEY_MANGA_AUTHOR_ID_FK, manga.getAuthor_id());
            long manga_id = db.insert(TABLE_MANGA, null, values);
            return manga_id;
        }
        return 0;
    }

    /**
     * Getting single manga
     *
     * @param titleManga
     * @return
     */
    // Work
    public MangaClass getManga(String titleManga) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_MANGA +
                " WHERE " + KEY_MANGA_TITLE + " LIKE " + "'" + titleManga + "'";
        MangaClass mg = null;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            mg = new MangaClass();
            mg.setManga_id(c.getInt(c.getColumnIndex(KEY_ID)));
            mg.setTitle(c.getString(c.getColumnIndex(KEY_MANGA_TITLE)));
            mg.setPrice(c.getDouble(c.getColumnIndex(KEY_MANGA_PRICE)));
            mg.setEditor_name(c.getString(c.getColumnIndex(KEY_MANGA_EDITOR)));
            mg.setCategory(c.getString(c.getColumnIndex(KEY_MANGA_CATEGORY)));
            mg.setAuthor_id(c.getInt(c.getColumnIndex(KEY_MANGA_AUTHOR_ID_FK)));
            return mg;
        } else {
            return mg;
        }
    }

    /**
     * Getting all mangas
     *
     * @return
     */
    // Work
    public List<MangaClass> getAllMangas() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MangaClass> mangas = new ArrayList<MangaClass>();
        String selectQuery = "SELECT * FROM " + TABLE_MANGA;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                MangaClass mg = new MangaClass();
                mg.setManga_id(c.getInt(c.getColumnIndex(KEY_ID)));
                mg.setTitle(c.getString(c.getColumnIndex(KEY_MANGA_TITLE)));
                mg.setPrice(c.getDouble(c.getColumnIndex(KEY_MANGA_PRICE)));
                mg.setEditor_name(c.getString(c.getColumnIndex(KEY_MANGA_EDITOR)));
                mg.setCategory(c.getString(c.getColumnIndex(KEY_MANGA_CATEGORY)));
                mg.setAuthor_id(c.getInt(c.getColumnIndex(KEY_MANGA_AUTHOR_ID_FK)));
                mangas.add(mg);
            } while (c.moveToNext());

        }
        return mangas;
    }

    /**
     * Updating a manga
     *
     * @param mg
     * @return
     */
    //work thinking about id=1 -> No author
    public int updateManga(MangaClass mg) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MANGA_TITLE, mg.getTitle());
        values.put(KEY_MANGA_PRICE, mg.getPrice());
        values.put(KEY_MANGA_EDITOR, mg.getEditor_name());
        values.put(KEY_MANGA_CATEGORY, mg.getCategory());
        values.put(KEY_MANGA_AUTHOR_ID_FK, mg.getAuthor_id());
        return db.update(TABLE_MANGA, values, KEY_ID + " = ?",
                new String[]{String.valueOf(mg.getManga_id())});

    }

    /**
     * Deleting a manga
     *
     * @param manga_id
     */
    //work
    public void deleteManga(long manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MANGA, KEY_ID + " = ?",
                new String[]{String.valueOf(manga_id)});

    }


    /**
     * Creating a Tome
     *
     * @param tome
     * @param manga_id
     * @return
     */
    //work
    public long createTome(TomeClass tome, int manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.TomeExists(tome.getNum_vol(), manga_id)) {
            ContentValues values = new ContentValues();
            values.put(KEY_TOME_PICTURE, tome.getImage());
            values.put(KEY_TOME_DESC, tome.getDesc());
            values.put(KEY_TOME_NUM, tome.getNum_vol());
            values.put(KEY_TOME_MANGA_ID_FK, manga_id);
            long tome_id = db.insert(TABLE_TOME, null, values);
            return tome_id;
        }
        return 0;
    }

    /**
     * Getting a single tome
     *
     * @param manga_id
     * @param num_vol
     * @return
     */
    //Work
    public TomeClass getTome(int manga_id, String num_vol) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TOME +
                " WHERE " + KEY_TOME_NUM + " LIKE " + "'" + num_vol + "'" + " AND " +
                KEY_TOME_MANGA_ID_FK + " = " + manga_id + ";";
        TomeClass tm = null;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            tm = new TomeClass();
            tm.setTome_id(c.getInt(c.getColumnIndex(KEY_ID)));
            tm.setImage(c.getString(c.getColumnIndex(KEY_TOME_PICTURE)));
            tm.setDesc(c.getString(c.getColumnIndex(KEY_TOME_DESC)));
            tm.setNum_vol(c.getString(c.getColumnIndex(KEY_TOME_NUM)));
            return tm;
        } else {
            return tm;
        }
    }

    /**
     * Getting all volumes of Manga
     *
     * @param manga_id
     * @return
     */
    //Work
    public List<TomeClass> getAllVolumes(int manga_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TomeClass> tomes = new ArrayList<TomeClass>();
        String selectQuery = "SELECT * FROM " + TABLE_TOME +
                " WHERE " + KEY_TOME_MANGA_ID_FK + " = " + manga_id + ";";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TomeClass tm = new TomeClass();
                tm.setTome_id(c.getInt(c.getColumnIndex(KEY_ID)));
                tm.setImage(c.getString(c.getColumnIndex(KEY_TOME_PICTURE)));
                tm.setDesc(c.getString(c.getColumnIndex(KEY_TOME_DESC)));
                tm.setNum_vol(c.getString(c.getColumnIndex(KEY_TOME_NUM)));
                tomes.add(tm);
            } while (c.moveToNext());

        }
        return tomes;
    }

    /**
     * Updating a manga
     *
     * @param tm
     * @return
     */
    //work
    public int updateTome(TomeClass tm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOME_PICTURE, tm.getImage());
        values.put(KEY_TOME_DESC, tm.getDesc());
        values.put(KEY_TOME_NUM, tm.getNum_vol());
        values.put(KEY_TOME_MANGA_ID_FK, tm.getManga_id());
        return db.update(TABLE_TOME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(tm.getTome_id())});
    }

    /**
     * Deleting a manga
     *
     * @param tome_id
     */
    public void deleteTome(long tome_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOME, KEY_ID + " = ?",
                new String[]{String.valueOf(tome_id)});
    }

    /**
     * Creating an author
     *
     * @param author_name
     * @return
     */
    public long createAuthor(String author_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR_NAME, author_name);
        long author_id = db.insert(TABLE_AUTHOR, null, values);
        return author_id;
    }


    public long createFavorite(int manga_id, int followed) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.FavoriteExists(manga_id)) {
            ContentValues values = new ContentValues();
            values.put(KEY_FAVORITE_MANGA_ID_FK, manga_id);
            values.put(KEY_FAVORITE_FOLLOWED, followed);
            long fav_id = db.insert(TABLE_FAVORITE, null, values);
            return fav_id;
        }
        return 0;
    }

    public int updateFavorite(int manga_id, int followed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE_FOLLOWED, followed);
        return db.update(TABLE_FAVORITE, values, KEY_FAVORITE_MANGA_ID_FK + " = ?",
                new String[]{String.valueOf(manga_id)});
    }

    /**
     * Search if Manga already in the table
     *
     * @param manga_id
     * @return
     */

    public void deleteFavorite(long manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_FAVORITE_MANGA_ID_FK + " = ?",
                new String[]{String.valueOf(manga_id)});
    }

    public void deleteFavorite2(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Search if Manga already in the table
     *
     * @param searchItem
     * @return
     */
    //work
    public boolean MangaExists(String searchItem) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_MANGA_TITLE};
        String selection = KEY_MANGA_TITLE + " =?";
        String[] selectionArgs = {searchItem};
        String limit = "1";

        Cursor cursor = db.query(TABLE_MANGA, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean TomeExists(String searchItem, int manga_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_TOME_NUM};
        String selection = KEY_TOME_NUM + " =?" + " AND " + KEY_TOME_MANGA_ID_FK + " = " + manga_id;
        String[] selectionArgs = {searchItem,};
        String limit = "1";
        Cursor cursor = db.query(TABLE_TOME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // get manga_id from titleManga of the newRelease from table manga

    public boolean FavoriteExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_FAVORITE_MANGA_ID_FK};
        String selection = KEY_FAVORITE_MANGA_ID_FK + " =?";
        String[] selectionArgs = {Integer.toString(id),};
        String limit = "1";
        Cursor cursor = db.query(TABLE_FAVORITE, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;

    }


    public int getManga_id(String titleManga) {
        SQLiteDatabase db = this.getReadableDatabase();
        int manga_id = 0;
        String selectQuery = "SELECT * FROM " + TABLE_MANGA +
                " WHERE " + KEY_MANGA_TITLE + " LIKE " + "'" + titleManga + "%'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            manga_id = c.getInt(c.getColumnIndex(KEY_ID));
            return manga_id;
        }
        return manga_id;
    }
    // get followed to turn star in grey or yellow. favorite or not ? from table favorites

    public int isFollow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_FAVORITE_FOLLOWED + " FROM " + TABLE_FAVORITE +
                " WHERE " + KEY_FAVORITE_MANGA_ID_FK + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            c.moveToNext();
            int mg_followed = c.getInt(c.getColumnIndex(KEY_FAVORITE_FOLLOWED));
            c.close();
            return mg_followed;
        }
        c.close();
        return 0;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}



