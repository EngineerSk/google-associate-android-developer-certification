package com.engineersk.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    public static final int COURSE_COLUMN_COUNT = 2;

    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private DrawerLayout mDrawer;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mNotesLinearLayoutManager;
    private NavigationView mNavigationView;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private StaggeredGridLayoutManager mCourseLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Called.");
        this.setContentView(R.layout.activity_main);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        FloatingActionButton fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: loading note content...");
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        this.mDrawer = this.findViewById(R.id.drawer_layout);

        this.mNavigationView = this.findViewById(R.id.nav_view);
        this.mNavigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        this.mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        this.initializeDisplayContent();

    }

    private void initializeDisplayContent() {
        Log.d(TAG, "initializeDisplayContent: Initializing Notes RecyclerView...");
        this.mRecyclerView = this.findViewById(R.id.list_items);
        this.mNotesLinearLayoutManager = new LinearLayoutManager(this);

        this.mCourseLayoutManager
                = new StaggeredGridLayoutManager(COURSE_COLUMN_COUNT, LinearLayoutManager.VERTICAL);

        this.displayNotes();
    }

    private void displayNotes() {
        Log.d(TAG, "displayNotes: Initializing recycler view for note items...");
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        this.mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        this.mRecyclerView.setLayoutManager(mNotesLinearLayoutManager);
        this.mRecyclerView.setAdapter(mNoteRecyclerAdapter);

        this.mNavigationView.getMenu().findItem(R.id.nav_notes).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Updating Notes RecyclerView...");
        this.mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(this.mDrawer.isDrawerOpen(GravityCompat.START))
            this.mDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_notes)
            this.displayNotes();

        else if(id == R.id.nav_courses)
            this.displayCourses();

        else if(id == R.id.nav_share){

        }else if(id == R.id.nav_send){

        }

        this.mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayCourses() {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        this.mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);
        this.mRecyclerView.setAdapter(mCourseRecyclerAdapter);
        this.mRecyclerView.setLayoutManager(mCourseLayoutManager);
        this.mNavigationView.getMenu().findItem(R.id.nav_courses).setChecked(true);
    }
}