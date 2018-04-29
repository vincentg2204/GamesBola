package com.example.android.gamesbola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentMenu extends Fragment implements View.OnClickListener{

    private Button newBtn, exitBtn;
    private MainActivity ui;

    public FragmentMenu(){
    }

    public static FragmentMenu newInstance(MainActivity mainActivity, String title) {
        FragmentMenu fragment = new FragmentMenu();
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
        View v = inflater.inflate(R.layout.fragment_menu, container,false);

        this.exitBtn = v.findViewById(R.id.exit_btn);
        this.newBtn = v.findViewById(R.id.new_btn);
        this.exitBtn.setOnClickListener(this);
        this.newBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == newBtn.getId()){
            ui.changePage(MainActivity.PAGE_GAMES);
        }
        if (v.getId() == exitBtn.getId()){
            System.exit(0);
        }
    }
}
