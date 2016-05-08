package com.votafore.applicationdesigner.support;


import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.controller.ProjectManager;

public class CustomGLSurfaceView extends GLSurfaceView{

    Context mContext;

    private ProjectManager mManager;

    private OpenGLRenderer mRenderer;

    public CustomGLSurfaceView(Context ctx, int projectID){
        super(ctx);
        mContext = ctx;

        // создаем "управляющего" для работы с проектом
        mManager = new ProjectManager(mContext, projectID);

        // настрйока вьюхи
        setEGLContextClientVersion(2);
//        getHolder().setFormat(PixelFormat.RGBA_8888);
//        try {
//            setEGLConfigChooser(new Config3D());
//        }catch (IllegalArgumentException e){
//            // просто так
//        }

        mRenderer = mManager.getRenderer();

        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        super.onTouchEvent(event);

        queueEvent(new Runnable() {
            @Override
            public void run() {
                mRenderer.onTouchEvent(event);
            }
        });

        return true;
    }
}
