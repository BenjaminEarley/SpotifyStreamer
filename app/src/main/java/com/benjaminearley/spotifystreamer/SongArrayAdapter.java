package com.benjaminearley.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongArrayAdapter extends ArrayAdapter<Song> {
    private static final String LOG_TAG = ArtistArrayAdapter.class.getSimpleName();

    public SongArrayAdapter(Activity context, List<Song> songs) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Song song = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_card, parent, false);
        }

        ImageView albumImageView = (ImageView) convertView.findViewById(R.id.album_image);
        albumImageView.setImageResource(song.getCoverId());

        TextView albumNameView = (TextView) convertView.findViewById(R.id.album_name);
        albumNameView.setText(song.getAlbum());

        TextView trackNameView = (TextView) convertView.findViewById(R.id.track_name);
        trackNameView.setText(song.getTrack());

        return convertView;
    }
}