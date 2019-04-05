package com.ritika.multiNotes;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyAsyncTask extends AsyncTask<Integer, Integer, List<Note>>{

    private static final String TAG = "MyAsyncTask";
    private MainActivity mActivity;
    public static boolean running = false;

    public MyAsyncTask(MainActivity ma1) {
        mActivity = ma1;
    }

    @Override
    protected List<Note> doInBackground(Integer... para) {
        Log.d(TAG, "Start of background execution");
        List<Note> nList = new ArrayList<>();
        boolean listInitialized = false;
        try {
            InputStream str = mActivity.getApplicationContext().openFileInput(mActivity.getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(str, mActivity.getString(R.string.encoding)));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("number notes")) {
                    int val = Integer.parseInt(reader.nextString());
                    for (int x = 1; x <= val; x++) {
                        nList.add(new Note("Title " + x, "Text " + x, "Date " + x));
                    }
                    listInitialized = true;
                } else if (name.substring(0, 4).equals("date") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(5)) - 1).setLatestSavedDate(reader.nextString());
                } else if (name.substring(0, 4).equals("text") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(5)) - 1).setNoteText(reader.nextString());
                } else if (name.substring(0, 5).equals("title") && listInitialized) {
                    nList.get(Integer.parseInt(name.substring(6)) - 1).setNoteTitle(reader.nextString());

                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            Log.d(TAG, "loadNotes: Completed");

        } catch (FileNotFoundException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return nList;
    }

    @Override
    protected void onPostExecute(List<Note> myList) {

        super.onPostExecute(myList);
        mActivity.whenAsyncIsDone(myList);

        running = false;
    }

}
