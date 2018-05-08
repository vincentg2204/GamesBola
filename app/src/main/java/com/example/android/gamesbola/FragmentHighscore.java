package com.example.android.gamesbola;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

public class FragmentHighscore extends Fragment{
    private ListView listHighscore;
    AdapterHighscore adapterHighscore;
    MainActivity ui;

    public FragmentHighscore(){

    }

    public static FragmentHighscore newInstance(MainActivity mainActivity,String title){
        FragmentHighscore fragment = new FragmentHighscore();
        fragment.setMainActivity(mainActivity);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.ui = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container,false);

        this.listHighscore = v.findViewById(R.id.list_highscore);
        this.listHighscore.setAdapter(adapterHighscore);
        return v;
    }
}
