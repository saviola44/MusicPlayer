package studia.android.musicplayer.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mariusz on 20.02.16.
 */
public class PlaylistSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mediaplayerdb";
    public static final int DATABASE_VERSION = 1;

    public PlaylistSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlaylistTable.onCreate(db);
        SongsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlaylistTable.onUpgrade(db, oldVersion, newVersion);
        SongsTable.onUpgrade(db, oldVersion, newVersion);
    }
}
