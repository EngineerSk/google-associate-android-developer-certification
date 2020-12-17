package com.engineersk.notekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CourseRecyclerAdapter
        extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseViewHolder>{

    private final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context context, List<CourseInfo> Courses) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mCourses = Courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent,
                false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseInfo course = mCourses.get(position);
        holder.mTextCourse.setText(course.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    protected class CourseViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTextCourse;
        private int mCurrentPosition;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = itemView.findViewById(R.id.text_course);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
}
