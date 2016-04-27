package com.votafore.applicationdesigner.model;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

/**
 * объект этого класса всего лишь содержит сцену... на ней ничего нет
 * только лишь в процессе работы сюда добавляются подчиненные объекты
 * ...
 * хотя со временем это может поменяться.
 * Например сцена имеет какие-нибудь заранее определенные элементы
 */

public class BlockScene extends Block {

    @Override
    public float[] getVertices() {

        float hight = 0.4f;

        float right = 1.0f;
        float left = -1.0f;

        float front = 0.5f;
        float back = -0.5f;

        vertices = new float[]{
                // X, Y, Z
                left, hight, back,
                right, hight, back,
                left, hight, front,
                right, hight, front
        };

        return vertices;
    }

    @Override
    public float[] getColor() {
        return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
    }

    @Override
    public int getMode() {
        return GL_TRIANGLE_STRIP;
    }

    @Override
    public int getVertexCount() {
        return vertices.length / POSITION_COUNT;
    }
}
