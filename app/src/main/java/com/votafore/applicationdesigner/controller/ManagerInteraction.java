package com.votafore.applicationdesigner.controller;

import com.votafore.applicationdesigner.model.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * класс, который позволит хранить данные о взаимодействии всех объектов
 * и обрабатывать их (данные)
 * Эта информация хранится в виде списка
 * Надо еще разработать функционал для его обработки
 */
public class ManagerInteraction {

    List<Connection> mConnections;

    public ManagerInteraction(){

        mConnections = new ArrayList<>();
    }

    public void createConnection(Block block1, Block block2){

        Connection conn = new Connection(block1, block2);

        mConnections.add(conn);
    }











    /**
     * класс хранит информацию о взаимощействии между двумя блоками
     * пока что здесь хранятся ссылки на связанные объекты
     * позже надо будет хранить информацию о самом взаимодействии:
     * - какие шаги выполняются
     * - как
     * - направление вызовов
     */
    public class Connection{

        Block mBlock1;
        Block mBlock2;

        public Connection(Block block1, Block block2){

            mBlock1 = block1;
            mBlock2 = block2;
        }
    }
}
