package com.example.android.gamesbola;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
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

        fragmentGames = FragmentGames.newInstance(this,"GAME FRAGMENT");
        fragmentMenu = FragmentMenu.newInstance(this, "Menu Fragment");
        fragmentManager = getSupportFragmentManager();
        changePage(PAGE_MENU);
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
                dl.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
