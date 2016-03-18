package studia.android.musicplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import studia.android.musicplayer.Database.MediaPlayerDBManager;

/**
 * Created by mariusz on 17.02.16.
 */
public class PlaySongActivity extends Activity implements MediaPlayer.OnCompletionListener {
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private ImageButton nextSongButton;
    private ImageButton prevSongButton;
    private SeekBar seekbar;
    private TextView songNameTV;
    private TextView artistTV;
    private TextView duration;
    private TextView songLenght;
    private RatingBar songRB;

    private MediaPlayer player;
    private int timeElapsed = 0, finalTime = 0;
    boolean play = true;

    ArrayList<Song> songs;
    Song currentSong;
    private Handler durationHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song_layout);
        playButton = (ImageButton) findViewById(R.id.playId);
        pauseButton = (ImageButton) findViewById(R.id.pauseId);
        prevButton = (ImageButton) findViewById(R.id.prevId);
        nextButton = (ImageButton) findViewById(R.id.nextId);
        nextSongButton = (ImageButton) findViewById(R.id.nextSongId);
        prevSongButton = (ImageButton) findViewById(R.id.prevSongId);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        songNameTV = (TextView) findViewById(R.id.songNameId);
        artistTV = (TextView) findViewById(R.id.artistId);
        songLenght = (TextView) findViewById(R.id.endId);
        duration = (TextView) findViewById(R.id.startId);
        songRB = (RatingBar) findViewById(R.id.ratingBar);
        int pos;
        if(savedInstanceState!=null){
            pos =  savedInstanceState.getInt("songPosition",0);
        }
        else{
            pos =  getIntent().getIntExtra("songPosition",0);
        }
        songs = getIntent().getParcelableArrayListExtra("songs");

        currentSong = songs.get(pos);
        player =  new MediaPlayer();
        //MediaPlayer.create(this, Uri.parse(currentSong.getPath()));
        try {
            player.setDataSource(getApplicationContext(),Uri.parse(currentSong.getPath()));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(savedInstanceState!=null){
            timeElapsed = savedInstanceState.getInt("timeElapsed",0);
            player.seekTo(timeElapsed);
            play = savedInstanceState.getBoolean("play", true);
        }



        if(play){
            player.start();
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(play)mp.start();
            }
        });
        player.setOnCompletionListener(this);

        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
        initializeNavigationView();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.start();
                play=true;
                timeElapsed = player.getCurrentPosition();
                seekbar.setProgress((int) timeElapsed);
                durationHandler.postDelayed(updateSeekBarTime, 100);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                play=false;
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeElapsed = timeElapsed + 10000;
                player.seekTo((int) timeElapsed);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeElapsed = timeElapsed - 10000;
                player.seekTo((int) timeElapsed);
            }
        });
        nextSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = songs.indexOf(currentSong);
                currentSong = songs.get((position+1)%(songs.size()));
                play = player.isPlaying();
                if(play)player.stop();
                player.reset();
                try {
                    player.setDataSource(getApplicationContext(), Uri.parse(currentSong.getPath()));
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //player = MediaPlayer.create(getApplicationContext(), Uri.parse(currentSong.getPath()));

                if(play){
                    player.start();
                }
                initializeNavigationView();
            }
        });
        prevSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = songs.indexOf(currentSong);
                position--;
                if (position < 0) position = songs.size() - 1;
                currentSong = songs.get(position);
                play = player.isPlaying();
                if(play)player.stop();
                player.reset();
                try {
                    player.setDataSource(getApplicationContext(), Uri.parse(currentSong.getPath()));
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(play)player.start();
                initializeNavigationView();
            }
        });
        if(songRB!=null){
            songRB.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    currentSong.setRating(rating);
                    MediaPlayerDBManager dbManager = new MediaPlayerDBManager(getApplicationContext());
                    dbManager.updateRatingSongInDatabase(currentSong);
                }
            });
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    //player.stop();
                    timeElapsed=progress;
                    player.seekTo(timeElapsed);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }



    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = player.getCurrentPosition();
            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%2d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("timeElapsed", timeElapsed);
        outState.putBoolean("play", play);
        outState.putInt("songPosition", songs.indexOf(currentSong));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    public void initializeNavigationView(){
        finalTime = player.getDuration();
        songLenght.setText(String.format("%2d:%02d", TimeUnit.MILLISECONDS.toMinutes(finalTime), TimeUnit.MILLISECONDS.toSeconds(finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((finalTime)))));
        seekbar.setMax((int) finalTime);
        String title = currentSong.getTitle();
        if(title!=null && title.length()>=40){
            title = title.substring(0,40);
        }
        songNameTV.setText(title);
        if(artistTV!=null) {
            if(currentSong.getArtist().equals("<unknown>")){
                artistTV.setText(R.string.unknown_artist);
            }
            else{
                artistTV.setText(currentSong.getArtist());
            }
        }
        if(songRB!=null){
            songRB.setRating(currentSong.getRating());
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        player.reset();
        int position = songs.indexOf(currentSong);
        if(position<songs.size()-1) {
            currentSong = songs.get(position +1 );
        }
        else{
            currentSong = songs.get(0);
        }
        //player = MediaPlayer.create(getApplicationContext(), Uri.parse(currentSong.getPath()));
        try {
            player.setDataSource(getApplicationContext(), Uri.parse(currentSong.getPath()));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }



        initializeNavigationView();
    }
}
