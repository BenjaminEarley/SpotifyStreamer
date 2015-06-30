package com.benjaminearley.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SongArrayAdapter extends ArrayAdapter<Track> {

    public SongArrayAdapter(Activity context, List<Track> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Track track = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_card, parent, false);
        }

        ImageView albumImageView = (ImageView) convertView.findViewById(R.id.album_image);
        if (track.album.images != null && !track.album.images.isEmpty()) {
            Picasso.with(parent.getContext()).load(track.album.images.get((int) Math.floor(track.album.images.size() / 2)).url).into(albumImageView);
        } else {
            albumImageView.setImageResource(R.drawable.ic_launcher);
        }


        TextView albumNameView = (TextView) convertView.findViewById(R.id.album_name);
        albumNameView.setText(track.album.name);

        TextView trackNameView = (TextView) convertView.findViewById(R.id.track_name);
        trackNameView.setText(track.name);

        return convertView;
    }
}