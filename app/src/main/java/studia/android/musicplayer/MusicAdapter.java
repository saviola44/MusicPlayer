package studia.android.musicplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariusz on 16.02.16.
 */
public class MusicAdapter extends BaseAdapter {
    List list;
    Activity context;
    public MusicAdapter(Activity context, List l) {
        this.context=context;
        this.list=l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    public void setList(List l){
        list=l;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DataHandler handler;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.music_row_layout, parent, false);
            handler = new DataHandler();
            handler.image = (ImageView) row.findViewById(R.id.music_image);
            handler.songName = (TextView) row.findViewById(R.id.songNameId);
            row.setTag(handler);
        }
        else{
            handler = (DataHandler) row.getTag();
        }
        Song s = (Song) getItem(position);
        //handler.image.setImageResource(R.drawable.sms_list);
        handler.songName.setText(s.toString());
        return row;
    }

    static class DataHandler{
        ImageView image;
        TextView songName;
    }
}
