package studia.android.musicplayer.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import studia.android.musicplayer.Song;

/**
 * Created by mariusz on 20.02.16.
 */
public class MediaPlayerDBManager {
    private Context context;
    private SQLiteDatabase db;
    SongsDAO songDAO;
    PlaylistDAO playlistDAO;

    public MediaPlayerDBManager(Context context){
        this.context = context;
        SQLiteOpenHelper openHelper = new PlaylistSQLiteOpenHelper(context);
        db = openHelper.getWritableDatabase();
        songDAO = new SongsDAO(db);
        playlistDAO = new PlaylistDAO(db);
    }
    public long addNewPlaylist(String playlistName){
        return playlistDAO.save(playlistName, "0");
    }
    public long addSongToPlaylist(String playlistName, long songID){
        return playlistDAO.save(playlistName, "" + songID);
    }
    public List<String> getAllPlaylistFromDatabase(){
        return playlistDAO.getAllPlaylistFromDatabase();
    }

    public long saveSongInDb(String path, String artist, String title, String playlist){
        if(songDAO.findSong(path)>0){
            Log.d("DB manager", "znalazlem");
        }
        else{
            long index = songDAO.save(path, 0, artist, title);
            playlistDAO.save(playlist, "" + index);
        }
        return 0;
    }
    public ArrayList<Song> getAllSongsFromDatabase(String playlistName){
        return playlistDAO.getAllSongsFromDatabase(playlistName);
    }
    public void updateRatingSongInDatabase(Song s){
        songDAO.updateSongRatingInDatabase(s);
    }

}
