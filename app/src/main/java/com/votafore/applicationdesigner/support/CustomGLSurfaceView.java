package com.votafore.applicationdesigner.support;


import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.controller.ProjectManager;
import com.votafore.applicationdesigner.glsupport.Config3D;
import com.votafore.applicationdesigner.model.Block;

public class CustomGLSurfaceView extends GLSurfaceView{

    String TAG = "MyEvent";


    Context mContext;

    private ProjectManager mManager;

    private OpenGLRenderer mRenderer;

    public CustomGLSurfaceView(Context ctx, int projectID){
        super(ctx);
        mContext = ctx;

        // создаем "управляющего" для работы с проектом
        mManager = new ProjectManager(mContext, this, projectID);

        // настрйока вьюхи
        setEGLContextClientVersion(2);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setEGLConfigChooser(new Config3D());

        mRenderer = new OpenGLRenderer(mContext);

        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);

//        queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                mRenderer.onTouchEvent(event);
//            }
//        });

        mRenderer.onTouchEvent(event);

        boolean result = true;

        if(event.getAction() == MotionEvent.ACTION_UP)
            result = false;

        requestRender();

        return result;
    }

    public void setRootBlock(final Block block) {

//        queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                mRenderer.setBlocks(block);
//            }
//        });

        mRenderer.setBlocks(block);

        requestRender();

        Log.d(TAG, "block sent");
    }
}
