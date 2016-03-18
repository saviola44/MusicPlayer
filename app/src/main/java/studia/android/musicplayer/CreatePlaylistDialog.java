package studia.android.musicplayer;


import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import studia.android.musicplayer.Database.MediaPlayerDBManager;
import studia.android.musicplayer.Database.PlaylistDAO;
import studia.android.musicplayer.Database.PlaylistSQLiteOpenHelper;
import studia.android.musicplayer.Database.SongsDAO;

/**
 * Created by mariusz on 20.02.16.
 */
public class CreatePlaylistDialog extends DialogFragment {

    private EditText playlistName;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());
        createProjectAlert.setTitle(getString(R.string.playlist_name));

        LayoutInflater inflater = getActivity().getLayoutInflater();

        createProjectAlert.setView(inflater.inflate(R.layout.create_playlist_dialog_layout, null))
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        playlistName = (EditText) getDialog().findViewById(R.id.playlist_name);
                        if(playlistName!=null){
                           if(playlistName.getText()!=null){
                               if(playlistName.getText().toString()!=null){
                                   MediaPlayerDBManager dbManager = new MediaPlayerDBManager(getContext());
                                   dbManager.addNewPlaylist(playlistName.getText().toString());
                               }
                           }
                        }else{
                            //Toast.makeText(getContext(), "edittext jest nulem", Toast.LENGTH_LONG).show();
                            Log.d("dialog", "nie znaleziono widoku edit text playlist_name");
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });

        return createProjectAlert.create();
    }
}
