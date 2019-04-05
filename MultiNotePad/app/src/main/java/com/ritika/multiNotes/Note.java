package com.ritika.multiNotes;

public class Note {

    private String noteTitle;
    private String noteText;
    private String latestSavedDate;


    public Note(String noteTitle, String noteText, String latestSavedDate) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.latestSavedDate = latestSavedDate;
    }

    public String getNoteTitle () {
        return this.noteTitle;
    }
    public String getNoteText() {
        return this.noteText;
    }
    public String getLatestSavedDate() {
        return this.latestSavedDate;
    }

    public void setNoteTitle (String newTitle)
    {
        this.noteTitle = newTitle;
    }
    public void setNoteText(String newText){
        this.noteText = newText;
    }
    public void setLatestSavedDate(String newLatestdate){
        this.latestSavedDate = newLatestdate;
    }

    public String toSaveFormat() {
        return "    START \n" + this.noteTitle + " \n" + this.latestSavedDate + "\n" +this.noteText + " END \n";
    }

    @Override
    public String toString() {
        return "Title : " + this.noteTitle + " \n" + "Date :" + this.latestSavedDate + "\n" + "Text :" +this.noteText ;
    }

}
