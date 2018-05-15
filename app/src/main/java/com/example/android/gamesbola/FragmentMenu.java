package com.example.android.gamesbola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentMenu extends Fragment implements View.OnClickListener{

    private Button newBtn, exitBtn;
    private MainActivity ui;
    private MainPresenter presenter;

    private NavigationView nv;
    private ActionBar actionBar;
    private Toolbar toolbar;
    protected DrawerLayout dl;

    public FragmentMenu(){
    }

    public static FragmentMenu newInstance(MainActivity mainActivity,MainPresenter presenter, String title) {
        FragmentMenu fragment = new FragmentMenu();
        fragment.setMainActivity(mainActivity);
        fragment.setPresenter(presenter);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
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

        this.nv = v.findViewById(R.id.nav_view);
        this.dl = v.findViewById(R.id.drawer_layout);
        this.toolbar = v.findViewById(R.id.toolbar);
        ui.setSupportActionBar(toolbar);
        this.actionBar = ui.getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        this.actionBar.setDisplayShowTitleEnabled(false);

        setupDrawerContent(nv);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView nv) {
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.high_score:
                ui.changePage(MainActivity.PAGE_HIGHSCORE);
                presenter.updateWebService();
                break;
            case R.id.setting:
                ui.changePage(MainActivity.PAGE_SETTINGS);
                break;
            case R.id.exit:
                System.exit(0);
        }
        dl.closeDrawers();
        item.setChecked(true);
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
