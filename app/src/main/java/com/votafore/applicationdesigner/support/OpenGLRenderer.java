package com.votafore.applicationdesigner.support;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

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

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
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

public class OpenGLRenderer implements Renderer{

    private final static int POSITION_COUNT = 3;

    private final static long TIME = 10000;

    private Context context;

    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;
    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMatrix = new float[16];

    private Block   mRootBlock; // содержит корневой объект сцены
    private float[] myVertices; // массив вершин для отрисовки объектов

    public OpenGLRenderer(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0.5f, 0.5f, 0.5f, 1f);
        glEnable(GL_DEPTH_TEST);
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
        createViewMatrix();

        prepareData();
        bindData();
    }


    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        bindMatrix();
    }


    @Override
    public void onDrawFrame(GL10 arg0) {

        createViewMatrix();
        bindMatrix();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        drawBlock(mRootBlock, 0);
    }







    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 8;
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

        float time = (float)(SystemClock.uptimeMillis() % TIME) / TIME;
        float angle = time  *  2 * 3.1415926f;

        // точка положения камеры
        float eyeX = 4f;//(float) (Math.cos(angle) * 4f);
        float eyeY = 1f;
        float eyeZ = 4f;//(float) (Math.sin(angle) * 4f);

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

    public void setBlocks(Block block){
        mRootBlock = block;
        prepareData();
    }

    private void prepareData(){

        mRootBlock = new BlockScene();
        mRootBlock.addChild(new BlockActivity());

        myVertices = new float[]{};

        toVertices(mRootBlock);

        vertexData = ByteBuffer
                .allocateDirect(myVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(myVertices);
    }

    private void toVertices(Block block){

        float[] objVertices = block.getVertices();

        int count = myVertices.length;
        int newCount = count + objVertices.length;

        // создаем новый массив
        float[] newVert = new float[newCount];

        // переносим в него данные из существующего
        for(int i = 0;i < myVertices.length; i++ ){
            newVert[i] = myVertices[i];
        }

        // добавляем новые элементы
        for(float item: objVertices){

            newVert[count] = item;
            count++;
        }

        // "переносим" данные в нужный массив
        myVertices = newVert;

        List<Block> childs = block.getChilds();
        for(Block curBlock: childs){

            toVertices(curBlock);
        }
    }

    private void bindData() {
        // примитивы
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        uColorLocation = glGetUniformLocation(programId, "u_Color");

        // матрица
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
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

}