package com.votafore.applicationdesigner.support;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.votafore.applicationdesigner.controller.ProjectManager;

public class CustomGLSurfaceView extends GLSurfaceView{

    Context mContext;

    private ProjectManager mManager;

    public CustomGLSurfaceView(Context ctx, int projectID){
        super(ctx);
        mContext = ctx;

        // создаем "управляющего" для работы с проектом
        mManager = new ProjectManager(mContext, projectID);

        // настрока вьюхи
        setEGLContextClientVersion(2);
        setRenderer(mManager.getRenderer());
        //setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mManager.onTouchEvent(event);
        return true;
    }
}
