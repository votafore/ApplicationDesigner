package com.votafore.applicationdesigner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * класс для работы с базой данных
 */
public class DataBase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION   = 1;
    private static final String DATABASE_NAME   = "ProjectBase.db";

    // имена таблиц
    private static final String TABLE_PROJECT       = "projects";
    private static final String TABLE_PROJECTDATA   = "ProjectData";

    // имена колонок
    private static final String ID              = "id";
    private static final String TITLE           = "title";

    private static final String ID_PROJECT      = "project_id";
    private static final String ID_PARENT       = "parent_id";

    public DataBase(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable_project = "CREATE TABLE " + TABLE_PROJECT + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + "," + TITLE + " TEXT"
                +  ")";

        db.execSQL(createTable_project);

        String createTable_projectData = "CREATE TABLE " + TABLE_PROJECTDATA + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
                + "," + ID_PROJECT +  " INTEGER"
                + "," + ID_PARENT +  " INTEGER"
                +")";

        db.execSQL(createTable_projectData);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);

        onCreate(db);
    }


    //////////////////////////////
    // работа со списком проектов

    public List<Project> getProjects(){

        List<Project> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROJECT, null);

        if (cursor.moveToFirst()){
            do{
                Project project = new Project(cursor.getString(1),cursor.getInt(0));
                list.add(project);
            }while (cursor.moveToNext());
        }

        cursor.close();

        db.close();

        return list;
    }

    public void createProject(String name){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, name);

        db.insert(TABLE_PROJECT, null, values);

        db.close();
    }

    public void deleteProject(int id){

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_PROJECT, ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }
    //
    /////////////////////////////



    /////////////////////////////
    // работа с данными проекта

    public int getAllDataCount(int id){

        SQLiteDatabase db = getWritableDatabase();

        //Cursor cursor = db.query(TABLE_PROJECTDATA, null, ID_PROJECT+"=?", new String[]{String.valueOf(id)},null, null, null);

        Cursor cursor = db.rawQuery("select * from " + TABLE_PROJECTDATA + " where " + ID_PROJECT + "=?", new String[]{String.valueOf(id)});

        db.close();

        int result = cursor.getCount();
        cursor.close();

        return result;
    }

    public void populateBlock(Block block){

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_PROJECTDATA, null, ID_PARENT+"=?", new String[]{String.valueOf(block.getID())},null, null, null);

        db.close();

        if(cursor.moveToFirst()){
            do {
                // TODO
                // на самом деле потом надо будет определять тип и от него
                // отталкиваться при создании блока
                Block child = new BlockActivity();

                child.setID(cursor.getInt(0));
                child.setProjectID(cursor.getInt(1));
                child.setParentID(cursor.getInt(2));

                block.addChild(child);

            }while (cursor.moveToNext());
        }
    }
    //
    /////////////////////////////
}