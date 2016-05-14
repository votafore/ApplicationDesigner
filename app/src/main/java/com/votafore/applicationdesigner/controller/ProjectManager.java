package com.votafore.applicationdesigner.controller;

import android.content.Context;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import com.votafore.applicationdesigner.model.Block;
import com.votafore.applicationdesigner.model.DataBase;
import com.votafore.applicationdesigner.support.BlockPlacementHorizontal;
import com.votafore.applicationdesigner.support.BlockPlacementVertical;
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
            Block rootBlock = new Block();
            rootBlock.setColor(new float[]{1.0f, 1.0f, 0.0f, 0.6f});
            rootBlock.setSize(1.0f, 0.6f);

            Block b1 = new Block();
            b1.setColor(new float[]{1, 0, 0, 0.6f});
            b1.setRelativeHeight(0.9f);
            b1.setRelativeWidth(0.2f);

            Block b2 = new Block();
            b2.setColor(new float[]{0, 0, 1, 0.6f});
            b2.setRelativeHeight(0.9f);
            b2.setRelativeWidth(0.2f);

            Block b3 = new Block();
            b3.setColor(new float[]{0, 0.2f, 1, 0.6f});
            b3.setRelativeHeight(0.9f);
            b3.setRelativeWidth(0.2f);


            // ########################### РАСПОЛОЖЕНИЕ В ДЕРЕВЕ ##############
            rootBlock.addChild(b1);
            rootBlock.addChild(b2);
            rootBlock.addChild(b3);



            // ########################### НАСТРОЙКА ОБЪЕКТОВ ##############

            rootBlock.setOrientation(Block.Plane.Y);

            translation = b1.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0, 0.01f, 0);
            b1.setTranslateMatrix(translation);

            translation = b2.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0.0f, 0.01f, 0);
            b2.setTranslateMatrix(translation);

            translation = b3.getTranslateMatrix();
            Matrix.translateM(translation, 0, 0.0f, 0.01f, 0);
            b3.setTranslateMatrix(translation);


            BlockPlacementHorizontal.setPlacement(rootBlock);




            rootBlock.initVertices();

            //publishProgress((count / countAll) * 100);


//            Block UI = new Block();
//            rootBlock.addChild(UI);
//
//            ++count;
//
//            translation = UI.getTranslateMatrix();
//            Matrix.translateM(translation, 0, 0.0f, 0.3f, 0.5f);
//            UI.setTranslateMatrix(translation);
//
//            UI.setColor(new float[]{0.0f, 1.0f, 0.0f, 0.6f});
//            UI.setSize(1.0f, 0.6f);
//            UI.setOrientation(Block.Plane.Y);
//            UI.initVertices();
//
//            publishProgress((count / countAll) * 100);
//
//
//            Block functionality = new Block();
//            rootBlock.addChild(functionality);
//
//            ++count;
//
//            functionality.setSize(0.0f, 0.0f);
//            functionality.setOrientation(Block.Plane.Y);
//            functionality.initVertices();
//
//            publishProgress((count / countAll) * 100);
//
//
//
//
//
//            Block mainActivity = new Block();
//            UI.addChild(mainActivity);
//
//            ++count;
//
//            mainActivity.setColor(new float[]{0.0f, 0.0f, 1.0f, 0.5f});
//            mainActivity.setSize(0.4f, 0.6f);
//
//            translation = mainActivity.getTranslateMatrix();
//            Matrix.translateM(translation, 0, 0.2f, mainActivity.getHeight()/2, 0.0f);
//            mainActivity.setTranslateMatrix(translation);
//
//            mainActivity.setOrientation(Block.Plane.X);
//            mainActivity.initVertices();
//
//            publishProgress((count / countAll) * 100);
//
//
//
//
//            Block recyclView = new Block();
//            mainActivity.addChild(recyclView);
//
//            recyclView.setColor(new float[]{0.4f,0.2f,0.7f,1.0f});
//            recyclView.setSize(0.3f, 0.5f);
//
//            translation = recyclView.getTranslateMatrix();
//            Matrix.translateM(translation, 0, 0.0f, 0.0f, -0.05f);
//            recyclView.setTranslateMatrix(translation);
//
//            recyclView.initVertices();
//
//
//
//            Block manager = new Block();
//            functionality.addChild(manager);
//
//            ++count;
//
//            manager.setColor(new float[]{0.5f, 0.0f, 1.0f, 0.5f});
//            manager.setSize(0.6f, 0.4f);
//
//            translation = manager.getTranslateMatrix();
//            Matrix.translateM(translation, 0, (float)(manager.getWidth()+0.1), 0.0f, -0.2f);
//            manager.setTranslateMatrix(translation);
//
//            manager.setOrientation(Block.Plane.Y);
//            manager.initVertices();
//
//            publishProgress((count / countAll) * 100);
//
//
//
//
//            Block dataBase = new Block();
//            functionality.addChild(dataBase);
//
//            dataBase.setColor(new float[]{0.5f, 1.0f, 1.0f, 1.0f});
//            dataBase.setSize(0.6f, 0.4f);
//
//            translation = dataBase.getTranslateMatrix();
//            Matrix.translateM(translation, 0, (float)(dataBase.getWidth()+0.1), 0.0f, -0.7f);
//            dataBase.setTranslateMatrix(translation);
//
//            dataBase.setOrientation(Block.Plane.Y);
//            dataBase.initVertices();
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
