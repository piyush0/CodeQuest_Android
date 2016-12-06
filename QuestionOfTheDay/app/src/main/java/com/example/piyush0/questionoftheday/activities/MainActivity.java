package com.example.piyush0.questionoftheday.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.piyush0.questionoftheday.R;
import com.example.piyush0.questionoftheday.fragments.ArchiveFragment;
import com.example.piyush0.questionoftheday.fragments.ChallengeFragment;
import com.example.piyush0.questionoftheday.fragments.TodayQuestionFragment;
import com.example.piyush0.questionoftheday.utils.FontsOverride;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager fragmentManager;
    private OnFragmentChangedListener ofcl;
    private static OnItemSelected onItemSelected;

    public void setOnFragmentChangedListener(OnFragmentChangedListener var){
        this.ofcl = var;
    }

    public static void setOnItemSelected(MainActivity.OnItemSelected var){
        MainActivity.onItemSelected = var;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FontsOverride.applyFontForToolbarTitle(this, FontsOverride.FONT_PROXIMA_NOVA);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

    }

    public void init() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_main,
                        TodayQuestionFragment.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        setOnFragmentChangedListener(new OnFragmentChangedListener() {
            @Override
            public void fragmentStatus(int id) {
                if(id == R.id.nav_archive){
                    menu.clear();
                    getMenuInflater().inflate(R.menu.filter_menu_archive,menu);
                }
                else{
                    menu.clear();
                    getMenuInflater().inflate(R.menu.main, menu);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_myProfile) {
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_java){
            this.onItemSelected.filterChosen("Java");
        }
        if(id == R.id.action_cpp){
            this.onItemSelected.filterChosen("Cpp");
        }
        if(id == R.id.action_android){
            this.onItemSelected.filterChosen("Android");
        }
        if(id == R.id.action_javascript){
            this.onItemSelected.filterChosen("JavaScript");
        }
        if(id == R.id.action_python){
            this.onItemSelected.filterChosen("Python");
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_today) {
            ofcl.fragmentStatus(R.id.nav_today);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            TodayQuestionFragment.newInstance()).commit();
        } else if (id == R.id.nav_challenge) {
            ofcl.fragmentStatus(R.id.nav_challenge);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            ChallengeFragment.newInstance()).commit();

        } else if (id == R.id.nav_archive) {
            ofcl.fragmentStatus(R.id.nav_archive);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main,
                            ArchiveFragment.newInstance()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface OnFragmentChangedListener {
        void fragmentStatus(int id);
    }

    public interface OnItemSelected{
        void filterChosen(String filter);
    }
}
