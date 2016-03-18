package studia.android.musicplayer.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import studia.android.musicplayer.Song;


/**
 * Created by mariusz on 20.02.16.
 */
public class PlaylistDAO {
    public static final String INSERT = "insert into " + PlaylistTable.TABLE_NAME
            + " (" + PlaylistTable.name + ", " + PlaylistTable.songID + ")"
            + " values (?, ?)";
    private SQLiteDatabase db;
    private SQLiteStatement insertStatement;

    public PlaylistDAO(SQLiteDatabase db){
        this.db = db;
        insertStatement = db.compileStatement(INSERT);
    }

    public long save(String name, String id){
        insertStatement.clearBindings();
        insertStatement.bindString(1, name);
        insertStatement.bindString(2, id);
        return insertStatement.executeInsert();
    }

    public void delete(String id){
        if(id!=null){
            //parametry to kolejno nazwa tabeli, where, i parametry
            db.delete(PlaylistTable.TABLE_NAME, PlaylistTable.Id + " = ?", new String[]{id});
        }
    }
    public List<String> getAllPlaylistFromDatabase(){
        List<String> playlists = new ArrayList<String>();
        //argumenty to kolejno nazwa tabeli
        //kolumny ktore chcemy wyswietlic
        //where
        //arg dla where
        //dalej
        Cursor c = db.query(PlaylistTable.TABLE_NAME, new String[]{PlaylistTable.name}, null, null, PlaylistTable.name, null, null, null);
        if(c.moveToFirst()){
            do{
                String name = c.getString(0);
                playlists.add(name);
            }while(c.moveToNext());
        }
        if(!c.isClosed()){
            c.close();
        }
        return playlists;
    }
    public ArrayList<Song> getAllSongsFromDatabase(String playlistName){
        ArrayList<Song> songs = new ArrayList<Song>();
        Cursor c = db.query(SongsTable.TABLE_NAME + " INNER JOIN " + PlaylistTable.TABLE_NAME + " ON " +
                SongsTable.TABLE_NAME + "." + SongsTable.Id + "=" + PlaylistTable.TABLE_NAME + "." + PlaylistTable.songID,
                new String[]{PlaylistTable.name, SongsTable.path, SongsTable.title, SongsTable.artist, SongsTable.ratings},
                PlaylistTable.name + " = "  + "'" + playlistName + "'",null,null,null,null);
        if(c.moveToFirst()){
            Log.d("getAllSongsFromDB count", "" + c.getCount());
            do{
                String path = c.getString(1);
                String title = c.getString(2);
                String artist = c.getString(3);
                float ratings = Float.parseFloat(c.getString(4));
                Song s = new Song(artist, title, path, ratings);
                songs.add(s);
            }while(c.moveToNext());
        }
        if(!c.isClosed()){
            c.close();
        }
        return songs;

    }
}
