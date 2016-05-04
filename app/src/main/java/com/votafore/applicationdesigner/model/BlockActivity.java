package com.votafore.applicationdesigner.model;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

public class BlockActivity extends Block {

    public BlockActivity(){

        float[] color = new float[]{0.0f, 1.0f, 0.0f, 0.5f};

        // x
        float right = 0.0f;
        float left = 0.5f;

        // y
        float bottom = 0.4f;
        float top = 0.7f;

        //z
        float back = 0.0f;

        vertices = new float[]{
                right, top, back,       color[0], color[1], color[2], color[3],
                left, top, back,        color[0], color[1], color[2], color[3],
                right, bottom, back,    color[0], color[1], color[2], color[3],
                left, bottom, back,     color[0], color[1], color[2], color[3]
        };
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public float[] getColor() {
        return new float[]{0.0f, 1.0f, 1.0f, 0.3f};
    }

    @Override
    public int getMode() {
        return GL_TRIANGLE_STRIP;
    }

    @Override
    public int getVertexCount() {
        return vertices.length / (POSITION_COUNT+COLOR_COUNT);
    }
}