package com.benjaminearley.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class PlaySongFragment extends DialogFragment {

    private static final String TAG = PlaySongFragment.class.getName();
    private static final String ARTIST_NAME = "ArtistName";

    private String mArtistName;

    public PlaySongFragment() {
        // Required empty public constructor
    }

    public static PlaySongFragment newInstance(String artistName) {
        PlaySongFragment fragment = new PlaySongFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtistName = getArguments().getString(ARTIST_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!((MainActivity) getActivity()).isTwoPane()) {
            try {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Spotify Streamer");
                if (getArguments() != null)
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
        if (((MainActivity) getActivity()).isTwoPane())
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_song, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!((MainActivity) getActivity()).isTwoPane()) {
            try {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top 10 Tracks");
                if (getArguments() != null)
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(mArtistName);
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

}
