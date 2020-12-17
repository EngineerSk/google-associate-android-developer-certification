package com.engineersk.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private static final String TAG = "NoteListActivity";

    private NoteRecyclerAdapter mNoteRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called.");
        this.setContentView(R.layout.activity_note_list);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        FloatingActionButton fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: loading note content...");
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        this.initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        Log.d(TAG, "initializeDisplayContent: Initializing Notes RecyclerView...");
        final RecyclerView notesRecyclerView = findViewById(R.id.list_notes);
        final LinearLayoutManager notesLinearLayoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setLayoutManager(notesLinearLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        notesRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Updating Notes RecyclerView...");
        this.mNoteRecyclerAdapter.notifyDataSetChanged();
    }
}