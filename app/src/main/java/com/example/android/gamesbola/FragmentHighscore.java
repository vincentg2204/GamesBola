package com.example.android.gamesbola;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class FragmentHighscore extends Fragment{
    private ListView listHighscore;
    public AdapterHighscore adapterHighscore;
    private MainActivity ui;
    private MainPresenter presenter;

    public FragmentHighscore(){

    }
    public void setData(ArrayList listOfScore){
        adapterHighscore.setListScore(listOfScore);
        adapterHighscore.sortListScore();
        adapterHighscore.notifyDataSetChanged();
    }

    public static FragmentHighscore newInstance(MainActivity mainActivity,MainPresenter presenter,String title){
        FragmentHighscore fragment = new FragmentHighscore();
        fragment.setMainActivity(mainActivity);
        fragment.setMainPresenter(presenter);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMainPresenter(MainPresenter presenter){this.presenter = presenter;}

    public void setMainActivity(MainActivity mainActivity) {
        this.ui = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_highscore, container,false);

        this.adapterHighscore = new AdapterHighscore(ui);
        this.listHighscore = v.findViewById(R.id.list_highscore);
//        for (int i =0 ;i<this.adapterHighscore.listScore.size();i++){
//            this.adapterHighscore.listScore.add((i+1) * 100);
//        }
        //DATA DUMMY
        //TODO GANTI JADI DATA BENERAN
//        for (int i =0 ;i<10;i++){
//            this.adapterHighscore.listScore.add((i+1) * 100);
//        }
//        adapterHighscore.sortListScore();
        presenter.updateListOfScore();
        this.listHighscore.setAdapter(adapterHighscore);
        return v;
    }
}
