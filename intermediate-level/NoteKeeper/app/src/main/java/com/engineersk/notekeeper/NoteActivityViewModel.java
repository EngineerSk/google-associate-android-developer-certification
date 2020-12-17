package com.engineersk.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    private static final String ORIGINAL_NOTE_COURSE_ID = "com.engineersk.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    private static final String ORIGINAL_NOTE_TITLE = "com.engineersk.notekeeper.ORIGINAL_NOTE_TITLE";
    private static final String ORIGINAL_NOTE_TEXT = "com.engineersk.notekeeper.ORIGINAL_NOTE_TEXT";

    private String mOriginalCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;

    public boolean mViewModelIsNewlyCreated = true;

    public String getOriginalCourseId() {
        return mOriginalCourseId;
    }

    public String getOriginalNoteTitle() {
        return mOriginalNoteTitle;
    }

    public String getOriginalNoteText() {
        return mOriginalNoteText;
    }

    public void setOriginalCourseId(String originalCourseId) {
        mOriginalCourseId = originalCourseId;
    }

    public void setOriginalNoteTitle(String originalNoteTitle) {
        mOriginalNoteTitle = originalNoteTitle;
    }

    public void setOriginalNoteText(String originalNoteText) {
        mOriginalNoteText = originalNoteText;
    }

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, getOriginalCourseId());
        outState.putString(ORIGINAL_NOTE_TITLE, getOriginalNoteTitle());
        outState.putString(ORIGINAL_NOTE_TEXT, getOriginalNoteText());
    }

    public void restoreState(Bundle inState){
        setOriginalCourseId(inState.getString(ORIGINAL_NOTE_COURSE_ID));
        setOriginalNoteTitle(inState.getString(ORIGINAL_NOTE_TITLE));
        setOriginalNoteText(inState.getString(ORIGINAL_NOTE_TEXT));
    }
}
