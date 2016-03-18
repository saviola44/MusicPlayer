package studia.android.musicplayer.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import studia.android.musicplayer.Song;

/**
 * Created by mariusz on 20.02.16.
 */
public class SongsDAO {
    public static final String INSERT = "insert into " + SongsTable.TABLE_NAME
            + " (" + SongsTable.path + ", " + SongsTable.ratings +
            ", " + SongsTable.artist + ", " + SongsTable.title + ")"
            + " values (?, ?, ?, ?)";

    public static final String UPDATE = "insert into " + SongsTable.TABLE_NAME
            + " (" + SongsTable.Id + ", " + SongsTable.path + ", " + SongsTable.ratings +
            ", " + SongsTable.artist + ", " + SongsTable.title + ")"
            + " values (?, ?, ?, ?, ?)";
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;
    private SQLiteStatement updateStatement;

    public SongsDAO(SQLiteDatabase db){
        this.db = db;
        insertStatement = db.compileStatement(INSERT);
        updateStatement = db.compileStatement(UPDATE);
    }

    public long save(String path, float ratings, String artist, String title){

        insertStatement.clearBindings();
        insertStatement.bindString(1, path);
        insertStatement.bindString(2, "" + ratings);
        insertStatement.bindString(3, artist);
        insertStatement.bindString(4, title);
        return insertStatement.executeInsert();
    }

    public long update(long id, Song s){
        updateStatement.clearBindings();
        updateStatement.bindLong(1, id);
        updateStatement.bindString(2, s.getPath());
        updateStatement.bindString(3, "" + s.getRating());
        updateStatement.bindString(4, s.getArtist());
        updateStatement.bindString(5, s.getTitle());
        return updateStatement.executeInsert();
    }

    public void delete(String path){
        if(path!=null){
            //parametry to kolejno nazwa tabeli, where, i parametry
            db.delete(SongsTable.TABLE_NAME, SongsTable.path + " = ?", new String[]{path});
        }
    }

    public long findSong(String path){
        String rawQuery = "Select ID from " + SongsTable.TABLE_NAME + " where " + SongsTable.path + " = \"" + path + "\"";
        Cursor c = db.rawQuery(rawQuery, null);
        long pos=-1;
        if(c.getCount()<=0){
           c.close();

        }
        else{
            if(c.moveToFirst()){
                pos=c.getLong(0);
            }

        }
        c.close();
        return pos;
    }
    public void updateSongRatingInDatabase(Song s){
        long id = findSong(s.getPath());
        if(id>0){
            delete(s.getPath());
            update(id, s);
        }


    }
}
