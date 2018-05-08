package com.example.android.gamesbola;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentSettings extends Fragment implements View.OnClickListener{

    private MainActivity ui;
    private Button[] btnGroup = new Button[3];
    private Button unfocus;
    private int[] btnId = {R.id.easy_btn, R.id.med_btn, R.id.hard_btn};
    private int difficulty;
    private int curColor;
    private TextView tvCurColor;
    private ImageButton orange, blue, pink;


    public static FragmentSettings newInstance(MainActivity mainActivity, String title) {
        FragmentSettings fragment = new FragmentSettings();
        fragment.setMainActivity(mainActivity);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMainActivity(MainActivity ui) {
        this.ui = ui;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings,container,false);

        for (int i = 0; i < btnGroup.length; i++){
            btnGroup[i] = v.findViewById(btnId[i]);
            btnGroup[i].setOnClickListener(this);
        }
        unfocus = btnGroup[0];
        this.tvCurColor = v.findViewById(R.id.cur_color);
        this.tvCurColor.setText("Default");

        pink = v.findViewById(R.id.pink);
        blue = v.findViewById(R.id.blue);
        orange = v.findViewById(R.id.orange);
        pink.setOnClickListener(this);
        orange.setOnClickListener(this);
        blue.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.easy_btn){
            setFocus(unfocus, btnGroup[0]);
            this.difficulty = 1;
        }
        if (v.getId() == R.id.hard_btn){
            setFocus(unfocus, btnGroup[2]);
            this.difficulty = 3;
        }
        if (v.getId() == R.id.med_btn){
            setFocus(unfocus, btnGroup[1]);
            this.difficulty = 2;
        }
        if (v.getId() == R.id.pink){
            tvCurColor.setText("Pink");
            curColor = R.color.colorAccent;
        }
        if (v.getId() == R.id.blue){
            tvCurColor.setText("Blue");
            curColor = R.color.colorPrimaryDark;
        }
        if (v.getId() == R.id.orange){
            tvCurColor.setText("Orange");
            curColor = R.color.orange;
        }
    }

    private void setFocus(Button unfocus, Button focus){
        unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.unfocus = focus;
    }
}
