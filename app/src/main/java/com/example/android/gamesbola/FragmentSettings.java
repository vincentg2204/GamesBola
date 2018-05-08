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
    private TextView tvCurColor;
    private ImageButton type1, type2, type3;
    private MainPresenter presenter;


    public static FragmentSettings newInstance(MainActivity mainActivity, MainPresenter presenter, String title) {
        FragmentSettings fragment = new FragmentSettings();
        fragment.setMainActivity(mainActivity);
        fragment.setMainPresenter(presenter);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    public void setMainPresenter(MainPresenter presenter){this.presenter = presenter;}
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

        type3 = v.findViewById(R.id.type3);
        type2 = v.findViewById(R.id.type2);
        type1 = v.findViewById(R.id.type1);
        type3.setOnClickListener(this);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == btnGroup[0]){
            setFocus(unfocus, btnGroup[0]);
            presenter.setDifficulty(1);
        }
        if (v == btnGroup[2]){
            setFocus(unfocus, btnGroup[2]);
            presenter.setDifficulty(3);
        }
        if (v == btnGroup[1]){
            setFocus(unfocus, btnGroup[1]);
            presenter.setDifficulty(2);
        }
        if (v == type3){
            tvCurColor.setText("Hijau");
            presenter.setCurColor(3);
        }
        if (v == type2){
            tvCurColor.setText("Biru");
            presenter.setCurColor(2);
        }
        if (v == type1){
            tvCurColor.setText("Coklat");
            presenter.setCurColor(1);
        }
    }

    private void setFocus(Button unfocus, Button focus){
        unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.unfocus = focus;
    }


}
