package com.example.android.gamesbola;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FragmentGames fragmentGames;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private FragmentSettings fragmentSettings;
    private FragmentHighscore fragmentHighscore;
    private MainPresenter presenter;

    private NavigationView nv;
    private ActionBar actionBar;
    private Toolbar toolbar;
    protected DrawerLayout dl;

    public static int PAGE_GAMES = 1;
    public static int PAGE_MENU = 2;
    public static int PAGE_HIGHSCORE = 3;
    public static int PAGE_SETTINGS = 4;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);

        fragmentGames = FragmentGames.newInstance(this, presenter, "GAME FRAGMENT");
        fragmentMenu = FragmentMenu.newInstance(this, "MENU FRAGMENT");
        fragmentHighscore = FragmentHighscore.newInstance(this, "HIGHSCORE FRAGMENT");
        fragmentSettings = FragmentSettings.newInstance(this, "SETTINGS FRAGMENT");

        this.nv = findViewById(R.id.nav_view);
        this.dl = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowTitleEnabled(false);

        setupDrawerContent(nv);

        fragmentManager = getSupportFragmentManager();
        changePage(PAGE_MENU);
    }

    public void changePage(int page) {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        if (page == PAGE_GAMES) {
            if (fragmentGames.isAdded()) {
                ft.show(fragmentGames);
            } else {
                ft.add(R.id.fragment_container, fragmentGames).addToBackStack(null);
            }
        } else if (page == PAGE_MENU) {
            if (fragmentMenu.isAdded()) {
                ft.show(fragmentMenu);
            } else {
                ft.add(R.id.fragment_container, fragmentMenu);
            }
        } else if (page == PAGE_HIGHSCORE) {
            if (fragmentHighscore.isAdded()) {
                ft.show(fragmentHighscore);
            } else {
                ft.add(R.id.fragment_container, fragmentHighscore).addToBackStack(null);
            }
        }else if(page == PAGE_SETTINGS){
            if (fragmentSettings.isAdded()) {
                ft.show(fragmentSettings);
            } else {
                ft.add(R.id.fragment_container, fragmentSettings).addToBackStack(null);
            }
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
        Fragment fragment = null;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.high_score:
                changePage(PAGE_HIGHSCORE);
                break;
            case R.id.setting:
                changePage(PAGE_SETTINGS);
                break;
            case R.id.exit:
                System.exit(0);
        }
        dl.closeDrawers();
        item.setChecked(true);
    }

    public void setTime() {
        fragmentGames.setTextTVWaktu(presenter.getWaktu());
    }

    public void setGameOver(boolean gameOver) {
        fragmentGames.setGameOver(gameOver);
    }
}
