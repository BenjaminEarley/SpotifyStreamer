package com.benjaminearley.spotifystreamer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopSongListFragment extends Fragment {

    private static final String TAG = ArtistSearchFragment.class.getName();
    private static final String ARTIST_NAME = "ARTIST_NAME";
    private static final String TRACKS_KEY = "TRACKS";
    private static final int MAX_TRACK_COUNT = 10;

    private String artistName;
    private SongArrayAdapter songAdapter;
    private ListView artistListView;
    private InputMethodManager keyboard;
    private SpotifyService spotify;
    private List<TrackShort> trackShorts = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public TopSongListFragment() {
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

        keyboard = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_song_list, container, false);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top 10 Tracks");
            if (getArguments() != null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }


        artistListView = (ListView) rootView.findViewById(R.id.listview_songs);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrackShort track = (TrackShort) parent.getItemAtPosition(position);

            }
        });

        if (savedInstanceState == null) {
            spotify.searchTracks(artistName, new Callback<TracksPager>() {
                @Override
                public void success(TracksPager tracks, Response response) {

                    if (tracks.tracks.items.size() <= MAX_TRACK_COUNT) {
                        update(tracks.tracks.items);
                    } else {
                        update(tracks.tracks.items.subList(0, 10));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Track failure", error.toString());
                }
            });
        } else {
            trackShorts = (List<TrackShort>) savedInstanceState.getSerializable(TRACKS_KEY);
            songAdapter = new SongArrayAdapter(getActivity(), trackShorts);
            artistListView.setAdapter(songAdapter);
        }


        try {
            keyboard.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }

        return rootView;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        if (trackShorts != null && !trackShorts.isEmpty()) {
            savedState.putSerializable(TRACKS_KEY, (Serializable) trackShorts);

        }

    }

    private void update(final List<Track> tracks) {

        for (Track track : tracks) {

            if (track.album.images != null && !track.album.images.isEmpty()) {
                trackShorts.add(new TrackShort(track.album.name, track.name, track.album.images.get((int) Math.floor(track.album.images.size() / 2)).url));
            } else {
                trackShorts.add(new TrackShort(track.album.name, track.name, null));
            }

        }

        new Thread() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        songAdapter = new SongArrayAdapter(getActivity(), trackShorts);
                        artistListView.setAdapter(songAdapter);
                    }
                });

            }

        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Spotify Streamer");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }

    }


}
