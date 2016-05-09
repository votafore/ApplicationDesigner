package com.votafore.applicationdesigner.model;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

/**
 * класс, объект которого содержит информацию по конкретному объекту проекта
 * будь то какой-либо раздел или элемент списка
 */
public class Block {

    public Block(){
        mChilds = new ArrayList<>();

        mMode = GL_TRIANGLE_STRIP;

        Matrix.setIdentityM(mTranslateMatrix, 0);
        Matrix.setIdentityM(mResultTranslateMatrix, 0);
    }



    /**
     * РАЗДЕЛ: СТРУКТУРА СЦЕНЫ (ДЕРЕВО ОБЪЕКТОВ)
     * mChild содержит в себе список подчиненных объктов
     * Это могут быть:
     * - классы
     * - логические блоки
     * - что-то другое, что можно считать отдельным элементом программы или ее алгоритма
     */
    List<Block>   mChilds;
    Block         mParent;

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

        // TODO: следует учесть расположение объектов в плоскости
        // учесть, что для объектов, расположенных в разных плоскостях будут разные возможности перемещения
        // может не совсем верно сформулированно, но...
        // их расположение по-любому надо учитывать

        // рассчитаем матрицу перемещения
        Matrix.multiplyMM(mResultTranslateMatrix, 0, mTranslateMatrix, 0, mParent.getResTranslateMatrix(), 0);
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

    float mMinWidth = 0.1f;
    float mMinHeight = 0.1f;


    /**
     * mOrientation. Задает положение объекта:
     * - лежит                  {1,0,1}
     * - стоит (лицом к оси X)  {0,1,1}
     * - стоит (лицом к оси Z)  {1,1,0}
     */
    float[] mOrientation = {1.0f, 0.0f, 1.0f};

    // TODO: учесть расположение объекта
    // расположение объекта будет влиять на расположение его "потомков"
    // пока что это не учитывается, но это надо доделать.
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
    float mWidth;
    float mHeight;

    public void setSize(float width, float height){

        mWidth  = width;
        mHeight = height;
    }

    /**
     * mRelativeWidth и mRelativeHeight параметры относительных размеров
     * т.е. относительно размера родителя.
     * 1.0f = 100%
     */
    float mRelativeWidth;
    float mRelativeHeight;

    public void setRelativeWidth(float width){
        mRelativeWidth = width;
    }

    public void setRelativeHeight(float height){
        mRelativeHeight = height;
    }

    /**
     * mColor. Задает цвет вершин.
     */
    float[] mColor = {0.0f, 0.0f, 0.0f, 0.0f};

    public void setColor(float[] newColor){
        mColor = newColor;
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
     *
     * mTranslateMatrix. Матрица перемещения объекта на указанные значения.
     * в случае если эта матрица не является единичной, то автоматическое положение объекта
     * не расчитывается. Так же следует уточнить что это матрица относительного перемещения
     * ... т.е. относительно центра родителя
     *
     * mResulttranslateMatrix. Матрица которая получилась в результате расчета матрицы перемещения
     * родителя и матрицы относительного перемещения текущего объекта.
     */
    public static int POSITION_COUNT = 3;
    public static int COLOR_COUNT = 4;
    float[] mVertices;
    int mMode;

    float[] mTranslateMatrix        = new float[16];
    float[] mResultTranslateMatrix  = new float[16];

    public void setTranslateMatrix(float[] newMatrix){
        mTranslateMatrix = newMatrix;

        if(mParent == null){
            // в случае когда нет родителя учитывать нечего,
            // поэтому относительная матрица становится непосредственной
            mResultTranslateMatrix = mTranslateMatrix;
            return;
        }

        // рассчитаем матрицу перемещения
        Matrix.multiplyMM(mResultTranslateMatrix, 0, mTranslateMatrix, 0, mParent.getResTranslateMatrix(), 0);
    }

    public float[] getTranslateMatrix(){
        return mTranslateMatrix;
    }

    public float[] getResTranslateMatrix(){
        return mResultTranslateMatrix;
    }

    /**
     * @return массив с координатами вершин текущего объекта (тем самым определяя его форму)
     */
    public float[] getVertices(){
        return mVertices;
    }

    /**
     * функция, в которой будет создаваться массив с координатами вершин
     */
    public void initVertices() {

        // x
        float right;
        float left;

        // y
        float bottom;
        float top;


        // считаем что центр объекта находится посредине,
        // поэтому расчет координат такой:
        right = Math.max(mMinWidth, mWidth)/2*-1;
        left = right*-1;

        bottom = Math.max(mMinHeight, mHeight)/2*-1;
        top = bottom*-1;

        float[] vector1 = {right, 0.0f, top, 1.0f};
        float[] vector2 = {left, 0.0f, top, 1.0f};
        float[] vector3 = {right, 0.0f, bottom, 1.0f};
        float[] vector4 = {left, 0.0f, bottom, 1.0f};

        // объект сейчас лежит
        // надо проверить должен ли он таким остаться и если нет,
        // то сделать необходимые вычисления

        float[] matrix = new float[16];

        Matrix.setIdentityM(matrix, 0);

        // если установлена матрица перемещения, то надо ее применить
        Matrix.multiplyMM(matrix, 0, matrix, 0, mResultTranslateMatrix, 0);

        // матрицы поворота в обратном порядке
        if(mOrientation[2] == 0.0f){
            // доп поворт по оси Y
            Matrix.rotateM(matrix, 0, 90, 0, 1, 0);
        }

        if(mOrientation[0] == 0.0f||mOrientation[2] == 0.0f){
            // подъем объекта с лежачего положения (поворот по X)
            Matrix.rotateM(matrix, 0, 90, 1, 0, 0);
        }

        Matrix.multiplyMV(vector1, 0, matrix, 0, vector1, 0);
        Matrix.multiplyMV(vector2, 0, matrix, 0, vector2, 0);
        Matrix.multiplyMV(vector3, 0, matrix, 0, vector3, 0);
        Matrix.multiplyMV(vector4, 0, matrix, 0, vector4, 0);

        mVertices = new float[]{
                vector1[0], vector1[1], vector1[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector2[0], vector2[1], vector2[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector3[0], vector3[1], vector3[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector4[0], vector4[1], vector4[2], mColor[0], mColor[1], mColor[2], mColor[3]
        };
    }

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
