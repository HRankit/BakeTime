package com.udacity.baketime.baketime;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.udacity.baketime.baketime.misc.MiscFunctions.CapitalizeString;
import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;

public class AllStepsFragment extends Fragment {
    public AllStepsFragment() {
    }

    public static AllStepsFragment newInstance(int position, String shortDescription, String description, String videoURL, String thumbnailURL) {

        AllStepsFragment myfrag = new AllStepsFragment();

        Bundle bundle = new Bundle();

        bundle.putInt("position", position);
        bundle.putString("shortDescription", shortDescription);
        bundle.putString("description", description);
        bundle.putString("videoURL", videoURL);
        bundle.putString("thumbnailURL", thumbnailURL);

        myfrag.setArguments(bundle);
        return myfrag;
    }

    private String getUTFdata(String str) {
        String stringg = "";
        try {
            stringg = URLEncoder.encode(str, "UTF-8");
            stringg = URLDecoder.decode(stringg, "UTF-8");
            stringg = CapitalizeString(stringg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringg;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.step_card_item, container, false);

        if (getArguments() != null) {
            String thumbnailURL = getArguments().getString("thumbnailURL");
            String videoURL = getArguments().getString("videoURL");
            String description = getArguments().getString("description");
            String shortDescription = getArguments().getString("shortDescription");
            Integer position = getArguments().getInt("position");


            TextView step_short_descrpition = rootview.findViewById(R.id.step_short_descrpition);
            step_short_descrpition.setText(getUTFdata(shortDescription));


            TextView step_description = rootview.findViewById(R.id.step_description);
            step_description.setText(getUTFdata(description));


            TextView step_video_url = rootview.findViewById(R.id.step_video_url);
            step_video_url.setText(videoURL);

            TextView step_thumbnail_url = rootview.findViewById(R.id.step_thumbnail_url);
            step_thumbnail_url.setText(thumbnailURL);

            if (isEmpty(videoURL) && isEmpty(thumbnailURL)) {
                TextView label_not_playing = rootview.findViewById(R.id.label_not_playing);
                label_not_playing.setVisibility(View.GONE);
            }

        }

        return rootview;
    }
}
