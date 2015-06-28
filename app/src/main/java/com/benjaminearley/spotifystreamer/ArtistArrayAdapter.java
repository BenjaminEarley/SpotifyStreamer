package com.benjaminearley.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class ArtistArrayAdapter extends ArrayAdapter<Artist> {
    private static final String LOG_TAG = ArtistArrayAdapter.class.getSimpleName();

    public ArtistArrayAdapter(Activity context, ArtistsPager artists) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, artists.artists.items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Artist artist = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_card, parent, false);
        }

        ImageView artistImageView = (ImageView) convertView.findViewById(R.id.artist_image);
        artistImageView.setImageResource(R.drawable.ic_launcher);

        TextView artistNameView = (TextView) convertView.findViewById(R.id.artist_name);
        artistNameView.setText(artist.name);

        return convertView;
    }
}
