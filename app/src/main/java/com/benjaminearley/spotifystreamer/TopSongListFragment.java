package com.benjaminearley.spotifystreamer;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopSongListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopSongListFragment extends Fragment {

    private static final String ARTIST_NAME = "ARTIST_NAME";

    private String artistName;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_song_list, container, false);
    }


}
