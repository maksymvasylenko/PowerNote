package com.powernote.project.powernote.fragment;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.powernote.project.powernote.Methods;
import com.powernote.project.powernote.R;

/**
 * Created by Maks on 13.10.2017.
 */

public class ColorListFragment extends Fragment {

    private Methods.OnPresortSelectedListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.choose_color_dialog, container, false);

        Button btnGreen = (Button) view.findViewById(R.id.colorGreenButton);
        Button btnRed = (Button) view.findViewById(R.id.colorRedrButton);
        Button btnPurple = (Button) view.findViewById(R.id.colorPurpleButton);

        Button btnBlue = (Button) view.findViewById(R.id.colorBlueButton);
        Button btnDarkBlue = (Button) view.findViewById(R.id.colorDarkBlueButton);
        Button btnOrange = (Button) view.findViewById(R.id.colorOrangeButton);

        Button btnYellow = (Button) view.findViewById(R.id.colorYellowButton);
        Button btnPink = (Button) view.findViewById(R.id.colorPinkButton);
        Button btnWhite = (Button) view.findViewById(R.id.colorWhiteButton);


        setColorButton(getResources().getColor(R.color.colorPurple), btnPurple);
        setColorButton(getResources().getColor(R.color.colorRed), btnRed);
        setColorButton(getResources().getColor(R.color.colorGreen), btnGreen);

        setColorButton(getResources().getColor(R.color.colorBlue), btnBlue);
        setColorButton(getResources().getColor(R.color.colorDarkBlue), btnDarkBlue);
        setColorButton(getResources().getColor(R.color.colorOrange), btnOrange);

        setColorButton(getResources().getColor(R.color.colorYellow), btnYellow);
        setColorButton(getResources().getColor(R.color.colorPink), btnPink);
        setColorButton(getResources().getColor(R.color.colorWhite), btnWhite);

        return view;

    }

    private void setColorButton(final int color, Button btn){
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{color,color});
        gd.setCornerRadius(100f);

        btn.setBackgroundDrawable(gd);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onColorSelected(color);
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Methods.OnPresortSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
