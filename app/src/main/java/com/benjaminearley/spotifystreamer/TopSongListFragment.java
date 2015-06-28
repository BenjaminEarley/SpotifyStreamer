package com.benjaminearley.spotifystreamer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;

public class TopSongListFragment extends Fragment {

    private static final String ARTIST_NAME = "ARTIST_NAME";
    Song[] songs = {
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher),
            new Song("Lame Album", "Lame Song", R.drawable.ic_launcher)
    };
    private String artistName;
    private SongArrayAdapter songAdapter;

    public TopSongListFragment() {
        // Required empty public constructor
    }

    public static TopSongListFragment newInstance(String param1) {
        TopSongListFragment fragment = new TopSongListFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistName = getArguments().getString(ARTIST_NAME);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top 10 Tracks");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_song_list, container, false);

        songAdapter = new SongArrayAdapter(getActivity(), Arrays.asList(songs));

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_songs);
        artistListView.setAdapter(songAdapter);


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Spotify Streamer");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


}
