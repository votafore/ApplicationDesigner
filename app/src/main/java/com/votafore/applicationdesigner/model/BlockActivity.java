package com.votafore.applicationdesigner.model;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

public class BlockActivity extends Block {

    public BlockActivity(){

        // x
        float right = 0.0f;
        float left = 0.5f;

        // y
        float bottom = 0.4f;
        float top = 0.7f;

        //z
        float back = 0.0f;

        vertices = new float[]{
                right, top, back,
                left, top, back,
                right, bottom, back,
                left, bottom, back
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
        return vertices.length / POSITION_COUNT;
    }
}