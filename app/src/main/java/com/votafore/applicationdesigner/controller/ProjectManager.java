package com.votafore.applicationdesigner.controller;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.model.BlockScene;
import com.votafore.applicationdesigner.model.DataBase;
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

    Context mContext;

    OpenGLRenderer mRenderer;

    Block mRootBlock;

    private String TAG = "MyEvent";

    public ProjectManager(Context ctx, int id){
        mContext = ctx;
        mRenderer = new OpenGLRenderer(mContext);

        mRootBlock = new BlockScene();
        mRootBlock.setProjectID(id);

        //ProjectLoader loader = new ProjectLoader(mContext, this);
        //loader.execute(mRootBlock);
    }


    /**
     * обработка окончания загрузки данных проекта
     */
    public void onLoadComplete(Block block){

        mRootBlock = block;

        // какие-нибудь дополнительные вычисления данных проекта
        // ...

        // устанавливаем дерево объектов в рендерер для отбражения
        Runnable setBlock = new Runnable() {
            @Override
            public void run() {
                mRenderer.setBlocks(mRootBlock);
            }
        };
        setBlock.run();

    }


    /**
     * УПРАВЛЕНИЕ RENDERER ом
     * */
    public OpenGLRenderer getRenderer(){
        return mRenderer;
    }


    // ЗАГРУЗКА ДАННЫХ ПРОЕКТА
    private class ProjectLoader extends AsyncTask<Block, Integer, Block>{

        Context mContext;

        DataBase db;

        ProjectManager mManager;

        private int count;
        private int countAll;

        public ProjectLoader(Context ctx, ProjectManager manager) {
            super();

            mContext    = ctx;
            mManager    = manager;

            db          = new DataBase(mContext);

            count = 0;
        }

        @Override
        protected Block doInBackground(Block... params) {

            countAll = db.getAllDataCount(params[0].getProjectID());

            loadBranch(params[0]);

            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Block block) {

            if(isCancelled())
                return;

            mManager.onLoadComplete(block);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "загружено " + String.valueOf(values) + "%");
        }





        private void loadBranch(Block curBlock){

            List<Block> childs = curBlock.getChilds();

            for(Block item: childs){
                db.populateBlock(item);

                // объект был загружен
                // увеличиваем счетчик загруженных объектов
                // и обновляем прогресс
                count++;

                int progress = (countAll / count) * 100;
                publishProgress(progress);

                loadBranch(item);
            }
        }

    }
}
