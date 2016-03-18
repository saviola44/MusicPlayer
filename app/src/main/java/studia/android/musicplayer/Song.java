package studia.android.musicplayer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mariusz on 17.02.16.
 */
public class Song implements Parcelable {
    String artist;
    String title;
    String path;
    float rating;

    protected Song(Parcel in) {
        artist = in.readString();
        title = in.readString();
        path = in.readString();
        rating = in.readFloat();
    }

    public Song(String artist, String title, String path, float rating) {
        this.artist = artist;
        this.title = title;
        this.path = path;
        this.rating = rating;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeFloat(rating);
    }

    @Override
    public String toString() {
        String result ="";
        if(artist!=null && !artist.equals("<unknown>")){
            result += artist + " ";
        }
        if(title!=null){
            result += title;
        }
        if(result!=null && result.length()>40){
            result = result.substring(0,40);
        }
        return  result;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

