package com.votafore.applicationdesigner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * класс, объект которого содержит информацию по конкретному объекту проекта
 * будь то какой-либо раздел или элемент списка
 */
public abstract class Block {

    protected List<Block> mChilds;

    public static int POSITION_COUNT = 3; // говорим что для описания одной вершины надо 3 float значения
    protected float[] vertices;

    protected int mID;        // ИД этого объекта в базе данных
    protected int mProjectID; // ИД проекта к которому принадлежит объект
    protected int mParentID;  // ИД объекта "Родителя"


    public Block(){
        mChilds = new ArrayList<>();
    }

    public int getID(){
        return mID;
    }

    public void setID(int id){
        mID = id;
    }

    public int getParentID() {
        return mParentID;
    }

    public void setParentID(int mParentID) {
        this.mParentID = mParentID;
    }

    public int getProjectID() {
        return mProjectID;
    }

    public void setProjectID(int mProjectID) {
        this.mProjectID = mProjectID;
    }








    //////////////////////////////////////////////
    // процедуры управления структурой

    public List<Block> getChilds(){
        return mChilds;
    }

    public void addChild(Block child){
        mChilds.add(child);
    }

    public void removeChild(int position){
        mChilds.remove(position);
    }

    public void removeChild(Block item){
        mChilds.remove(item);
    }



    //////////////////////////////////////////////
    // процедуры управления отображением объекта

    /**
     * функция возвращает массив с координатами текущего объекта
     * тем самым определяя его форму
     *
     * @return
     */
    public abstract float[] getVertices();

    /**
     * функция возвращает цвет объекта
     */
    public abstract float[] getColor();

    /**
     * функция возвращает тип примитива, которым рисуется объект
     */
    public abstract int getMode();

    /**
     * функция возвращает количество вершин, необходимых для
     * отрисовки объекта
     */
    public abstract int getVertexCount();
}
