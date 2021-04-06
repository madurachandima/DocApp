package com.example.docapp.category_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.docapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryMusicMain extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_music_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setSelectedItemId(R.id.nav_favorites);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.container, mFragmentMusicFavorite)
                .commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    FragmentMusicFavorite mFragmentMusicFavorite = new FragmentMusicFavorite();
    FragmentMusicSearch mFragmentMusicSearch = new FragmentMusicSearch();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.container, mFragmentMusicFavorite)
                        .commit();
                return true;

            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.container, mFragmentMusicSearch)
                        .commit();
                return true;
        }
        return false;
    }
}