package com.votafore.applicationdesigner.model;


public class Project{

    String mTitle;
    int mId;

    public Project(){
    }

    public Project(String title, int id){

        mTitle = title;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public int getId(){
        return mId;
    }
}