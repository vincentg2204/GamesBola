package com.example.android.gamesbola;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toolbar;

public class FragmentMenu extends Fragment {

    private Button newBtn, exitBtn;
    private Context ctx;


    public FragmentMenu(){}

    public static FragmentMenu newInstance(MainActivity mainActivity, String title) {
        FragmentMenu fragment = new FragmentMenu();
        fragment.setMainActivity(mainActivity);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    public void setMainActivity(MainActivity ui) {
        this.ctx = ui;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container,false);

        this.exitBtn = v.findViewById(R.id.exit_btn);
        this.newBtn = v.findViewById(R.id.new_btn);

        return v;
    }
}
