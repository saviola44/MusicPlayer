package studia.android.musicplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import studia.android.musicplayer.Database.MediaPlayerDBManager;

/**
 * Created by mariusz on 20.02.16.
 */
public class ChoosePlaylistActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    MediaPlayerDBManager dbManager;
    List<String> playlists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_playlist_layout);

        dbManager = new MediaPlayerDBManager(getApplicationContext());
        playlists = dbManager.getAllPlaylistFromDatabase();
        if(playlists.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.no_playlist_created, Toast.LENGTH_LONG).show();
            finish();
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, playlists);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = getIntent();
                setResult(RESULT_OK, i);
                i.putExtra("playlistName", playlists.get(position));
                ArrayList<Song> songs = dbManager.getAllSongsFromDatabase(playlists.get(position));
                i.putParcelableArrayListExtra("songs", songs);
                finish();
            }
        });
    }
}
