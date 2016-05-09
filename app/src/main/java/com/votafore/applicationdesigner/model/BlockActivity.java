package com.votafore.applicationdesigner.model;


import android.opengl.Matrix;

public class BlockActivity extends Block {

    public BlockActivity(){
        super();
    }

    @Override
    public void initVertices() {

        float[] color = new float[]{0.0f, 1.0f, 0.0f, 0.5f};

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


        if(mOrientation[0] == 0.0f||mOrientation[2] == 0.0f){
            // подъем объекта с лежачего положения (поворот по X)
            Matrix.rotateM(matrix, 0, 90, 1, 0, 0);
        }

        if(mOrientation[2] == 0.0f){
            // доп поворт по оси Y
            Matrix.rotateM(matrix, 0, 90, 0, 1, 0);
        }

        Matrix.multiplyMV(vector1, 0, matrix, 0, vector1, 0);
        Matrix.multiplyMV(vector2, 0, matrix, 0, vector2, 0);
        Matrix.multiplyMV(vector3, 0, matrix, 0, vector3, 0);
        Matrix.multiplyMV(vector4, 0, matrix, 0, vector4, 0);

        mVertices = new float[]{
                vector1[0], vector1[1], vector1[2], color[0], color[1], color[2], color[3],
                vector2[0], vector2[1], vector2[2], color[0], color[1], color[2], color[3],
                vector3[0], vector3[1], vector3[2], color[0], color[1], color[2], color[3],
                vector4[0], vector4[1], vector4[2], color[0], color[1], color[2], color[3]
        };
    }
}