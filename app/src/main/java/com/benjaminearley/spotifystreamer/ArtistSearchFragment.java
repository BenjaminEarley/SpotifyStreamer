package com.benjaminearley.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@SuppressWarnings("unchecked")
public class ArtistSearchFragment extends Fragment {

    private static final String TAG = ArtistSearchFragment.class.getName();
    private static final String ARTIST_KEY = "ARTISTS";

    private ArtistArrayAdapter artistAdapter;
    private SpotifyService spotify;
    private List<ArtistShort> artistShorts = new ArrayList<>();
    private DownloadArtistsTask mTask = new DownloadArtistsTask();
    private Toast toast;

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

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_artists);
        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistShort artistShort = (ArtistShort) parent.getItemAtPosition(position);

                openSongListFragment(artistShort.getName());
            }
        });

        if (savedInstanceState != null) {
            artistShorts = (List<ArtistShort>) savedInstanceState.getSerializable(ARTIST_KEY);
        }

        artistAdapter = new ArtistArrayAdapter(getActivity(), artistShorts);

        artistListView.setAdapter(artistAdapter);

        EditText searchArtistField = (EditText) rootView.findViewById(R.id.searchArtist);
        searchArtistField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                update(s);
            }
        });

        return rootView;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        if (artistShorts != null && !artistShorts.isEmpty()) {
            savedState.putSerializable(ARTIST_KEY, (Serializable) artistShorts);
        }

    }

    private void update(CharSequence artist) {

        mTask.cancel(true);
        mTask = new DownloadArtistsTask();
        mTask.execute(artist);

    }

    private void openSongListFragment(String name) {

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment, TopSongListFragment.newInstance(name)).addToBackStack(null).commit();
    }

    void refineSearchToast() {

        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(getActivity(), "Please Refine Search", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private class DownloadArtistsTask extends AsyncTask<CharSequence, Void, Void> {
        protected Void doInBackground(CharSequence... artists) {

            spotify.searchArtists(artists[0].toString(), new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artists, Response response) {
                    if (artistShorts != null) {
                        artistShorts.clear();
                    } else {
                        artistShorts = new ArrayList<>();
                    }
                    for (Artist artist : artists.artists.items) {

                        if (artist.images != null && !artist.images.isEmpty()) {
                            artistShorts.add(new ArtistShort(artist.name, artist.images.get(artist.images.size() / 2).url));
                        } else {
                            artistShorts.add(new ArtistShort(artist.name, null));
                        }

                        if (isCancelled())
                            break;
                    }

                    new Thread() {
                        public void run() {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (artistShorts == null || artistShorts.isEmpty()) {
                                            refineSearchToast();
                                        }

                                        artistAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }

                    }.start();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.toString());
                }
            });

            return null;
        }

    }


}
