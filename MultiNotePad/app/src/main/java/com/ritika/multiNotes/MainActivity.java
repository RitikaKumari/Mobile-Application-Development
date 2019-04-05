package com.ritika.multiNotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener{

        private static final String TAG = "MainActivity";
        private List<Note> notesList = new ArrayList<>();
        private RecyclerView rView;
        private NoteAdapter mAdapter;
        private int latestPosition = 0;
        private static final int EDIT_REQ_CODE = 1;
        private static final int CREATE_REQUEST_CODE = 2;
        public static final int RESULT_NO_MODIFICATION = 5;


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        rView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        mAdapter = new NoteAdapter(notesList, this);
        rView.setAdapter(mAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        MyAsyncTask.running = true;
        new MyAsyncTask(this).execute();
}

    @Override
    public void onClick(View v) {

        int position = rView.getChildLayoutPosition(v);
        Note m = notesList.get(position);
        latestPosition = position;
        modifyExistingNote(m);
    }

    public void modifyExistingNote(Note m){
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "Edit note");
        intent.putExtra("Title", m.getNoteTitle());
        intent.putExtra("Text", m.getNoteText());
        startActivityForResult(intent, EDIT_REQ_CODE);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = rView.getChildLayoutPosition(v);
        Note m = notesList.get(pos);
        latestPosition = pos;
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Confirmation")
                .setMessage("Do you want to delete the note " + m.getNoteTitle()+" ?")
                .setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No",dialogClickListener)
                .show();


        return false;
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Log.d(TAG, " Button : YES ");
                    Log.d(TAG, "Remove element " + latestPosition + " from list");
                    notesList.remove(latestPosition);
                    mAdapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    Log.d(TAG, " Button : NO ");
                    break;
            }
        }
    };



        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.main_item_edit:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                initiateEditActivity();
                break;
            case R.id.main_item_info:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                initiateInfoActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initiateInfoActivity() {
        Intent intent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(intent);
    }

    public void initiateEditActivity() {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(intent, CREATE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String newNoteText = data.getStringExtra("NEW_NOTE_TEXT");
                String newNoteTitle = data.getStringExtra("NEW_NOTE_TITLE");
                String newNoteDate = data.getStringExtra("NEW_NOTE_DATE");
                if ( !newNoteTitle.equals("")){
                    notesList.add(0,new Note(newNoteTitle, newNoteText, newNoteDate));
                }
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onActivityResult: New Note Added Create");
            } else {
                Toast.makeText(this, "The un-titled note wasn't saved", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        }
        if (requestCode == EDIT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                String newText = data.getStringExtra("NEW_NOTE_TEXT");
                String newTitle = data.getStringExtra("NEW_NOTE_TITLE");
                String newDate = data.getStringExtra("NEW_NOTE_DATE");
                notesList.remove(latestPosition);
                notesList.add(0,new Note(newTitle, newText, newDate));
                mAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "Result Code on ActivityResult: r: " + resultCode);
            }

        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        saveNotes();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    private void saveNotes() {
            
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("number notes").value(notesList.size());
            int realI = 1;
            for (int j = 1; j <= notesList.size(); j++){
                if (notesList.get(j-1).getNoteTitle().equals("")){
                    Log.d(TAG, "saveNotes: Element " + j + " doesn't have a title");
                }
                else{
                    writer.name("title " + realI).value(notesList.get(j-1).getNoteTitle());
                    writer.name("date " + realI).value(notesList.get(j-1).getLatestSavedDate());
                    writer.name("text " + realI).value(notesList.get(j-1).getNoteText());
                    realI ++;
                }
            }
            writer.endObject();
            writer.close();
            
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void whenAsyncIsDone(List<Note> nList) {

        Log.d(TAG, "whenAsyncIsDone Start");
        this.notesList.clear();
        this.notesList.addAll(nList);
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "whenAsyncIsDone ");

    }

    public void readList (List<Note> myNoteList){
            for (int i = 0; i < myNoteList.size(); i++){
                Log.d(TAG, "readList: Title "+i+" is " + myNoteList.get(i).getNoteTitle());
                Log.d(TAG, "readList: Date  "+i+" is " + myNoteList.get(i).getLatestSavedDate());
                Log.d(TAG, "readList: Text  "+i+" is " + myNoteList.get(i).getNoteText());
            }
    }
}
