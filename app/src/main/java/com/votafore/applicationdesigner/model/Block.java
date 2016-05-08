package com.votafore.applicationdesigner.model;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

/**
 * класс, объект которого содержит информацию по конкретному объекту проекта
 * будь то какой-либо раздел или элемент списка
 */
public abstract class Block {

    public Block(){
        mChilds = new ArrayList<>();

        mMode = GL_TRIANGLE_STRIP;

        //initVertices();
    }



    /**
     * РАЗДЕЛ: СТРУКТУРА СЦЕНЫ (ДЕРЕВО ОБЪЕКТОВ)
     * mChild содержит в себе список подчиненных объктов
     * Это могут быть:
     * - классы
     * - логические блоки
     * - что-то другое, что можно считать отдельным элементом программы или ее алгоритма
     */
    protected List<Block>   mChilds;
    protected Block         mParent;

    public List<Block> getChilds(){
        return mChilds;
    }

    public void addChild(Block child){

        child.setParent(this);
        mChilds.add(child);
    }

    public void removeChild(int position){
        mChilds.remove(position);
    }

    public void removeChild(Block item){
        mChilds.remove(item);
    }

    public void setParent(Block parent){
        mParent = parent;
    }







    /**
     * РАЗДЕЛ: НАСТРОЙКИ ОБЪЕКТА
     * здесь определяется:
     * - размеры относительно родителя
     * - минимальные размеры
     * - ориентация в пространстве
     */



    /**
     * mMinWidth и mMinHeight. Ширина и высота вложенных (подчиненных объектов) уменьшается
     * в зависимости от уровня вложенности и настроек размера относительно родителя,
     * но размер не может быть меньше чем минимальный
     */

    protected float mMinWidth = 0.1f;
    protected float mMinHeight = 0.1f;


    /**
     * mOrientation. Задает положение объекта:
     * - лежит                  {1,0,1}
     * - стоит (лицом к оси Z)  {0,1,1}
     * - стоит (лицом к оси X)  {1,1,0}
     */
    protected float[] mOrientation = {1.0f, 0.0f, 1.0f};

    public void setOrientation(int x, int y, int z){

        mOrientation[0] = (float)x;
        mOrientation[1] = (float)y;
        mOrientation[2] = (float)z;
    }


    /**
     * mMinWidth и mMinHeight. Ширина и высота вложенных (подчиненных объектов) уменьшается
     * в зависимости от уровня вложенности и настроек размера относительно родителя,
     * но размер не может быть меньше чем минимальный
     */
    protected float mWidth;
    protected float mHeight;

    public void setSize(float width, float height){

        mWidth  = width;
        mHeight = height;
    }

    /**
     * mRelativeWidth и mRelativeHeight параметры относительных размеров
     * т.е. относительно размера родителя.
     * 1.0f = 100%
     */
    protected float mRelativeWidth;
    protected float mRelativeHeight;

    public void setRelativeWidth(float width){
        mRelativeWidth = width;
    }

    public void setRelativeHeight(float height){
        mRelativeHeight = height;
    }







    /**
     * РАЗДЕЛ: ОТОБРАЖЕНИЕ (РЕНДЕРИНГ)
     * этот раздел содержит в себе объекты, необходимые для
     * настройки отображения объекта на 3D сцене
     *
     * POSITION_COUNT - количество значений (float) которые задают координаты вершины
     *      в данном случае координаты вершины задаются тремя значениями
     *
     * COLOR_COUNT - количество значений (float) которые задают цвет вершины
     *
     * vertices - float массив в котором находятся координаты вершин объекта
     *
     * mMode - режим рисования вершин
     *      по умолчанию стоит GL_TRIANGLE_STRIP
     *      инициализируется в конструкторе
     */
    public static int POSITION_COUNT = 3;
    public static int COLOR_COUNT = 4;
    protected float[] mVertices;
    protected int mMode;


    /**
     * @return массив с координатами вершин текущего объекта (тем самым определяя его форму)
     */
    public float[] getVertices(){
        return mVertices;
    }

    /**
     * функция, в которой будет создаваться массив с координатами вершин
     * в объектах, расширяющие класс
     */
    public abstract void initVertices();

    /**
     * @return тип примитива, которым рисуется объект
     */
    public int getMode(){
        return mMode;
    }

    /**
     * @return количество вершин, необходимых для отрисовки объекта
     */
    public int getVertexCount(){
        return mVertices.length / (POSITION_COUNT + COLOR_COUNT);
    }












    /**
     * РАЗДЕЛ: ХРАНЕНИЕ В БАЗЕ ДАННЫХ
     * В данном разделе содержатся переменные, необходимые для
     * записи\чтения информации о проекте в\из базы данных
     *
     * mID          - ID объекта в базе данных
     * mProjectID   - ID проекта в котором находится объект
     * mParentID    - ID объекта "родителя"
     */
    protected int mID;
    protected int mProjectID;
    protected int mParentID;



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
}
