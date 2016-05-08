package com.votafore.applicationdesigner.controller;

import android.content.Context;

import com.votafore.applicationdesigner.model.DataBase;
import com.votafore.applicationdesigner.support.RecycleAdapter;

/**
 * Класс предназначен для управления работой с проектами и их данными.
 * т.е. тут будет список всех проектов, возможность их создания, редактирования
 * и удаления. Для этого он будет единственным (Singletone)
 */
public class ProjectListManager {

    private static ProjectListManager mThis;

    private DataBase        mDataBase;
    private RecycleAdapter  mAdapter;


    /////////////////////////////////////////////
    // ОСНОВНОЙ РАЗДЕЛ КЛАССА

    public static ProjectListManager getInstance(Context ctx){

        // TODO кажется реализация этого класса в виде синглтона - излишество. На данный момент в этом не смысла.
        if(mThis == null)
            mThis = new ProjectListManager(ctx);

        return mThis;
    }

    private ProjectListManager(Context ctx){

        mDataBase   = new DataBase(ctx);
        mAdapter    = new RecycleAdapter(ctx, mDataBase.getProjects());
    }
    //
    /////////////////////////////////////////////




    /////////////////////////////////////////////
    // УПРАВЛЕНИЕ АДАПТЕРОМ RecyclerView

    public RecycleAdapter getAdapter(){
        return mAdapter;
    }
    //
    /////////////////////////////////////////////




    /////////////////////////////////////////////
    // УПРАВЛЕНИЕ ПРОЕКТАМИ

    public void createProject(String name){

        mDataBase.createProject(name);

        mAdapter.setList(mDataBase.getProjects());
        mAdapter.notifyItemInserted(mAdapter.getProjects().size());
    }

    public void deleteProject(int index){

        mDataBase.deleteProject(mAdapter.getProjects().get(index).getId());

        mAdapter.setList(mDataBase.getProjects());
        mAdapter.notifyItemRemoved(index);
    }
    //
    /////////////////////////////////////////////

}
