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

public class MainActivity extends AppCompatActivity{
    private FragmentGames fragmentGames;
    private FragmentManager fragmentManager;
    private FragmentMenu fragmentMenu;
    private NavigationView nv;
    private ActionBar actionBar;
    private Toolbar toolbar;
    protected DrawerLayout dl;

    public static int PAGE_GAMES = 1;
    public static int PAGE_MENU = 2;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nv = findViewById(R.id.nav_view);
        this.dl = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayShowTitleEnabled(false);

        setupDrawerContent(nv);
        fragmentGames = FragmentGames.newInstance(this,"GAME FRAGMENT");
        fragmentMenu = new FragmentMenu();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,fragmentMenu).commit();

    }

    public void changePage(int page){
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        if(page == PAGE_GAMES){
            if (fragmentGames.isAdded()) {
                ft.show(fragmentGames);
            } else {
                ft.add(R.id.fragment_container, fragmentGames).addToBackStack(null);
            }
        }
        if (page == PAGE_MENU){
            if (fragmentMenu.isAdded()){
                ft.show(fragmentMenu);
            }
            }
            ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView nv){
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem item){
        Fragment fragment = null;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch(item.getItemId()) {
            case R.id.high_score:
                fragment = fragmentGames;
                break;
            case R.id.setting:
                fragment = fragmentGames;
                break;
            case R.id.exit:
                System.exit(0);
        }
        ft.replace(R.id.fragment_container, fragment).commit();
        dl.closeDrawers();
        item.setChecked(true);
    }
}
