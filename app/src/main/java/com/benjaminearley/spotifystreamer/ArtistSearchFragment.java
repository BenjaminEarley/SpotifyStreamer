package com.benjaminearley.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ArtistSearchFragment extends Fragment {

    private ArtistArrayAdapter artistAdapter;
    private SpotifyService spotify;
    private ListView artistListView;

    public ArtistSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpotifyApi api = new SpotifyApi();
        spotify = api.getService();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        artistListView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) parent.getItemAtPosition(position);

                openSongListFragment(artist.name);
            }
        });

        EditText searchArtistField = (EditText) rootView.findViewById(R.id.searchArtist);
        searchArtistField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                spotify.searchArtists(s.toString(), new Callback<ArtistsPager>() {
                    @Override
                    public void success(ArtistsPager artists, Response response) {
                        try {
                            update(artists.artists.items);
                        } catch (IndexOutOfBoundsException e) {

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Album failure", error.toString());
                    }
                });
            }
        });

        return rootView;
    }

    private void update(final List<Artist> artists) {

        new Thread() {
            public void run() {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        artistAdapter = new ArtistArrayAdapter(getActivity(), artists);
                        artistListView.setAdapter(artistAdapter);
                    }
                });

            }

        }.start();

    }

    private void openSongListFragment(String name) {

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment, TopSongListFragment.newInstance(name)).addToBackStack(null).commit();
    }

}