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

public class SongArrayAdapter extends ArrayAdapter<TrackShort> {

    public SongArrayAdapter(Activity context, List<TrackShort> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TrackShort track = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_card, parent, false);
        }

        ImageView albumImageView = (ImageView) convertView.findViewById(R.id.album_image);
        if (track.getAlbumUrl() != null) {
            Picasso.with(parent.getContext()).load(track.getAlbumUrl()).into(albumImageView);
        } else {
            albumImageView.setImageResource(R.drawable.ic_launcher);
        }


        TextView albumNameView = (TextView) convertView.findViewById(R.id.album_name);
        albumNameView.setText(track.getAlbumName());

        TextView trackNameView = (TextView) convertView.findViewById(R.id.track_name);
        trackNameView.setText(track.getSongName());

        return convertView;
    }
}