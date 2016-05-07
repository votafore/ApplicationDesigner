package com.votafore.applicationdesigner.model;


/**
 * объект этого класса всего лишь содержит сцену... на ней ничего нет
 * только лишь в процессе работы сюда добавляются подчиненные объекты
 * ...
 * хотя со временем это может поменяться.
 * Например сцена имеет какие-нибудь заранее определенные элементы
 */

public class BlockScene extends Block {

    public BlockScene(){
        super();
    }

    @Override
    public void initVertices() {

        float[] color = new float[]{0.0f, 0.0f, 1.0f, 1.0f};

        float hight = 0.4f;

        float right = 1.0f;
        float left = -1.0f;

        float front = 0.5f;
        float back = -0.5f;

        mVertices = new float[]{
                // X, Y, Z          цвета вершин
                left, hight, back,      color[0], color[1], color[2], color[3],
                right, hight, back,     color[0], color[1], color[2], color[3],
                left, hight, front,     color[0], color[1], color[2], color[3],
                right, hight, front,    color[0], color[1], color[2], color[3]
        };
    }
}
