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
 * Management of the Database of the Application
 * All Activity and Class share the same instance of the db to avoid any errors.
 *
 * Inside of the DB :
 *
 * Table MANGAS : id, manga_title, manga_price, manga_editor,manga_category, author_id
 * Table TOMES : id, tome_picture, tome_desc, tome_numero, tome_buy, manga_id
 * Table AUTHORS : id, author_name
 * Table FAVORITES : id, manga_id, manga_followed
 *
 * Relation between tables :
 *
 * An authors can have many mangas.
 * A manga belongs to only one author.
 * A manga can have many tomes.
 * A tome belongs to only one manga.
 *
 * A manga in the favorite table can have two status : follow or not follow
 * follow : New release tomes in the ReleaseActivity will be add automatically for the manga
 * not follow : New release in the ReleaseActivity will not be add for the manga. Otherwise is still a favorite manga.
 *
 */

public class SqLiteHelper extends SQLiteOpenHelper {

    private static SqLiteHelper sInstance;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 9;

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
    private static final String KEY_TOME_BUY = "tome_buy";
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
            KEY_TOME_PICTURE + " TEXT, " + KEY_TOME_BUY + " INTEGER, " + KEY_TOME_MANGA_ID_FK + " INTEGER" + ")";

    // Author table create statement
    private static final String CREATE_TABLE_AUTHOR = "CREATE TABLE " +
            TABLE_AUTHOR + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_AUTHOR_NAME + " TEXT" + ")";

    // Favorite table create statement
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
    /**
     * Create the table of the db
     */
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_AUTHOR);
        db.execSQL(CREATE_TABLE_MANGA);
        db.execSQL(CREATE_TABLE_TOME);
        db.execSQL(CREATE_TABLE_FAVORITE);

    }

    @Override
    /**
     * Upgrade the db
     */
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
     * @param manga  MangaClass
     * @return long  id of the row create for the manga
     */
    public long createManga(MangaClass manga) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.MangaExists(manga.getTitle())) { // if the manga exist do not create return 0 instead of return the id.
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
     * Getting single manga with the title
     *
     * @param titleManga String
     * @return MangaClass if found in the db or null if it is not in the db
     */
    public MangaClass getManga(String titleManga) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"*"};
        String selection = KEY_MANGA_TITLE + " =?";
        String[] selectionArgs = {titleManga,};
        String limit = "1";
        Cursor c = db.query(TABLE_MANGA, columns, selection, selectionArgs, null, null, null, limit);

        MangaClass mg = null;
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
     * Provide the id of a manga with the title or 0 if not in the db
     *
     * @param titleManga String
     * @return manga_id int
     */
    public int getManga_id(String titleManga) {
        SQLiteDatabase db = this.getReadableDatabase();
        int manga_id = 0;
        String[] columns = {KEY_ID};
        String selection = KEY_MANGA_TITLE + " =?";
        String[] selectionArgs = {titleManga,};
        String limit = "1";
        Cursor c = db.query(TABLE_MANGA, columns, selection, selectionArgs, null, null, null, limit);

        if (c.getCount() > 0) {
            c.moveToNext();
            manga_id = c.getInt(c.getColumnIndex(KEY_ID));
            return manga_id;
        }
        return manga_id;
    }

    /**
     * Getting all mangas from the db
     *
     * @return List<MangaClass>
     */
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
    public void deleteManga(long manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MANGA, KEY_ID + " = ?",
                new String[]{String.valueOf(manga_id)});

    }

    /**
     * Search if Manga already in the table with the title
     *
     * @param titleManga String
     * @return boolean
     */
    public boolean MangaExists(String titleManga) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_MANGA_TITLE};
        String selection = KEY_MANGA_TITLE + " LIKE?";
        String[] selectionArgs = {titleManga};
        String limit = "1";
        Cursor cursor = db.query(TABLE_MANGA, columns, selection, selectionArgs, null, null, null, limit);
        cursor.moveToFirst();
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    /** get followed to turn star in grey or yellow. favorite or not ? from table favorites
     *
     * To know if a manga is follow by the user. The manga can be in the favorite and not be followed.
     * @param id   int Id of the manga
     * @return int return the status of the followed columns of the db. 0 or 1 : not follow or follow
     */
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




    /**
     * Creating a Tome
     *
     * @param tome  TomeClass params contains the tome to insert in the db
     * @param manga_id  int id of the manga in which the tome belongs to it.
     * @return  long  new id of the create row with the given tome.
     */
    public long createTome(TomeClass tome, int manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.TomeExists(tome.getNum_vol(), manga_id)) {
            ContentValues values = new ContentValues();
            values.put(KEY_TOME_PICTURE, tome.getImage());
            values.put(KEY_TOME_DESC, tome.getDesc());
            values.put(KEY_TOME_NUM, tome.getNum_vol());
            values.put(KEY_TOME_MANGA_ID_FK, manga_id);
            values.put(KEY_TOME_BUY, 0);
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
            tm.setIsBuy(c.getInt(c.getColumnIndex(KEY_TOME_BUY)));
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
                tm.setIsBuy(c.getInt(c.getColumnIndex(KEY_TOME_BUY)));
                tomes.add(tm);
            } while (c.moveToNext());

        }
        return tomes;
    }

    /**
     * Updating a manga
     * Needed for testing. Do not use in the application.
     * @param tm
     * @return
     */
    public int updateTome(TomeClass tm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOME_PICTURE, tm.getImage());
        values.put(KEY_TOME_DESC, tm.getDesc());
        values.put(KEY_TOME_NUM, tm.getNum_vol());
        values.put(KEY_TOME_MANGA_ID_FK, tm.getManga_id());
        values.put(KEY_TOME_BUY, tm.getIsBuy());
        return db.update(TABLE_TOME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(tm.getTome_id())});
    }

    /**
     * Deleting a tome
     * Needed for testing. Do not use in the application.
     * @param tome_id
     */
    public void deleteTome(long tome_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOME, KEY_ID + " = ?",
                new String[]{String.valueOf(tome_id)});
    }

    /**
     * Delete all of the tomes belongs to a manga
     * @param manga_id  id of the manga which be delete too
     */
    public void deleteAllTomes(long manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOME, KEY_TOME_MANGA_ID_FK + "= ?",
                new String[]{String.valueOf(manga_id)});
    }

    /**
     * Search a tome of a manga if already in the db
     * @param num_vol    String number of the tome to find in the db
     * @param manga_id  int id of the manga in which the tome belongs to it.
     * @return
     */
    public boolean TomeExists(String num_vol, int manga_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_TOME_NUM};
        String selection = KEY_TOME_NUM + " =?" + " AND " + KEY_TOME_MANGA_ID_FK + " = " + manga_id;
        String[] selectionArgs = {num_vol};
        String limit = "1";
        Cursor cursor = db.query(TABLE_TOME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    /**
     * Return true or false if the manga is buy by the user useful for the MangaActivity
     *
     * @param num  String numero of the volume
     * @return boolean
     */
    public boolean isBuy(String num) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_TOME_BUY + " FROM " + TABLE_TOME +
                " WHERE " + KEY_TOME_NUM + " LIKE " + "'" + num + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.getCount() > 0) {
            c.moveToNext();
            int buy = c.getInt(c.getColumnIndex(KEY_TOME_BUY));
            c.close();
            if (buy == 1)
                return true;
            else
                return false;
        }
        return false;
    }

    /**
     *
     * @param isbuy int  status to insert. If is buy or not
     * @param num_vol String numero of the tome to update
     * @return  int
     */
    public int updateBuy(int isbuy, String num_vol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TOME_BUY, isbuy);
        return db.update(TABLE_TOME, values, KEY_TOME_NUM + " = ?",
                new String[]{String.valueOf(num_vol)});
    }



    /**
     * Creating an author  with the name
     *
     * @param author_name  String name of the author
     * @return long id of the new row create with the name of the author
     */
    public long createAuthor(String author_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!this.AuthorExists(author_name)) {
            ContentValues values = new ContentValues();
            values.put(KEY_AUTHOR_NAME, author_name);
            long author_id = db.insert(TABLE_AUTHOR, null, values);
            return author_id;
        }
        return 0;
    }

    /**
     * Return the name of the author
     * @param author_id  long Id of the author
     * @return  String  Name of the author to retrieve
     */
    public String getAuthor(long author_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_AUTHOR_NAME + " FROM " + TABLE_AUTHOR +
                " WHERE " + KEY_ID + " = " + author_id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {
            c.moveToNext();
            String name = c.getString(c.getColumnIndex(KEY_AUTHOR_NAME));
            c.close();
            return name;
        }
        c.close();
        return "";

    }

    /**
     *  Return the id of an author with his name
     * @param name  String name of the author
     * @return int  id of the author
     */
    public int getAuthor_id(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        int author_id = 0;
        String[] columns = {KEY_ID};
        String selection = KEY_AUTHOR_NAME + " =?";
        String[] selectionArgs = {name,};
        String limit = "1";
        Cursor c = db.query(TABLE_AUTHOR, columns, selection, selectionArgs, null, null, null, limit);

        if (c.getCount() > 0) {
            c.moveToNext();
            author_id = c.getInt(c.getColumnIndex(KEY_ID));
            return author_id;
        }
        return author_id;
    }

    /**
     * Return true or false if the author is in the db  with the name
     * @param name  String name of the author
     * @return boolean
     */
    public boolean AuthorExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_AUTHOR_NAME};
        String selection = KEY_AUTHOR_NAME + " =?";
        String[] selectionArgs = {name,};
        String limit = "1";
        Cursor cursor = db.query(TABLE_AUTHOR, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    /**
     * Create a favorite row  with the id of a manga and the status followed == 1
     * @param manga_id   int id of the manga which will be follow by the user
     * @param followed   int  params put in the columns follow  (0 false 1 true)
     * @return
     */
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

    /**
     *
     * @param manga_id   int id of the favorite manga
     * @param followed  int params put in the columns follow (0 false 1 true)
     * @return
     */
    public int updateFavorite(int manga_id, int followed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE_FOLLOWED, followed);
        return db.update(TABLE_FAVORITE, values, KEY_FAVORITE_MANGA_ID_FK + " = ?",
                new String[]{String.valueOf(manga_id)});
    }

    /**
     *  Delete a favorite row
     * @param manga_id  id of the favorite manga deleted
     */
    public void deleteFavorite(long manga_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_FAVORITE_MANGA_ID_FK + " = ?",
                new String[]{String.valueOf(manga_id)});
    }

    /**
     * Needed for testing. Do not use in the application.
     * @param id
     */
    public void deleteFavorite2(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Find if the manga is in the favorite table or not
     * @param id  int id of the manga
     * @return  boolean  true if in the table false otherwise
     */
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

    /**
     * Close the db when the app is closed
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}



