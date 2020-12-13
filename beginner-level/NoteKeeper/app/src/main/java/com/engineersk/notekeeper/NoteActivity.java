package com.engineersk.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = NoteActivity.class.getSimpleName();
    public static final String NOTE_POSITION_EXTRA = "com.engineersk.notekeeper.note_position";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private boolean mIsNewNote;
    private int mNotePosition;
    private boolean mIsCancelling = false;

    private NoteActivityViewModel mNoteActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called.");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_note);
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(this.getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()));

        this.mNoteActivityViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(this.mNoteActivityViewModel.mViewModelIsNewlyCreated && savedInstanceState != null)
            this.mNoteActivityViewModel.restoreState(savedInstanceState);

        this.mNoteActivityViewModel.mViewModelIsNewlyCreated = false;

        this.mSpinnerCourses = this.findViewById(R.id.spinner_courses);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseInfoArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courses);
        courseInfoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mSpinnerCourses.setAdapter(courseInfoArrayAdapter);

        this.mTextNoteTitle = this.findViewById(R.id.text_note_title);
        this.mTextNoteText = this.findViewById(R.id.text_note_text);

        this.readDisplayStateValues();
        this.saveOriginalNote();

        if(!this.mIsNewNote)
            this.displayNotes();
    }

    private void saveOriginalNote() {
        if(this.mIsNewNote)
            return;

        this.mNoteActivityViewModel.setOriginalCourseId(this.mNote.getCourse().getCourseId());
        this.mNoteActivityViewModel.setOriginalNoteTitle(this.mNote.getTitle());
        this.mNoteActivityViewModel.setOriginalNoteText(this.mNote.getText());
    }

    private void displayNotes() {
        Log.d(TAG, "displayNotes: Displaying note info!!!");
        CourseInfo course = this.mNote.getCourse();
        int courseIndex = DataManager.getInstance().getCourses().indexOf(course);

        this.mSpinnerCourses.setSelection(courseIndex);
        this.mTextNoteTitle.setText(this.mNote.getTitle());
        this.mTextNoteText.setText(this.mNote.getText());
    }

    private void readDisplayStateValues() {
        Log.d(TAG, "readDisplayStateValues: " +
                "retrieving intent parcelable extra and updating note...");
        int position = this.getIntent().getIntExtra(NOTE_POSITION_EXTRA, POSITION_NOT_SET);
        this.mIsNewNote = (position == POSITION_NOT_SET);
        if(!this.mIsNewNote)
            this.mNote = DataManager.getInstance().getNotes().get(position);
        else
            this.createNewNote();
    }

    private void createNewNote() {
        Log.d(TAG, "createNewNote: creating new note on note position!!!");
        DataManager dataManager = DataManager.getInstance();
        this.mNotePosition = dataManager.createNewNote();
        this.mNote = dataManager.getNotes().get(this.mNotePosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(this.mIsCancelling) {
            if(this.mIsNewNote)
                DataManager.getInstance().removeNote(this.mNotePosition);
            else
                this.storePreviousNoteValues();
        }
        else
            this.saveNote();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mNoteActivityViewModel.saveState(outState);
    }

    private void storePreviousNoteValues() {
        this.mNote.setCourse(DataManager.getInstance()
                .getCourse(this.mNoteActivityViewModel.getOriginalCourseId()));
        this.mNote.setTitle(this.mNoteActivityViewModel.getOriginalNoteTitle());
        this.mNote.setText(this.mNoteActivityViewModel.getOriginalNoteText());
    }

    private void saveNote() {
        this.mNote.setCourse((CourseInfo) this.mSpinnerCourses.getSelectedItem());
        this.mNote.setTitle(this.mTextNoteTitle.getText().toString());
        this.mNote.setText(this.mTextNoteText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            this.sendEmail();
        }else if(id == R.id.action_cancel){
            this.mIsCancelling = true;
            this.finish();
        }
        else
            return super.onOptionsItemSelected(item);

        return true;
    }

    private void sendEmail() {
        Log.d(TAG, "sendEmail: sending note...");
        CourseInfo course = (CourseInfo) this.mSpinnerCourses.getSelectedItem();
        String subject = this.mTextNoteTitle.getText().toString();
        String text = "Check out what I learnt in the plural sight course " +
                "\""+course.getTitle()+ "\"\n" + this.mTextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        this.startActivity(intent);
    }
}