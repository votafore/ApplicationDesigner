package com.votafore.applicationdesigner.support;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.glsupport.ShaderUtils;
import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.R;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class OpenGLRenderer implements Renderer{

    private Context context;

    private String TAG = "MyEvent";



    // ДАННЫЕ ДЛЯ ШЕЙДЕРОВ
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int programId;


    // ДАННЫЕ ДЛЯ УПРАВЛЕНИЯ ИЗОБРАЖЕНИЕМ
    private float[] mProjectionMatrix   = new float[16];
    private float[] mViewMatrix         = new float[16];
    private float[] mMatrix             = new float[16];


//    // ДАННЫЕ ДЛЯ УПРАВЛЕНИЯ ПОЛОЖЕНИЕМ КАМЕРЫ
    float eyeX;
    float eyeY;
    float eyeZ;

    float deltaX;
    float angle;
    float radius;

    float mX;
    float mY;

    // точка направления камеры
    float mCenterX = 0;
    float mCenterY = 0;
    float mCenterZ = 0;

    // up-вектор
    float mUpX = 0;
    float mUpY = 1;
    float mUpZ = 0;

    // ДАННЫЕ ОБЪЕКТОВ
    private Block   mRootBlock;     // содержит корневой объект сцены
    private float[] vertices;       // массив вершин для отрисовки объектов
    private FloatBuffer vertexData; // буфер вершин


    public OpenGLRenderer(Context context) {
        this.context = context;

        mRootBlock = null;

        radius = 4f;

        deltaX = 0f;

        angle = ((this.deltaX % 25) / 25)  *  2 * 3.1415926f;

        eyeY = 1.2f;
        eyeX = (float) (Math.cos(angle) * radius);
        eyeZ = (float) (Math.sin(angle) * radius);

        // настройки направления камеры
        mCenterX = 0;
        mCenterY = 0;
        mCenterZ = 0;

        mUpX = 0;
        mUpY = 1;
        mUpZ = 0;

        prepareData();
    }


    // УПРАВЛЕНИЕ РИСОВАНИЕМ

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
        glEnable(GL_DEPTH_TEST);
        //glDisable(GL_DEPTH_TEST);

//        glEnable(GL_BLEND);
//        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int vertexShaderId      = ShaderUtils.createShader(context, GL_VERTEX_SHADER   , R.raw.vertex_shader);
        int fragmentShaderId    = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER , R.raw.fragment_shader);
        programId               = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);

        glUseProgram(programId);

        aPositionLocation   = glGetAttribLocation(programId, "a_Position");
        aColorLocation      = glGetAttribLocation(programId, "a_Color");
        uMatrixLocation     = glGetUniformLocation(programId, "u_Matrix");

        // примитивы
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, Block.POSITION_COUNT, GL_FLOAT, false, (Block.POSITION_COUNT + Block.COLOR_COUNT)*4, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        vertexData.position(Block.POSITION_COUNT);
        glVertexAttribPointer(aColorLocation, Block.COLOR_COUNT, GL_FLOAT, false, (Block.POSITION_COUNT + Block.COLOR_COUNT)*4, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }


    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);

        // настраиваем матрицу проекции
        float ratio;

        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 1;
        float far = 12;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }


    @Override
    public void onDrawFrame(GL10 arg0) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(mRootBlock ==  null)
            return;

        // создаем матрицу вида
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, mCenterX, mCenterY, mCenterZ, mUpX, mUpY, mUpZ);

        // соединяем матрицы вида и проекции
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);

        drawBlock(mRootBlock, 0);
    }


    private int drawBlock(Block block, int counter){

        glDrawArrays(block.getMode(), counter, block.getVertexCount());

        counter += block.getVertexCount();

        List<Block> childs = block.getChilds();
        for(Block curBlock: childs){

            counter = drawBlock(curBlock, counter);
        }

        return counter;
    }



    // УПРАВЛЕНИЕ ПОДГОТОВКОЙ ДАННЫХ

    private void prepareData(){

        if(mRootBlock == null){
            // если объект еще не назначен
            // создаем пустой буфер
            vertices = new float[0];
        }else{
            // иначе заполняем буфер данными вершин
            int count = getVertexCount(mRootBlock);
            vertices = new float[count];
            toVertices(mRootBlock, 0);
        }

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }



    // УПРАВЛЕНИЕ ИЗМЕНЕНИЕМ ПАРАМЕТРОВ

    public void setBlocks(Block block){
        mRootBlock = block;
        prepareData();
    }

    public void onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
//                Log.d(TAG, "action move new X:" + String.valueOf(event.getX()) + " new Y:"+String.valueOf(event.getY()));
//                Log.d(TAG, "delta X:" + String.valueOf(mX - event.getX()) + " delta Y:"+String.valueOf(mY - event.getY()));
                break;
            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG, "action down X:" + String.valueOf(event.getX()) + " Y:"+String.valueOf(event.getY()));
                mX = event.getX();
                mY = event.getY();
                return;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up");
                return;
        }

        // устанавливаем расстояние камеры
        radius += (mY - event.getY())/100;

        // устанавливаем угол поворота (место просмотра) для камеры
        this.deltaX += (event.getX() - mX)/100;
        angle = ((this.deltaX % 25) / 25)  *  2 * 3.1415926f;

        eyeX = (float) (Math.cos(angle) * radius);
        eyeZ = (float) (Math.sin(angle) * radius);

//        Log.d(TAG, "event.getX():" + String.valueOf(event.getX()) + " event.getY():"+String.valueOf(event.getY()));
//        Log.d(TAG, "delta X:" + String.valueOf((event.getX() - mX)/100) + " delta Y:"+String.valueOf((mY - event.getY())/100));
//        Log.d(TAG, "delta X:" + String.valueOf(deltaX) + " radius (Y):"+String.valueOf(radius));

        mX = event.getX();
        mY = event.getY();
    }



    // ДОПОЛНИТЕЛЬНЫЕ (ВСПОМОГАТЕЛЬНЫЕ) ПРОЦЕДУРЫ ФУНКЦИИ

    private int toVertices(Block block, int count){

        float[] objVertices = block.getVertices();

        // добавляем новые элементы
        for(float item: objVertices){

            vertices[count] = item;
            count++;
        }

        List<Block> childs = block.getChilds();
        for(Block curBlock: childs){

            count = toVertices(curBlock, count);
        }

        return count;
    }

    private int getVertexCount(Block block){

        int count = 0;

        for(Block child: block.getChilds()){

            count += getVertexCount(child);
        }

        return block.getVertices().length + count;
    }

}