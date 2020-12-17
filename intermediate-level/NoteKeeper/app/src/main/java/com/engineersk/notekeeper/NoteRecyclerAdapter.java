package com.engineersk.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteViewHolder>{

    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNotes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextTitle.setText(note.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    protected class NoteViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTextCourse, mTextTitle;
        private int mCurrentPosition;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);
            mTextTitle = itemView.findViewById(R.id.text_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION_EXTRA, mCurrentPosition);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
