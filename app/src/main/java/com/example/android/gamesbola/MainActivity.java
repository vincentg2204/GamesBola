package com.example.android.gamesbola;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FragmentGames fragmentGames;
    private FragmentManager fragmentManager;
    private MainPresenter presenter;

    public static int PAGE_GAMES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter();

        fragmentGames = FragmentGames.newInstance(this,presenter,"GAME FRAGMENT");

        fragmentManager = getSupportFragmentManager();
        changePage(PAGE_GAMES);
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
        ft.commit();
    }
}
