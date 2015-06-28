package com.benjaminearley.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;


public class ArtistSearchFragment extends Fragment {

    Artist[] artists = {
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher),
            new Artist("Maroon 5", R.drawable.ic_launcher)
    };
    private ArtistArrayAdapter artistAdapter;

    public ArtistSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        artistAdapter = new ArtistArrayAdapter(getActivity(), Arrays.asList(artists));

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistListView.setAdapter(artistAdapter);

        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) parent.getItemAtPosition(position);

                openSongListFragment(artist.getName());
            }
        });


        return rootView;
    }

    private void openSongListFragment(String name) {

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment, TopSongListFragment.newInstance(name)).addToBackStack(null).commit();
    }

}
