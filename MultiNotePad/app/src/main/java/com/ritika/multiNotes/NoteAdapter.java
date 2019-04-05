package com.ritika.multiNotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NoteAdapter";
    private List<Note> nList;
    private MainActivity mact;

    public NoteAdapter(List<Note> emptyList, MainActivity m1) {
        this.nList = emptyList;
        mact = m1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mact);
        itemView.setOnLongClickListener((View.OnLongClickListener) mact);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note myNote = nList.get(position);
        holder.noteTitle.setText(myNote.getNoteTitle());
        if (myNote.getNoteText().length() < 80 ){
            holder.noteText.setText(myNote.getNoteText());
        }
        else {
            holder.noteText.setText(myNote.getNoteText().substring(0,80) + " ...");
        }
        holder.noteLatestSavedDate.setText(myNote.getLatestSavedDate());
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

}
