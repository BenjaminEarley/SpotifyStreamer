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

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistArrayAdapter extends ArrayAdapter<Artist> {

    public ArtistArrayAdapter(Activity context, List<Artist> artists) {

        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Artist artist = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_card, parent, false);
        }

        ImageView artistImageView = (ImageView) convertView.findViewById(R.id.artist_image);

        if (artist.images != null && !artist.images.isEmpty()) {
            Picasso.with(parent.getContext()).load(artist.images.get(artist.images.size() / 2).url).into(artistImageView);
        } else {
            artistImageView.setImageResource(R.drawable.ic_launcher);
        }


        TextView artistNameView = (TextView) convertView.findViewById(R.id.artist_name);
        artistNameView.setText(artist.name);

        return convertView;
    }
}
