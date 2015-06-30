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

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TopSongListFragment extends Fragment {

    private static final String ARTIST_NAME = "ARTIST_NAME";
    private static final int MAX_TRACK_COUNT = 10;

    private String artistName;
    private SongArrayAdapter songAdapter;
    private ListView artistListView;
    private InputMethodManager keyboard;

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

        keyboard = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        spotify.searchTracks(artistName, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracks, Response response) {
                try {
                    if (tracks.tracks.items.size() <= MAX_TRACK_COUNT) {
                        update(tracks.tracks.items);
                    } else {
                        update(tracks.tracks.items.subList(0, 10));
                    }
                } catch (IndexOutOfBoundsException e) {

                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Track failure", error.toString());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_song_list, container, false);


        artistListView = (ListView) rootView.findViewById(R.id.listview_songs);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = (Track) parent.getItemAtPosition(position);

            }
        });

        try {
            keyboard.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {

        }

        return rootView;
    }

    private void update(final List<Track> tracks) {

        new Thread() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        songAdapter = new SongArrayAdapter(getActivity(), tracks);
                        artistListView.setAdapter(songAdapter);
                    }
                });

            }

        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Spotify Streamer");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
