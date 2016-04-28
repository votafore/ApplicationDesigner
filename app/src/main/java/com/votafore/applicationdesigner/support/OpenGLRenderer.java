package com.votafore.applicationdesigner.support;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.votafore.applicationdesigner.glsupport.ShaderUtils;
import com.votafore.applicationdesigner.R;
import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.model.BlockActivity;
import com.votafore.applicationdesigner.model.BlockScene;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ALPHA;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glBlendFunc;

public class OpenGLRenderer implements Renderer{

    private Context context;


    // ДАННЫЕ ДЛЯ ШЕЙДЕРОВ
    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int programId;


    // ДАННЫЕ ДЛЯ УПРАВЛЕНИЯ ИЗОБРАЖЕНИЕМ
    private float[] mProjectionMatrix   = new float[16];
    private float[] mViewMatrix         = new float[16];
    private float[] mMatrix             = new float[16];


    // ДАННЫЕ ДЛЯ УПРАВЛЕНИЯ ПОЛОЖЕНИЕМ КАМЕРЫ
    float eyeX;
    float eyeY;
    float eyeZ;

    float deltaX;
    float angle;
    float radius;

    // ДАННЫЕ ОБЪЕКТОВ
    private Block   mRootBlock; // содержит корневой объект сцены
    private float[] myVertices; // массив вершин для отрисовки объектов
    private FloatBuffer vertexData; // буфер вершин


    public OpenGLRenderer(Context context) {
        this.context = context;

        mRootBlock = null;

        //eyeX = 3f;
        eyeY = 1f;
        //eyeZ = 3f;

        radius = 4f;

        deltaX = 0f;

        angle = ((this.deltaX % 25) / 25)  *  2 * 3.1415926f;

        eyeX = (float) (Math.cos(angle) * radius);
        eyeZ = (float) (Math.sin(angle) * radius);
    }


    // УПРАВЛЕНИЕ РИСОВАНИЕМ

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        glEnable(GL_DEPTH_TEST);

        int vertexShaderId      = ShaderUtils.createShader(context, GL_VERTEX_SHADER   , R.raw.vertex_shader);
        int fragmentShaderId    = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER , R.raw.fragment_shader);
        programId               = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);

        glUseProgram(programId);

        createViewMatrix();

        prepareData();
        bindData();
    }


    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);

        glBlendFunc(GL_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        createProjectionMatrix(width, height);
        bindMatrix();
    }


    @Override
    public void onDrawFrame(GL10 arg0) {

        createViewMatrix();
        bindMatrix();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(mRootBlock ==  null)
            return;

        drawBlock(mRootBlock, 0);
    }


    private void drawBlock(Block block, int counter){

        float[] color = block.getColor();
        glUniform4f(uColorLocation, color[0], color[1], color[2], color[3]);
        glDrawArrays(block.getMode(), counter, block.getVertexCount());

        counter += block.getVertexCount();

        List<Block> childs = block.getChilds();
        for(Block curBlock: childs){

            drawBlock(curBlock, counter);
        }
    }



    // УПРАВЛЕНИЕ ПОДГОТОВКОЙ ДАННЫХ

    private void prepareData(){

        mRootBlock = new BlockScene();
        mRootBlock.addChild(new BlockActivity());

        int count = getVertexCount(mRootBlock);
        myVertices = new float[count];

        toVertices(mRootBlock, 0);

        vertexData = ByteBuffer
                .allocateDirect(myVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(myVertices);
    }

    private void bindData() {
        // примитивы
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, Block.POSITION_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        uColorLocation = glGetUniformLocation(programId, "u_Color");

        // матрица
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void createProjectionMatrix(int width, int height) {

        float ratio;

        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 1;
        float far = 16;
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

    private void createViewMatrix() {

//        float time = (float)(SystemClock.uptimeMillis() % TIME) / TIME;
//        float angle = time  *  2 * 3.1415926f;

//        // точка положения камеры
//        float eyeX = 4f;//(float) (Math.cos(angle) * 4f);
//        float eyeY = 1f;
//        float eyeZ = 4f;//(float) (Math.sin(angle) * 4f);

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }



    // УПРАВЛЕНИЕ ИЗМЕНЕНИЕМ ПАРАМЕТРОВ

    public void setBlocks(Block block){
        mRootBlock = block;
        prepareData();
    }

    public void setX(float deltaX){
        this.deltaX += deltaX;

        angle = ((this.deltaX % 25) / 25)  *  2 * 3.1415926f;

        eyeX = (float) (Math.cos(angle) * radius);
        eyeZ = (float) (Math.sin(angle) * radius);
    }

    public void setZ(float deltaZ){
        radius += deltaZ;
    }





    // ДОПОЛНИТЕЛЬНЫЕ (ВСПОМОГАТЕЛЬНЫЕ) ПРОЦЕДУРЫ ФУНКЦИИ

    private int toVertices(Block block, int count){

        float[] objVertices = block.getVertices();

        // добавляем новые элементы
        for(float item: objVertices){

            myVertices[count] = item;
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