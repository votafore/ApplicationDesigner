package com.votafore.applicationdesigner.model;

import android.graphics.Point;
import android.opengl.Matrix;

import com.votafore.applicationdesigner.support.BlockPlacement;

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

        mRelativeWidth = 1.0f;
        mRelativeHeight = 1.0f;

        mPlane = Plane.Y;
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
        //child.setOrientation(mPlane);
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
     * SPACE определяет размер отступа от края (если необходим)
     */
    public static float SPACE = 0.02f;

    /**
     * mMinWidth и mMinHeight. Ширина и высота вложенных (подчиненных объектов) уменьшается
     * в зависимости от уровня вложенности и настроек размера относительно родителя,
     * но размер не может быть меньше чем минимальный
     */

    float mMinWidth = 0.1f;
    float mMinHeight = 0.1f;


    /**
     * mPlane. Задает положение объекта в одной из плоскостей: X, Y или Z
     */

    public enum Plane{
        X,
        Y,
        Z
    }

    Plane mPlane;

    public void setOrientation(Plane plane){

        //  по умолчанию предполагается что будет установлена переданная плоскость
        //Plane newPlane = plane;

//        // но если есть родитель, то проверяем его плоскость
//        if(mParent != null){
//
//            // если это вертикальная плоскость
//            // то потомки могут быть только в этой же плоскости
//
//            Plane parentPlane = mParent.getPlane();
//
//            if(parentPlane != Plane.Y)
//                newPlane = parentPlane;
//        }

        // для объектов, расположенных вертикально
        // потомки могут иметь только вертикальное расположение

//        mPlane = newPlane;
//
//        if(mPlane != Plane.Y){
//
//            for(Block curBlock: mChilds){
//
//                curBlock.setOrientation(mPlane);
//            }
//        }

        mPlane = plane;
    }

    public Plane getPlane(){
        return mPlane;
    }


    /**
     * mMinWidth и mMinHeight. Ширина и высота вложенных (подчиненных объектов) уменьшается
     * в зависимости от уровня вложенности и настроек размера относительно родителя,
     * но размер не может быть меньше чем минимальный
     */
    float mWidth = 0.0f;
    float mHeight = 0.0f;

    public void setSize(float width, float height){

        mWidth  = width;
        mHeight = height;
    }

    public float getWidth(){
        return mWidth;
    }

    public float getHeight(){
        return mHeight;
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
    float[] mColor = {0.0f, 0.0f, 0.0f, 1.0f};

    public void setColor(float[] newColor){
        mColor = newColor;
    }

    /**
     * mPlacement. Интерфейс, который определяет (реализует) расположение
     * подчиненных блоков внутри текущего (вертикальное, горизонтальное или таблица... или еще как-то...)
     */

    BlockPlacement mPlacement;

    public void setPlacement(BlockPlacement placement){
        mPlacement = placement;
    }

    public BlockPlacement getPlacement(){
        return mPlacement;
    }


    /**
     * mPointInRelativeMatrix определяет относительное местоположение логического
     * блока на сцене
     */

    private Point mPointInRelativeMatrix;

    public void setPointInRelativeMatrix(Point point){
        mPointInRelativeMatrix = point;
    }

    public Point getPointInRelativeMatrix(){
        return mPointInRelativeMatrix;
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
    float[] mResultTranslateMatrix  = new float[16]; // подозреваю что это временная (на время тестов) матрица

    public void setTranslateMatrix(float[] newMatrix){
        mTranslateMatrix = newMatrix;
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
    public float[] initVertices() {

        // установим относительные размеры в случае необходимости
        // они устанавливаются ТОЛЬКО если ширина и высота не заданы или установлены в 0
        if((mWidth == 0.0f) && (mParent != null))
            mWidth = mParent.getWidth() * mRelativeWidth;

        if((mHeight == 0.0f) && (mParent != null))
            mHeight = mParent.getHeight() * mRelativeHeight;



        // рекурсивный вызов расчета в подчиненных объектах
        for(Block curBlock: mChilds){

            curBlock.initVertices();
        }





        // считаем что центр объекта находится посредине,
        // поэтому расчет координат такой:

        // x
        float right = Math.max(mMinWidth, mWidth)/2*-1;
        float left = right*-1;

        // y
        float bottom = Math.max(mMinHeight, mHeight)/2*-1;
        float top = bottom*-1;

        float[] vector1 = {right, 0.0f, top};
        float[] vector2 = {left, 0.0f, top};
        float[] vector3 = {right, 0.0f, bottom};
        float[] vector4 = {left, 0.0f, bottom};

        mVertices = new float[]{
                vector1[0], vector1[1], vector1[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector2[0], vector2[1], vector2[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector3[0], vector3[1], vector3[2], mColor[0], mColor[1], mColor[2], mColor[3],
                vector4[0], vector4[1], vector4[2], mColor[0], mColor[1], mColor[2], mColor[3]
        };

        return new float[]{mWidth, mHeight};
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
