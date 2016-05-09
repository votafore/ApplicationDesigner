package com.votafore.applicationdesigner.controller;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.model.BlockActivity;
import com.votafore.applicationdesigner.model.BlockScene;
import com.votafore.applicationdesigner.model.DataBase;
import com.votafore.applicationdesigner.support.CustomGLSurfaceView;
import com.votafore.applicationdesigner.support.OpenGLRenderer;

import java.util.List;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;

/**
 * класс для организации работы с данными проекта:
 * - чтение данных из базы
 * - построение (сбор, структурирование) информации
 * - управление отображением
 */
public class ProjectManager {

    Context             mContext;
    CustomGLSurfaceView mGLview;


    public ProjectManager(Context ctx, CustomGLSurfaceView GLview, int id){

        mContext    = ctx;
        mGLview     = GLview;

        ProjectLoader loader = new ProjectLoader(mContext);
        loader.execute(id);
    }







    // ЗАГРУЗКА ДАННЫХ ПРОЕКТА
    private String TAG = "MyEvent";

    private class ProjectLoader extends AsyncTask<Integer, Integer, Block>{

        Context mContext;

        DataBase db;

        private int count;
        private int countAll;

        public ProjectLoader(Context ctx) {
            super();

            mContext    = ctx;
            count       = 0;

            //db          = new DataBase(mContext);
        }

        @Override
        protected Block doInBackground(Integer... params) {

            countAll = 1;

            Block testBlock = new BlockActivity();

            testBlock.setSize(0.4f, 0.3f);
            testBlock.setOrientation(0,1,1);
            testBlock.initVertices();

            count++;
            publishProgress((count / countAll) * 100);

            return testBlock;
        }

        @Override
        protected void onPostExecute(Block block) {

            if(isCancelled())
                return;

            mGLview.setRootBlock(block);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "загружено " + String.valueOf(values[0]) + "%");
        }





        private void loadBranch(Block curBlock){

            List<Block> childs = curBlock.getChilds();

            for(Block item: childs){
                db.populateBlock(item);

                // объект был загружен
                // увеличиваем счетчик загруженных объектов
                // и обновляем прогресс
                count++;

                int progress = (count / countAll) * 100;
                publishProgress(progress);

                loadBranch(item);
            }
        }

    }
}
