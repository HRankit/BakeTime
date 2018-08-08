package com.udacity.baketime.baketime;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

import static com.udacity.baketime.baketime.misc.MiscFunctions.IS_TWO_PANE;
import static com.udacity.baketime.baketime.misc.MiscFunctions.URL_NOT_FOUND;
import static com.udacity.baketime.baketime.misc.MiscFunctions.VIDEO_URL;
import static com.udacity.baketime.baketime.misc.MiscFunctions.convertDpIntoPx;


public class BlankFragment extends Fragment {


    private static boolean canVideoBePlayed = true;
    private final int VIDEO_PLAYER_HEIGHT_IN_DP = 420;
    private ComponentListener componentListener;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String videoUrl;
    private FrameLayout fl;
    private Boolean isTwoPane;
    private boolean mExoPlayerFullscreen = false;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private boolean videoAvailable;
    private String PLAYER_POSITION = "player_position";
    private String PLAYER_PLAY_WHEN_READY = "should_video_play";

    public BlankFragment() {
        // Required empty public constructor
    }

    public static boolean canVideoBePlayed() {
        return canVideoBePlayed;
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };

        View decorView = mFullScreenDialog.getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            ;
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        fl.addView(playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_expand));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoUrl = getArguments().getString(VIDEO_URL);
            isTwoPane = getArguments().getBoolean(IS_TWO_PANE);
            Timber.e(isTwoPane.toString());
        } else {
            videoUrl = URL_NOT_FOUND;
        }

        componentListener = new ComponentListener();
        if (savedInstanceState != null) {
            String STATE_PLAYER_FULLSCREEN = "isPlayerFullscreen";
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            playbackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_video_playback, container, false);

        playerView = returnView.findViewById(R.id.video_view);
        fl = returnView.findViewById(R.id.frag_video_playback);


        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpIntoPx(getActivity(), VIDEO_PLAYER_HEIGHT_IN_DP));
        fl.setLayoutParams(lp);
        initFullscreenDialog();
        mFullScreenIcon = returnView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout mFullScreenButton = returnView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else {
                    closeFullscreenDialog();
                    initFullscreenDialog();
                }

            }
        });

        return returnView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane && videoAvailable) {
            player.setPlayWhenReady(playWhenReady);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            fl.setLayoutParams(lp);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !isTwoPane && videoAvailable) {
            player.setPlayWhenReady(playWhenReady);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpIntoPx(getActivity(), VIDEO_PLAYER_HEIGHT_IN_DP));
            fl.setLayoutParams(lp);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String STATE_PLAYER_FULLSCREEN = "isPlayerFullscreen";
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            playbackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAYER_PLAY_WHEN_READY);
        }
        super.onViewStateRestored(savedInstanceState);

    }

    private void initializePlayer(String vidUrl) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        player.addListener(componentListener);

        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        if (vidUrl.equals(URL_NOT_FOUND)) {
            fl.setVisibility(View.GONE);
            videoAvailable = false;
        } else {
            hideImageAndShowPlayer(View.VISIBLE);

            Uri uri = Uri.parse(vidUrl);

            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, false, false);
            videoAvailable = true;
        }


    }

    private void hideImageAndShowPlayer(int visible) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpIntoPx(getActivity(), VIDEO_PLAYER_HEIGHT_IN_DP));
        fl.setLayoutParams(lp);

        playerView.setVisibility(visible);
        fl.setVisibility(visible);

    }

    private void hidePlayerAndShowImage() {
        fl.setVisibility(View.GONE);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(videoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(videoUrl);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        releasePlayer();
        outState.putLong(PLAYER_POSITION, playbackPosition);
        outState.putBoolean(PLAYER_PLAY_WHEN_READY, playWhenReady);
        super.onSaveInstanceState(outState);

    }

    private class ComponentListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerError(ExoPlaybackException error) {

            super.onPlayerError(error);
            canVideoBePlayed = false;
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.videoErrorMessage), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {

            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    hidePlayerAndShowImage();
                    canVideoBePlayed = false;
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    canVideoBePlayed = true;

                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    canVideoBePlayed = true;

                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    canVideoBePlayed = true;

                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    canVideoBePlayed = true;

                    break;
            }
            Timber.e("changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }

    }
}
