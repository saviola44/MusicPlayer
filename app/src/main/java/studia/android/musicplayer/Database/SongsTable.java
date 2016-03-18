package studia.android.musicplayer.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mariusz on 20.02.16.
 */
public class SongsTable {
    public static final String TABLE_NAME = "songs";

    public static final String Id = "ID";
    public static final String path = "path";
    public static final String ratings = "ratings";
    public static final String artist = "artist";
    public static final String title = "title";

    public static void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(Id + " INTEGER PRIMARY KEY, ");
        sb.append(path + " TEXT UNIQUE NOT NULL, ");
        sb.append(ratings + " TEXT,");
        sb.append(artist + " TEXT, ");
        sb.append(title + " TEXT);");
        db.execSQL(sb.toString());
    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        SongsTable.onCreate(db);
    }
}
