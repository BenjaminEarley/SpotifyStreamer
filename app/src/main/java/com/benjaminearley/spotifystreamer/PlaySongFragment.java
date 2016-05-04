package com.benjaminearley.spotifystreamer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private static final String SONG_POSITION = "SongPosition";
    private static final String SEEK_POSITION = "SeekPosition";
    private static final String IS_PLAYING = "IsPlaying";
    private static final int SEEK_TIME_DELAY = 100;

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
    private LinearLayout playSongView;
    private ProgressBar progressBar;
    private int mSongPosition;
    private int mSeekPosition;
    private boolean isPlaying = true;


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

        setRetainInstance(true);

        mSongPosition = mStartingPosition;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play_song, container, false);

        if (!((MainActivity) getActivity()).isTwoPane()) {
            try {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Music Sampler");
                if (getArguments() != null)
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            try {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            } catch (NullPointerException ignored) {

            }
        }

        albumCover = (ImageView) view.findViewById(R.id.album_cover);
        artistName = (TextView) view.findViewById(R.id.artist);
        albumName = (TextView) view.findViewById(R.id.album);
        trackName = (TextView) view.findViewById(R.id.track);
        trackSeekBar = (SeekBar) view.findViewById(R.id.trackSeekBar);
        skipLeft = (Button) view.findViewById(R.id.left);
        play = (Button) view.findViewById(R.id.play);
        skipRight = (Button) view.findViewById(R.id.right);
        playSongView = (LinearLayout) view.findViewById(R.id.playSongView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (savedInstanceState == null) {
            artistName.setText(mArtistName);
            new PlayMusicTask().execute();
        } else {
            mSongPosition = savedInstanceState.getInt(SONG_POSITION);
            mSeekPosition = savedInstanceState.getInt(SEEK_POSITION);
            isPlaying = savedInstanceState.getBoolean(IS_PLAYING);
            postExecute();
        }


        return view;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);

        savedState.putInt(SONG_POSITION, mSongPosition);
        savedState.putInt(SEEK_POSITION, mSeekPosition);
        savedState.putBoolean(IS_PLAYING, isPlaying);
    }

    private void mediaPlayback() {

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_48dp), null, null, null);
        } else {
            mediaPlayer.pause();
            isPlaying = false;
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_48dp), null, null, null);
        }

    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
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

    private void showPlayerView(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


        progressBar.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });


        playSongView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        playSongView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                playSongView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void postExecute() {

        showPlayerView(true);

        final Handler mHandler = new Handler();

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                boolean exited = false;
                if (mediaPlayer != null) {
                    try {
                        mSeekPosition = mediaPlayer.getCurrentPosition() / SEEK_TIME_DELAY;
                        trackSeekBar.setProgress(mSeekPosition);
                    } catch (IllegalStateException ignored) {
                        exited = true;
                    }
                }
                if (!exited) mHandler.postDelayed(this, SEEK_TIME_DELAY);
            }
        });

        if (isPlaying)
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pause_black_48dp), null, null, null);
        else
            play.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_play_arrow_black_48dp), null, null, null);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayback();
            }
        });

        skipRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerView(false);
                mediaPlayer.reset();
                mSongPosition++;
                new PlayMusicTask().execute();
            }
        });

        skipLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerView(false);
                mediaPlayer.reset();
                mSongPosition--;
                new PlayMusicTask().execute();
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
                    mediaPlayer.seekTo(progress * SEEK_TIME_DELAY);
                }
            }
        });

        Picasso.with(getActivity()).load(mTrackShorts.get(mSongPosition).getAlbumUrl()).into(albumCover);
        albumName.setText(mTrackShorts.get(mSongPosition).getAlbumName());
        trackName.setText(mTrackShorts.get(mSongPosition).getSongName());

        if (mSongPosition == 0) {
            skipLeft.setVisibility(View.INVISIBLE);
        } else {
            if (skipLeft.getVisibility() == View.INVISIBLE) {
                skipLeft.setVisibility(View.VISIBLE);
            }
        }

        if (mSongPosition == mTrackShorts.size() - 1) {
            skipRight.setVisibility(View.INVISIBLE);
        } else {
            if (skipRight.getVisibility() == View.INVISIBLE) {
                skipRight.setVisibility(View.VISIBLE);
            }
        }
    }

    private class PlayMusicTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... Voids) {

            try {
                mediaPlayer.setDataSource(mTrackShorts.get(mSongPosition).getSongUrl());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (isPlaying) mediaPlayer.start();
            mediaPlayer.seekTo(mSeekPosition * SEEK_TIME_DELAY);
            mediaPlayer.setLooping(true);

            return null;
        }

        protected void onPostExecute(Void Void) {

            try {
                postExecute();
            } catch (IllegalStateException ignored) {

            }
        }
    }


}
