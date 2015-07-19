package com.benjaminearley.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.benjaminearley.spotifystreamer.model.TrackShort;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class PlaySongFragment extends DialogFragment {

    private static final String TAG = PlaySongFragment.class.getName();
    private static final String ARTIST_NAME = "ArtistName";
    private static final String LIST_POSITION = "ListPosition";
    private static final String LIST_TRACKS = "ListTracks";

    private String mArtistName;
    private int mStartingPosition;
    private List<TrackShort> mTrackShorts;
    private MediaPlayer mediaPlayer;
    private ImageView albumCover;
    private TextView artistName;
    private TextView albumName;
    private TextView trackName;
    private SeekBar trackSeekBar;
    private Button skipLeft;
    private Button play;
    private Button skipRight;
    private int mPosition;


    public PlaySongFragment() {
        // Required empty public constructor
    }

    public static PlaySongFragment newInstance(String artistName, int position, List<TrackShort> trackShorts) {
        PlaySongFragment fragment = new PlaySongFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_NAME, artistName);
        args.putInt(LIST_POSITION, position);
        args.putSerializable(LIST_TRACKS, (Serializable) trackShorts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtistName = getArguments().getString(ARTIST_NAME);
            mStartingPosition = getArguments().getInt(LIST_POSITION);
            mTrackShorts = (List<TrackShort>) getArguments().getSerializable(LIST_TRACKS);
        }

        mPosition = mStartingPosition;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_song, container, false);

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
            try {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            } catch (NullPointerException ignored) {

            }

        albumCover = (ImageView) view.findViewById(R.id.album_cover);
        artistName = (TextView) view.findViewById(R.id.artist);
        albumName = (TextView) view.findViewById(R.id.album);
        trackName = (TextView) view.findViewById(R.id.track);
        trackSeekBar = (SeekBar) view.findViewById(R.id.trackSeekBar);
        skipLeft = (Button) view.findViewById(R.id.left);
        play = (Button) view.findViewById(R.id.play);
        skipRight = (Button) view.findViewById(R.id.right);

        if (savedInstanceState == null) {
            artistName.setText(mArtistName);
            loadResources(mPosition);
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayback();
            }
        });

        skipRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                mPosition++;
                loadResources(mPosition);
            }
        });

        skipLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                mPosition--;
                loadResources(mPosition);
            }
        });

        trackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });


        return view;
    }

    private void mediaPlayback() {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_48dp), null, null, null);
        } else {
            mediaPlayer.pause();
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_48dp), null, null, null);
        }

    }

    private void loadResources(int position) {

        Picasso.with(getActivity()).load(mTrackShorts.get(position).getAlbumUrl()).into(albumCover);
        albumName.setText(mTrackShorts.get(position).getAlbumName());
        trackName.setText(mTrackShorts.get(position).getSongName());

        if (position == 0) {
            skipLeft.setVisibility(View.INVISIBLE);
        } else {
            if (skipLeft.getVisibility() == View.INVISIBLE) {
                skipLeft.setVisibility(View.VISIBLE);
            }
        }

        if (position == 9) {
            skipRight.setVisibility(View.INVISIBLE);
        } else {
            if (skipRight.getVisibility() == View.INVISIBLE) {
                skipRight.setVisibility(View.VISIBLE);
            }
        }

        try {
            mediaPlayer.setDataSource(mTrackShorts.get(position).getSongUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_48dp), null, null, null);

        final Handler mHandler = new Handler();

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    trackSeekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
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
