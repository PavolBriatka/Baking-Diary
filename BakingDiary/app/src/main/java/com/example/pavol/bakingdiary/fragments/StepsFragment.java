package com.example.pavol.bakingdiary.fragments;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pavol.bakingdiary.R;
import com.example.pavol.bakingdiary.customObjects.RecipeObject;
import com.example.pavol.bakingdiary.customObjects.StepObject;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String RECIPE_SAVE_KEY = "saved_recipe";
    private static final String LIST_INDEX_KEY = "saved_index";

    private RecipeObject stepsRecipe;
    private int listIndex = 0;
    private SimpleExoPlayer exoPlayer;


    @BindView(R.id.step_short_description)
    TextView shortDescription;
    @BindView(R.id.step_description)
    TextView description;
    @BindView(R.id.exoplayer_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.arrow_back)
    ImageView arrowBack;
    @BindView(R.id.arrow_forward)
    ImageView arrowForward;

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            stepsRecipe = savedInstanceState.getParcelable(RECIPE_SAVE_KEY);
            listIndex = savedInstanceState.getInt(LIST_INDEX_KEY);
        }

        final View stepView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, stepView);


        if (stepsRecipe == null) {
            shortDescription.setText(getResources().getString(R.string.tablet_empty_steps_string));
        } else {

            final ArrayList<StepObject> stepsArray = stepsRecipe.mListOfSteps;

            if (stepsArray != null) {


                if (stepsArray.get(listIndex).mShortDescription.equals(stepsArray.get(listIndex).mDescription)) {
                    shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                    description.setVisibility(View.GONE);
                } else {
                    description.setVisibility(View.VISIBLE);
                    shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                    description.setText(stepsArray.get(listIndex).mDescription);
                }

                initializePlayer(stepsArray.get(listIndex).mVideoUrl,
                        stepsArray.get(listIndex).mThumbnailUrl);


                arrowForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        exoPlayer.stop();

                        if (listIndex < stepsArray.size() - 1) {
                            listIndex++;
                        } else {
                            listIndex = 0;
                        }

                        if (stepsArray.get(listIndex).mShortDescription.equals(stepsArray.get(listIndex).mDescription)) {
                            shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                            description.setVisibility(View.GONE);
                        } else {
                            description.setVisibility(View.VISIBLE);
                            shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                            description.setText(stepsArray.get(listIndex).mDescription);
                        }

                        initializePlayer(stepsArray.get(listIndex).mVideoUrl,
                                stepsArray.get(listIndex).mThumbnailUrl);
                    }
                });

                arrowBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exoPlayer.stop();

                        if (listIndex > 0) {
                            listIndex--;
                        } else {
                            listIndex = stepsArray.size() - 1;
                        }

                        if (stepsArray.get(listIndex).mShortDescription.equals(stepsArray.get(listIndex).mDescription)) {
                            shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                            description.setVisibility(View.GONE);
                        } else {
                            description.setVisibility(View.VISIBLE);
                            shortDescription.setText(stepsArray.get(listIndex).mShortDescription);
                            description.setText(stepsArray.get(listIndex).mDescription);
                        }

                        initializePlayer(stepsArray.get(listIndex).mVideoUrl,
                                stepsArray.get(listIndex).mThumbnailUrl);
                    }
                });

            } else {
                Log.e("message", "Empty array list");
            }

        }

        return stepView;
    }

    public void setStepsRecipe(RecipeObject passedRecipe) {
        stepsRecipe = passedRecipe;
    }

    private void initializePlayer(String videoUrlParam, String thumbnailUrlParam) {

        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);
        }

        String finalVideoUrl = identifyVideoUrl(videoUrlParam, thumbnailUrlParam);

        if (finalVideoUrl.equals("")) {
            exoPlayerView.setUseController(false);
            exoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.no_video_placeholder));
            exoPlayer.prepare(null);
        } else {
            exoPlayerView.setUseController(true);
            exoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.loading_video_placeholder));
            String userAgent = Util.getUserAgent(getContext(), "BakingDiary");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(finalVideoUrl),
                    new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(),
                    null, null);

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }
    }

    private String identifyVideoUrl(String videoParam, String thumbnailParam) {

        String videoUrlString = "";
        if (!videoParam.equals("")) {
            videoUrlString = videoParam;
        }

        if (videoParam.equals("") && !thumbnailParam.equals("")) {
            videoUrlString = thumbnailParam;
        }

        return videoUrlString;
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVE_KEY, stepsRecipe);
        outState.putInt(LIST_INDEX_KEY, listIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
