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
 * А так же:
 * - обработка событий ввода пользователя
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
        mRootBlock.setID(id);

//        ProjectLoader loader = new ProjectLoader(mContext, mRootBlock);
//        loader.execute();
    }




    /**
     * УПРАВЛЕНИЕ RENDERER ом
     * */

    public Renderer getRenderer(){
        return mRenderer;
    }


    /**
     * ОБРАБОТКА СОБЫТИЙ ВВОДА
     * @param event
     */

    public void onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "action move");
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "action down");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "action up");
        }
    }


    // ЗАГРУЗКА ДАННЫХ ПРОЕКТА
    private class ProjectLoader extends AsyncTask<Void, Integer, Void>{

        private final Block mRoot;

        Context mContext;

        DataBase db;

        private int count;
        private int countAll;

        public ProjectLoader(Context ctx, Block rootBlock) {
            super();

            mContext    = ctx;
            mRoot       = rootBlock;
            db          = new DataBase(mContext);

            count = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {

            countAll = db.getAllDataCount(mRoot.getID());

            loadBranch(mRoot);

            db.close();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "загружено " + String.valueOf(values) + "%");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
