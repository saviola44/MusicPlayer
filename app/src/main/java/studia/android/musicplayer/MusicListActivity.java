package studia.android.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import studia.android.musicplayer.Database.MediaPlayerDBManager;


import static android.provider.MediaStore.MediaColumns.TITLE;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
public class MusicListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {


    private CreatePlaylistDialog createPlaylistDialog = new CreatePlaylistDialog();
    ListView lV;
    MusicAdapter adapter;
    ArrayList<Song> songs;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        //ponizszy kod zostal wygenerowany automatycznie po utworzeniu projektu nvigation drawer
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_songs);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //--------------------------------------------------------------------------------------------



        lV = (ListView) findViewById(android.R.id.list);
        songs = new ArrayList<Song>();
        if(savedInstanceState!=null){
            songs = savedInstanceState.getParcelableArrayList("songs");
            String s = savedInstanceState.getString("playlistName");
            toolbar.setTitle(s);
        }

        adapter = new MusicAdapter(this, songs);
        lV.setAdapter(adapter);
        if(savedInstanceState==null){
        getSupportLoaderManager().initLoader(0, null, this);}

        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //tworzymy intencje i wysylamy do niej liste wszystlich piosenek naszej wybranej listy odtwarzania
                //oraz pozycje piosenki na liscie ktora chcemy odtwarzac
                Intent playSongIntent = new Intent(getApplicationContext(), PlaySongActivity.class);
                playSongIntent.putExtra("songPosition", position);
                playSongIntent.putParcelableArrayListExtra("songs", songs);
                startActivity(playSongIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.display_playlists_menu_option) {
            Intent i = new Intent(getApplicationContext(), ChoosePlaylistActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.add_playlist_menu_option) {
            createPlaylistDialog.show(getSupportFragmentManager(), "createPlaylistDialog");
        } else if (id == R.id.add_song_menu_option) {
            if(toolbar.getTitle().equals(getString(R.string.all_songs))){
                Toast.makeText(getApplicationContext(), "Musisz wybrac listę odtwarzania", Toast.LENGTH_LONG).show();

            }else{
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.add_song)),2);
            }

        } else if (id == R.id.all_songs_menu_option) {
            getSupportLoaderManager().initLoader((int) Calendar.getInstance().getTimeInMillis(), null, this);
            toolbar.setTitle(R.string.all_songs);
            invalidateOptionsMenu();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //teraz trzeba pobrac muzyke z pamieci
        //definiujemy kolumny które chcemy pobrac, nazwy kolumn pochadza z
        // pakietu provider.MediaStore.Audio.AudioColumns -> patrz importy
        String[] columns = {TITLE, ARTIST,_ID, DATA};
        String whereClause = IS_MUSIC + " =?";
        String[] whereValues = {"1"};
        return new CursorLoader(this,EXTERNAL_CONTENT_URI, columns,whereClause, whereValues, null );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        while(data.moveToNext()){

            String songTitle = data.getString(data.getColumnIndex(TITLE));
            String songArtist = data.getString(data.getColumnIndex(ARTIST));
            String songPath = data.getString(data.getColumnIndex(DATA));
            Song s = new Song(songArtist, songTitle, songPath, 0);
            songs.add(s);
        }
        adapter.setList(songs);
        lV.invalidateViews();
        lV.refreshDrawableState();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songs", songs);
        outState.putString("playlistName", "" + toolbar.getTitle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                songs = data.getParcelableArrayListExtra("songs");
                String playlistName = data.getStringExtra("playlistName");
                if(playlistName!=null)  toolbar.setTitle(playlistName);
                if(songs==null)songs = new ArrayList<>();
            }
        }
        if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                if(data!=null && data.getData()!=null){
                    Uri songUri = data.getData();
                    getSongFromDatabase(songUri);
                }
            }
        }
        adapter.setList(songs);
        lV.invalidateViews();
        lV.refreshDrawableState();
        adapter.notifyDataSetChanged();
    }
    public void getSongFromDatabase(Uri songUri){
        Cursor cursor = null;
        try {
            String[] proj = { DATA, TITLE, ARTIST };
            cursor = getContentResolver().query(songUri,  proj, null, null, null);
            cursor.moveToFirst();
            int songPathIndex = cursor.getColumnIndexOrThrow(DATA);
            int songNameIndex = cursor.getColumnIndexOrThrow(TITLE);
            int songArtistIndex = cursor.getColumnIndexOrThrow(ARTIST);
            String songPath = cursor.getString(songPathIndex);
            String songName = cursor.getString(songNameIndex);
            String songArtist = cursor.getString(songArtistIndex);
            Song s = new Song(songArtist, songName, songPath, 0);
            if(!toolbar.getTitle().equals(getString(R.string.all_songs))){
                //jezeli wyswietlamy wszystkie piosenki to nie dodajemu jej do listy
                songs.add(s);
                saveSongInDb(s);
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void saveSongInDb(Song s){
        MediaPlayerDBManager dbManager = new MediaPlayerDBManager(getApplicationContext());
        dbManager.saveSongInDb(s.getPath(), s.getArtist(), s.getTitle(), "" + toolbar.getTitle());
    }
}
