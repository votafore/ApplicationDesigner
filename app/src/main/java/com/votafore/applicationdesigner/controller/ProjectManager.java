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

            countAll = 5;
            float[] translation;

            Block rootBlock = new Block();

            ++count;

            rootBlock.setSize(0.0f, 0.0f);
            rootBlock.setOrientation(Block.Plane.Y);
            rootBlock.initVertices();

            publishProgress((count / countAll) * 100);


            Block UI = new Block();
            rootBlock.addChild(UI);

            ++count;

            translation = UI.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0.0f, 0.3f, 0.5f);
            UI.setTranslateMatrix(translation);

            UI.setColor(new float[]{0.0f, 1.0f, 0.0f, 0.6f});
            UI.setSize(1.0f, 0.6f);
            UI.setOrientation(Block.Plane.Y);
            UI.initVertices();

            publishProgress((count / countAll) * 100);


            Block functionality = new Block();
            rootBlock.addChild(functionality);

            ++count;

            functionality.setSize(0.0f, 0.0f);
            functionality.setOrientation(Block.Plane.Y);
            functionality.initVertices();

            publishProgress((count / countAll) * 100);






            Block mainActivity = new Block();
            UI.addChild(mainActivity);

            ++count;

            mainActivity.setColor(new float[]{0.0f, 0.0f, 1.0f, 0.5f});
            mainActivity.setSize(0.4f, 0.6f);

            translation = mainActivity.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0.2f, mainActivity.getHeight()/2, 0.0f);
            mainActivity.setTranslateMatrix(translation);

            mainActivity.setOrientation(Block.Plane.X);
            mainActivity.initVertices();

            publishProgress((count / countAll) * 100);





            Block manager = new Block();
            functionality.addChild(manager);

            ++count;

            manager.setColor(new float[]{0.5f, 0.0f, 1.0f, 0.5f});
            manager.setSize(0.6f, 0.4f);

            translation = manager.getTranslateMatrix();
            Matrix.translateM(translation, 0, (float)(manager.getWidth()+0.1), 0.0f, -0.2f);
            manager.setTranslateMatrix(translation);

            manager.setOrientation(Block.Plane.Y);
            manager.initVertices();

            publishProgress((count / countAll) * 100);


            return rootBlock;
        }

        @Override
        protected void onPostExecute(Block block) {

            if(isCancelled())
                return;

            Log.d(TAG, "Load complete");

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
