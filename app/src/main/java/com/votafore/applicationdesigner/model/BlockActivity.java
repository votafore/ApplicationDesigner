package com.votafore.applicationdesigner.model;


public class BlockActivity extends Block {

    public BlockActivity(){
        super();
    }

    @Override
    public void initVertices() {

        float[] color = new float[]{0.0f, 1.0f, 0.0f, 0.5f};

        // x
        float right = 0.0f;
        float left = 0.5f;

        // y
        float bottom = 0.4f;
        float top = 0.7f;

        //z
        float back = 0.0f;

        mVertices = new float[]{
                right, top, back,       color[0], color[1], color[2], color[3],
                left, top, back,        color[0], color[1], color[2], color[3],
                right, bottom, back,    color[0], color[1], color[2], color[3],
                left, bottom, back,     color[0], color[1], color[2], color[3]
        };
    }
}