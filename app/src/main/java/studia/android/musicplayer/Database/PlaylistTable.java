package studia.android.musicplayer.Database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mariusz on 20.02.16.
 */
public class PlaylistTable {
    public static final String TABLE_NAME = "playlists";

    public static final String Id = "id";
    public static final String name = "playlist";
    public static final String songID = "songId";


    public static void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(Id + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(name + " TEXT NOT NULL, ");
        sb.append(songID + " INTEGER NOT NULL, ");
        sb.append(" FOREIGN KEY("  + songID + ") REFERENCES " + SongsTable.TABLE_NAME + " (");
        sb.append(SongsTable.Id + ")); ");
        db.execSQL(sb.toString());
    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        SongsTable.onCreate(db);
    }


}
