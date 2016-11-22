package com.mshvdvskgmail.apiicefire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by mshvd_000 on 07.11.2016.
 */

class DBHelper extends SQLiteOpenHelper{

    static String databasePath;
    static final String DATABASE_NAME = "RobotDB.db";
    static final String KEY_ID = "id";

    // Table on actors

    static final String ACTORS_TABLE_NAME = "actors";
    static final String ACTORS_COLUMN_URL = "url";
    static final String ACTORS_COLUMN_NAME = "name";
    static final String ACTORS_COLUMN_GENDER = "gender";
    static final String ACTORS_COLUMN_CULTURE = "culture";
    static final String ACTORS_COLUMN_BORN = "born";
    static final String ACTORS_COLUMN_DIED = "died";
    static final String ACTORS_COLUMN_TITLES = "titles";
    static final String ACTORS_COLUMN_ALIASES = "aliases";
    static final String ACTORS_COLUMN_FATHER = "father";
    static final String ACTORS_COLUMN_MOTHER = "mother";
    static final String ACTORS_COLUMN_SPOUSE = "spouse";
    static final String ACTORS_COLUMN_ALLEGIANCES = "allegiances";
    static final String ACTORS_COLUMN_BOOKS = "books";
    static final String ACTORS_COLUMN_POVBOOKS = "povBooks";
    static final String ACTORS_COLUMN_TVSERIES = "tvSeries";
    static final String ACTORS_COLUMN_PLAYEDBY = "playedBy";

    // Table on books

    static final String BOOKS_TABLE_NAME = "books";
    static final String BOOKS_COLUMN_URL = "url";
    static final String BOOKS_COLUMN_NAME = "name";
    static final String BOOKS_COLUMN_ISBN = "isbn";
    static final String BOOKS_COLUMN_AUTHORS = "authors";
    static final String BOOKS_COLUMN_NUMBEROFPAGES = "numberOfPages";
    static final String BOOKS_COLUMN_PUBLISHER = "publisher";
    static final String BOOKS_COLUMN_COUNTRY = "country";
    static final String BOOKS_COLUMN_MEDIATYPE = "mediaType";
    static final String BOOKS_COLUMN_RELEASED = "released";
    static final String BOOKS_COLUMN_POVCHARACTERS = "povCharacters";

    // Table on links

    static final String LINKS_TABLE_NAME = "links";
    static final String LINKS_COLUMN_NAME = "name";
    static final String LINKS_COLUMN_URL = "url";

    // Table on list of all books

    static final String LIST_TABLE_NAME = "list";
    static final String LIST_COLUMN_NAME = "name";
    static final String LIST_COLUMN_URL = "url";

    // Table on allegiances

    private static final String ALLEGIANCES_TABLE_NAME = "allegiances";
    private static final String ALLEGIANCES_COLUMN_URL = "url";
    private static final String ALLEGIANCES_COLUMN_NAME = "name";
    private static final String ALLEGIANCES_COLUMN_REGION = "region";
    private static final String ALLEGIANCES_COLUMN_COATOFARMS = "coatOfArms";
    private static final String ALLEGIANCES_COLUMN_WORDS = "words";
    private static final String ALLEGIANCES_COLUMN_TITLES = "titles";
    private static final String ALLEGIANCES_COLUMN_SEATS = "seats";
    private static final String ALLEGIANCES_COLUMN_CURRENTLORD = "currentLord";
    private static final String ALLEGIANCES_COLUMN_HEIR = "heir";
    private static final String ALLEGIANCES_COLUMN_OVERLORD = "overlord";
    private static final String ALLEGIANCES_COLUMN_FOUNDED = "founded";
    private static final String ALLEGIANCES_COLUMN_FOUNDER = "founder";
    private static final String ALLEGIANCES_COLUMN_DIEDOUT = "diedOut";
    private static final String ALLEGIANCES_COLUMN_ANCESTRALWEAPONS = "ancestralWeapons";
    private static final String ALLEGIANCES_COLUMN_CADETBRANCHES = "cadetBranches";
    private static final String ALLEGIANCES_COLUMN_SWORNMEMBERS = "swornMembers";

    // Creating strings

    static final String CREATE_TABLE_ACTORS = "CREATE TABLE " + ACTORS_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + ACTORS_COLUMN_URL + " TEXT," + ACTORS_COLUMN_NAME + " TEXT," + ACTORS_COLUMN_GENDER + " TEXT,"
            + ACTORS_COLUMN_CULTURE + " TEXT," + ACTORS_COLUMN_BORN + " TEXT,"  + ACTORS_COLUMN_DIED + " TEXT,"  + ACTORS_COLUMN_TITLES + " TEXT,"
            + ACTORS_COLUMN_ALIASES + " TEXT," + ACTORS_COLUMN_FATHER + " TEXT," + ACTORS_COLUMN_MOTHER + " TEXT," + ACTORS_COLUMN_SPOUSE + " TEXT,"
            + ACTORS_COLUMN_ALLEGIANCES + " TEXT," + ACTORS_COLUMN_BOOKS + " TEXT," + ACTORS_COLUMN_POVBOOKS + " TEXT," + ACTORS_COLUMN_TVSERIES + " TEXT,"
            + ACTORS_COLUMN_PLAYEDBY + " TEXT" + ")";

    static final String CREATE_TABLE_BOOKS = "CREATE TABLE " + BOOKS_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + BOOKS_COLUMN_URL + " TEXT," + BOOKS_COLUMN_NAME + " TEXT," + BOOKS_COLUMN_ISBN + " TEXT," + BOOKS_COLUMN_AUTHORS + " TEXT,"
            + BOOKS_COLUMN_NUMBEROFPAGES + " TEXT," + BOOKS_COLUMN_PUBLISHER + " TEXT," + BOOKS_COLUMN_COUNTRY + " TEXT,"
            + BOOKS_COLUMN_MEDIATYPE + " TEXT," + BOOKS_COLUMN_RELEASED + " TEXT," + BOOKS_COLUMN_POVCHARACTERS + " TEXT" + ")";

    static final String CREATE_TABLE_LINKS = "CREATE TABLE " + LINKS_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + LINKS_COLUMN_NAME + " TEXT,"+ LINKS_COLUMN_URL + " TEXT"+ ")";

    static final String CREATE_TABLE_LIST = "CREATE TABLE " + LIST_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + LIST_COLUMN_NAME + " TEXT,"+ LIST_COLUMN_URL + " TEXT"+ ")";

    static final String CREATE_TABLE_ALLEGIANCES = "CREATE TABLE " + ALLEGIANCES_TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + ALLEGIANCES_COLUMN_URL + " TEXT," + ALLEGIANCES_COLUMN_NAME + " TEXT," + ALLEGIANCES_COLUMN_REGION + " TEXT,"
            + ALLEGIANCES_COLUMN_COATOFARMS + " TEXT," + ALLEGIANCES_COLUMN_WORDS + " TEXT," + ALLEGIANCES_COLUMN_TITLES + " TEXT,"
            + ALLEGIANCES_COLUMN_SEATS + " TEXT," + ALLEGIANCES_COLUMN_CURRENTLORD + " TEXT," + ALLEGIANCES_COLUMN_HEIR + " TEXT,"
            + ALLEGIANCES_COLUMN_OVERLORD + " TEXT," + ALLEGIANCES_COLUMN_FOUNDED + " TEXT," + ALLEGIANCES_COLUMN_FOUNDER + " TEXT,"
            + ALLEGIANCES_COLUMN_DIEDOUT + " TEXT," + ALLEGIANCES_COLUMN_ANCESTRALWEAPONS + " TEXT," + ALLEGIANCES_COLUMN_CADETBRANCHES + " TEXT,"
            + ALLEGIANCES_COLUMN_SWORNMEMBERS + " TEXT" + ")";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        databasePath = context.getDatabasePath("RobotDB.db").getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACTORS);
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_LINKS);
        db.execSQL(CREATE_TABLE_ALLEGIANCES);
        db.execSQL(CREATE_TABLE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ACTORS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+BOOKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+LINKS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ALLEGIANCES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+LIST_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertActorInfo (HashMap<String, String> infoOnActor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTORS_COLUMN_URL, infoOnActor.get(ACTORS_COLUMN_URL));
        values.put(ACTORS_COLUMN_NAME, infoOnActor.get(ACTORS_COLUMN_NAME));
        values.put(ACTORS_COLUMN_GENDER, infoOnActor.get(ACTORS_COLUMN_GENDER));
        values.put(ACTORS_COLUMN_CULTURE, infoOnActor.get(ACTORS_COLUMN_CULTURE));
        values.put(ACTORS_COLUMN_BORN, infoOnActor.get(ACTORS_COLUMN_BORN));
        values.put(ACTORS_COLUMN_DIED, infoOnActor.get(ACTORS_COLUMN_DIED));
        values.put(ACTORS_COLUMN_TITLES, infoOnActor.get(ACTORS_COLUMN_TITLES));
        values.put(ACTORS_COLUMN_ALIASES, infoOnActor.get(ACTORS_COLUMN_ALIASES));
        values.put(ACTORS_COLUMN_FATHER, infoOnActor.get(ACTORS_COLUMN_FATHER));
        values.put(ACTORS_COLUMN_MOTHER, infoOnActor.get(ACTORS_COLUMN_MOTHER));
        values.put(ACTORS_COLUMN_SPOUSE, infoOnActor.get(ACTORS_COLUMN_SPOUSE));
        values.put(ACTORS_COLUMN_ALLEGIANCES, infoOnActor.get(ACTORS_COLUMN_ALLEGIANCES));
        values.put(ACTORS_COLUMN_BOOKS, infoOnActor.get(ACTORS_COLUMN_BOOKS));
        values.put(ACTORS_COLUMN_POVBOOKS, infoOnActor.get(ACTORS_COLUMN_POVBOOKS));
        values.put(ACTORS_COLUMN_TVSERIES, infoOnActor.get(ACTORS_COLUMN_TVSERIES));
        values.put(ACTORS_COLUMN_PLAYEDBY, infoOnActor.get(ACTORS_COLUMN_PLAYEDBY));
        db.insert("actors", null, values);
        return true;
    }

    public boolean insertBookInfo (HashMap<String, String> infoOnBook){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOKS_COLUMN_URL, infoOnBook.get(BOOKS_COLUMN_URL));
        values.put(BOOKS_COLUMN_NAME, infoOnBook.get(BOOKS_COLUMN_NAME));
        values.put(BOOKS_COLUMN_ISBN, infoOnBook.get(BOOKS_COLUMN_ISBN));
        values.put(BOOKS_COLUMN_AUTHORS, infoOnBook.get(BOOKS_COLUMN_AUTHORS));
        values.put(BOOKS_COLUMN_NUMBEROFPAGES, infoOnBook.get(BOOKS_COLUMN_NUMBEROFPAGES));
        values.put(BOOKS_COLUMN_PUBLISHER, infoOnBook.get(BOOKS_COLUMN_PUBLISHER));
        values.put(BOOKS_COLUMN_COUNTRY, infoOnBook.get(BOOKS_COLUMN_COUNTRY));
        values.put(BOOKS_COLUMN_MEDIATYPE, infoOnBook.get(BOOKS_COLUMN_MEDIATYPE));
        values.put(BOOKS_COLUMN_RELEASED, infoOnBook.get(BOOKS_COLUMN_RELEASED));
        values.put(BOOKS_COLUMN_POVCHARACTERS, infoOnBook.get(BOOKS_COLUMN_POVCHARACTERS));
        db.insert("books", null, values);
        return true;
    }

    public boolean insertNameAndLink (String name, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LINKS_COLUMN_NAME, name);
        values.put(LINKS_COLUMN_URL, url);
        db.insert(LINKS_TABLE_NAME, null, values);
        return true;
    }

    public boolean insetBookAndLink (String name, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_COLUMN_NAME, name);
        values.put(LIST_COLUMN_URL, url);
        db.insert(LIST_TABLE_NAME, null, values);
        return true;
    }

    public boolean insertAllegianceInfo (HashMap <String, String> infoOnAllegiance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALLEGIANCES_COLUMN_URL, infoOnAllegiance.get(ALLEGIANCES_COLUMN_URL));
        values.put(ALLEGIANCES_COLUMN_NAME, infoOnAllegiance.get(ALLEGIANCES_COLUMN_NAME));
        values.put(ALLEGIANCES_COLUMN_REGION, infoOnAllegiance.get(ALLEGIANCES_COLUMN_REGION));
        values.put(ALLEGIANCES_COLUMN_COATOFARMS, infoOnAllegiance.get(ALLEGIANCES_COLUMN_COATOFARMS));
        values.put(ALLEGIANCES_COLUMN_WORDS, infoOnAllegiance.get(ALLEGIANCES_COLUMN_WORDS));
        values.put(ALLEGIANCES_COLUMN_TITLES, infoOnAllegiance.get(ALLEGIANCES_COLUMN_TITLES));
        values.put(ALLEGIANCES_COLUMN_SEATS, infoOnAllegiance.get(ALLEGIANCES_COLUMN_SEATS));
        values.put(ALLEGIANCES_COLUMN_CURRENTLORD, infoOnAllegiance.get(ALLEGIANCES_COLUMN_CURRENTLORD));
        values.put(ALLEGIANCES_COLUMN_HEIR, infoOnAllegiance.get(ALLEGIANCES_COLUMN_HEIR));
        values.put(ALLEGIANCES_COLUMN_OVERLORD, infoOnAllegiance.get(ALLEGIANCES_COLUMN_OVERLORD));
        values.put(ALLEGIANCES_COLUMN_FOUNDED, infoOnAllegiance.get(ALLEGIANCES_COLUMN_FOUNDED));
        values.put(ALLEGIANCES_COLUMN_FOUNDER, infoOnAllegiance.get(ALLEGIANCES_COLUMN_FOUNDER));
        values.put(ALLEGIANCES_COLUMN_DIEDOUT, infoOnAllegiance.get(ALLEGIANCES_COLUMN_DIEDOUT));
        values.put(ALLEGIANCES_COLUMN_ANCESTRALWEAPONS, infoOnAllegiance.get(ALLEGIANCES_COLUMN_ANCESTRALWEAPONS));
        values.put(ALLEGIANCES_COLUMN_CADETBRANCHES, infoOnAllegiance.get(ALLEGIANCES_COLUMN_CADETBRANCHES));
        values.put(ALLEGIANCES_COLUMN_SWORNMEMBERS, infoOnAllegiance.get(ALLEGIANCES_COLUMN_SWORNMEMBERS));
        db.insert(ALLEGIANCES_TABLE_NAME, null, values);
        return true;
    }


    public String getLinkByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor linkFinder = db.query(LINKS_TABLE_NAME, null, LINKS_COLUMN_NAME + "=?",
                new String[]{name}, null, null, null, null);

        if (linkFinder != null){
            linkFinder.moveToFirst();
        }
        String result = linkFinder.getString(linkFinder.getColumnIndex(LINKS_COLUMN_URL));
        return result;
    }

    public String [] getListOfAllBooks(){
        SQLiteDatabase db = this.getReadableDatabase();
        String [] result;
        Cursor listGrabber = db.rawQuery("SELECT * FROM list", null);
        if (listGrabber != null){
            result = new String[listGrabber.getCount()];
            String nameHolder;
            listGrabber.moveToFirst();
            for (int i = 0;i<listGrabber.getCount();i++){
                nameHolder = listGrabber.getString(listGrabber.getColumnIndex(LINKS_COLUMN_NAME));
                result[i] = nameHolder;
                listGrabber.moveToNext();
            }
            return result;
        }
        return null;
    }


    public HashMap<String, String> getActorInfo(String url){
        HashMap <String, String> result = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor infoOnActors = db.rawQuery("SELECT * FROM actors WHERE url='"+url+"'",null); -- альтернативная версия
        Cursor infoOnActor = db.query(ACTORS_TABLE_NAME, null, ACTORS_COLUMN_URL + "=?",
                new String[]{url}, null, null, null, null);

        if (infoOnActor != null){
            infoOnActor.moveToFirst();
        }
        result.put(ACTORS_COLUMN_URL, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_URL)));
        result.put(ACTORS_COLUMN_NAME, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_NAME)));
        result.put(ACTORS_COLUMN_GENDER, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_GENDER)));
        result.put(ACTORS_COLUMN_CULTURE, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_CULTURE)));
        result.put(ACTORS_COLUMN_BORN, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_BORN)));
        result.put(ACTORS_COLUMN_DIED, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_DIED)));
        result.put(ACTORS_COLUMN_TITLES, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_TITLES)));
        result.put(ACTORS_COLUMN_ALIASES, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_ALIASES)));
        result.put(ACTORS_COLUMN_FATHER, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_FATHER)));
        result.put(ACTORS_COLUMN_MOTHER, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_MOTHER)));
        result.put(ACTORS_COLUMN_SPOUSE, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_SPOUSE)));
        result.put(ACTORS_COLUMN_ALLEGIANCES, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_ALLEGIANCES)));
        result.put(ACTORS_COLUMN_BOOKS, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_BOOKS)));
        result.put(ACTORS_COLUMN_POVBOOKS, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_POVBOOKS)));
        result.put(ACTORS_COLUMN_TVSERIES, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_TVSERIES)));
        result.put(ACTORS_COLUMN_PLAYEDBY, infoOnActor.getString(infoOnActor.getColumnIndex(ACTORS_COLUMN_PLAYEDBY)));
        return result;
    }

    public HashMap <String, String> getBookInfo(String url){
        HashMap <String, String> result = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor infoOnActors = db.rawQuery("SELECT * FROM actors WHERE url='"+url+"'",null); -- альтернативная версия
        Cursor infoOnBook = db.query(BOOKS_TABLE_NAME, null, BOOKS_COLUMN_URL + "=?",
                new String[]{url}, null, null, null, null);

        if (infoOnBook != null){
            infoOnBook.moveToFirst();
        }
        result.put(BOOKS_COLUMN_URL, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_URL)));
        result.put(BOOKS_COLUMN_NAME, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_NAME)));
        result.put(BOOKS_COLUMN_ISBN, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_ISBN)));
        result.put(BOOKS_COLUMN_AUTHORS, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_AUTHORS)));
        result.put(BOOKS_COLUMN_NUMBEROFPAGES, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_NUMBEROFPAGES)));
        result.put(BOOKS_COLUMN_PUBLISHER, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_PUBLISHER)));
        result.put(BOOKS_COLUMN_COUNTRY, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_COUNTRY)));
        result.put(BOOKS_COLUMN_MEDIATYPE, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_MEDIATYPE)));
        result.put(BOOKS_COLUMN_RELEASED, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_RELEASED)));
        result.put(BOOKS_COLUMN_POVCHARACTERS, infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_POVCHARACTERS)));
        return result;
        //new String("fuck it");
        // infoOnBook.getString(infoOnBook.getColumnIndex(BOOKS_COLUMN_NAME));
    }

    public HashMap <String, String> getAllegianceInfo(String url){
        HashMap <String, String> result = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor infoOnAllegiance = db.query(ALLEGIANCES_TABLE_NAME, null, ALLEGIANCES_COLUMN_URL + "=?",
                new String[]{url}, null, null, null, null);
        if (infoOnAllegiance != null){
            infoOnAllegiance.moveToFirst();
        }
        result.put(ALLEGIANCES_COLUMN_URL, infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_URL))));
        result.put(ALLEGIANCES_COLUMN_NAME, infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_NAME))));
        result.put(ALLEGIANCES_COLUMN_REGION, infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_REGION))));
        result.put(ALLEGIANCES_COLUMN_COATOFARMS, infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_COATOFARMS))));
        result.put(ALLEGIANCES_COLUMN_WORDS , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_WORDS ))));
        result.put(ALLEGIANCES_COLUMN_TITLES , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_TITLES ))));
        result.put(ALLEGIANCES_COLUMN_SEATS , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_SEATS ))));
        result.put(ALLEGIANCES_COLUMN_CURRENTLORD , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_CURRENTLORD ))));
        result.put(ALLEGIANCES_COLUMN_HEIR , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_HEIR ))));
        result.put(ALLEGIANCES_COLUMN_OVERLORD , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_OVERLORD ))));
        result.put(ALLEGIANCES_COLUMN_FOUNDED , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_FOUNDED ))));
        result.put(ALLEGIANCES_COLUMN_FOUNDER , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_FOUNDER ))));
        result.put(ALLEGIANCES_COLUMN_DIEDOUT , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_DIEDOUT ))));
        result.put(ALLEGIANCES_COLUMN_ANCESTRALWEAPONS , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_ANCESTRALWEAPONS ))));
        result.put(ALLEGIANCES_COLUMN_CADETBRANCHES , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_CADETBRANCHES ))));
        result.put(ALLEGIANCES_COLUMN_SWORNMEMBERS , infoOnAllegiance.getString(infoOnAllegiance.getColumnIndex((ALLEGIANCES_COLUMN_SWORNMEMBERS ))));
        return result;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTORS_TABLE_NAME);
        return numRows;
    }

    public int numberOfBookRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows2 = (int) DatabaseUtils.queryNumEntries(db, BOOKS_TABLE_NAME);
        return numRows2;
    }

    public int numberOfBookLinks(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LIST_TABLE_NAME);
        return numRows;
    }

    public int numberOfLinks(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LINKS_TABLE_NAME);
        return numRows;
    }

    public int numberOfAllegiances(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ALLEGIANCES_TABLE_NAME);
        return numRows;
    }



    public boolean exists(String url) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { ACTORS_COLUMN_URL };
        String selection = ACTORS_COLUMN_URL + " =?";
        String[] selectionArgs = { url };
        String limit = "1";

        Cursor cursor = db.query(ACTORS_TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }


    public boolean bookExists(String url) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { BOOKS_COLUMN_URL };
        String selection = BOOKS_COLUMN_URL + " =?";
        String[] selectionArgs = { url };
        String limit = "1";

        Cursor cursor = db.query(BOOKS_TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public boolean allegianceExists(String url) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { ALLEGIANCES_COLUMN_URL };
        String selection = ALLEGIANCES_COLUMN_URL + " =?";
        String[] selectionArgs = { url };
        String limit = "1";

        Cursor cursor = db.query(ALLEGIANCES_TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }



    public boolean linkExists(String name) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { LINKS_COLUMN_NAME };
        String selection = LINKS_COLUMN_NAME + " =?";
        String[] selectionArgs = { name };
        String limit = "1";

        Cursor cursor = db.query(LINKS_TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public boolean booksListExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM list";
        Cursor crs = db.rawQuery(count, null);
        crs.moveToFirst();
        int icount = crs.getInt(0);
        return icount > 0;
    }

}

