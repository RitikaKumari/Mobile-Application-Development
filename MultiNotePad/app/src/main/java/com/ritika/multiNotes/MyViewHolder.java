package com.ritika.multiNotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView noteTitle;
    public TextView noteText;
    public TextView noteLatestSavedDate;

    public MyViewHolder(View view) {
        super(view);
        noteTitle = (TextView) view.findViewById(R.id.noteTitle);
        noteText = (TextView) view.findViewById(R.id.noteText);
        noteLatestSavedDate = (TextView) view.findViewById(R.id.noteLatestSavedDate);
    }

}
