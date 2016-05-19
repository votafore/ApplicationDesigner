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

    ManagerInteraction mManager;

    public ProjectManager(Context ctx, CustomGLSurfaceView GLview, int id){

        mContext    = ctx;
        mGLview     = GLview;

        mManager = new ManagerInteraction();

        ProjectLoader loader = new ProjectLoader(mContext, mManager);
        loader.execute(id);
    }






    // ЗАГРУЗКА ДАННЫХ ПРОЕКТА
    private String TAG = "MyEvent";

    private class ProjectLoader extends AsyncTask<Integer, Integer, Block>{

        Context mContext;

        DataBase db;

        private int count;
        private int countAll;

        ManagerInteraction mInterManager;

        public ProjectLoader(Context ctx, ManagerInteraction interManager) {
            super();

            mContext    = ctx;
            count       = 0;

            mInterManager = interManager;

            //db          = new DataBase(mContext);
        }

        @Override
        protected Block doInBackground(Integer... params) {

            countAll = 5;
            float[] translation;

            /**
             * подготовка информации о "подчинении", вершинах, перемещениях и т.п. и т.д.
             * будет происходить в следующей очередности:
             * - создаются объекты и расставляются по местам в дереве
             * - потом задаем необходимые настройки (размеры, цвет, размещение, плоскость и т.п)
             *
             * - по мере их расставления в дереве рассчитываются их размеры.
             *      Это теоретические т.е. те, которые предположительно будут установлены для объектов.
             *      Если вдруг окажется что один из подчиненных объектов при уменьшении окажется меньше
             *      своего минимального (допустимого) размера, то размер будет установлен минимальным
             *      а размеры родителей увеличатся что бы все поместилось красиво.
             *
             * - запускается процедура расчета матриц и вершин через вызов метода initVertices корневого объекта
             *      Он рекурсивно вызовет этот же метод подчиненных объектов и расчитает данные во всех блоках
             */

            // ########################### СОЗДАНИЕ ОБЪЕКТОВ ##############
            float width = 0.9f;
            float height = 0.15f;

            Block rootBlock = new Block();
            rootBlock.setColor(new float[]{1.0f, 1.0f, 0.0f, 0});
            rootBlock.setSize(0,0);
            rootBlock.setRelativeWidth(0);
            rootBlock.setRelativeHeight(0);

            Block UI = new Block();
            UI.setColor(new float[]{0.0f, 1.0f, 0.0f, 0.6f});
            UI.setSize(2, 0.6f);

            Block mainActivity = new Block();
            mainActivity.setColor(new float[]{0.0f, 0.0f, 1.0f, 0.5f});
            mainActivity.setSize(0.3f, 0.5f);

            Block recyclView = new Block();
            recyclView.setColor(new float[]{0.4f,0.2f,0.7f,1.0f});
            recyclView.setSize(0.26f, 0.4f);

            Block functionality = new Block();
            functionality.setColor(new float[]{1.0f, 1.0f, 0.0f, 0});
            functionality.setSize(0.0f, 0.0f);

            Block manager = new Block();
            manager.setColor(new float[]{0.5f, 0.0f, 1.0f, 0.5f});
            manager.setSize(0.6f, 0.4f);

            Block dataBase = new Block();
            dataBase.setColor(new float[]{0.5f, 1.0f, 1.0f, 1.0f});
            dataBase.setSize(0.6f, 0.4f);

            // ########################### РАСПОЛОЖЕНИЕ В ДЕРЕВЕ ##############
            rootBlock.addChild(UI);
                UI.addChild(mainActivity);
                    mainActivity.addChild(recyclView);

            rootBlock.addChild(functionality);
                functionality.addChild(manager);
                functionality.addChild(dataBase);



            // ########################### НАСТРОЙКА ОБЪЕКТОВ ##############

            //rootBlock.setOrientation(Block.Plane.Y);
            //mainActivity.setOrientation(Block.Plane.X);




            translation = UI.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0, 0.2f, -0.4f);
            UI.setTranslateMatrix(translation);

            translation = mainActivity.getTranslateMatrix();
            Matrix.translateM(translation, 0, -0.2f, 0, 0);
            Matrix.translateM(translation, 0, 0, mainActivity.getHeight()/2, 0);
            Matrix.rotateM(translation, 0, 90, 1, 0, 0);
            mainActivity.setTranslateMatrix(translation);

            translation = recyclView.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0, 0.05f, 0);
            recyclView.setTranslateMatrix(translation);

            translation = manager.getTranslateMatrix();
            Matrix.translateM(translation, 0, -(float)(manager.getWidth()+0.1), 0.0f, 0.2f);
            manager.setTranslateMatrix(translation);

            translation = dataBase.getTranslateMatrix();
            Matrix.translateM(translation, 0, -(float)(dataBase.getWidth()+0.1), 0.0f, 0.7f);
            dataBase.setTranslateMatrix(translation);

            //BlockPlacementVertical.setPlacement(rootBlock);




            rootBlock.initVertices();

//
//            // установим связи между блоками
//            mInterManager.createConnection(recyclView, manager);
//            mInterManager.createConnection(manager, dataBase);



            return rootBlock;
        }

        @Override
        protected void onPostExecute(Block block)    {

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
