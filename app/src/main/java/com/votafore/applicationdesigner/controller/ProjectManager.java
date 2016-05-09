package com.votafore.applicationdesigner.controller;

import android.content.Context;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.model.DataBase;
import com.votafore.applicationdesigner.support.CustomGLSurfaceView;

import java.util.List;

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

            countAll = 2;

            Block testBlock = new Block();

            count++;

            float[] matr = testBlock.getTranslateMatrix();
            Matrix.translateM(matr, 0, 0.2f, 0.0f, 0.0f);
            testBlock.setTranslateMatrix(matr);

            testBlock.setColor(new float[]{0.0f, 1.0f, 0.0f, 0.0f});
            testBlock.setSize(1.0f, 0.5f);
            testBlock.setOrientation(1,0,1);
            testBlock.initVertices();

            publishProgress((count / countAll) * 100);

            Block testChild = new Block();

            count++;

            testBlock.addChild(testChild);

            testChild.setTranslateMatrix(matr);
            testChild.setColor(new float[]{0.0f, 0.0f, 1.0f, 0.0f});
            testChild.setSize(0.4f, 0.4f);
            testChild.setOrientation(1,0,1);
            testChild.initVertices();

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
