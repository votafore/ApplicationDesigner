package com.votafore.applicationdesigner;

import android.app.ActivityManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.votafore.applicationdesigner.support.CustomGLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class ActivityProject extends AppCompatActivity {

    private CustomGLSurfaceView mGLView;

    final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
    final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion < 0x20000){
            //Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

//        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
//        int newUiOptions = uiOptions;
//
//        if (Build.VERSION.SDK_INT >= 14)
//            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//
//        if (Build.VERSION.SDK_INT >= 16)
//            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//        if (Build.VERSION.SDK_INT >= 18)
//            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//
//        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Intent i = getIntent();

        int projectID = i.getExtras().getInt("ProjectID");

        mGLView = new CustomGLSurfaceView(this, projectID);
        setContentView(mGLView);


        ////////////////////////////////////
        // проверка доступных конфигураций

//        TextView textv = new TextView(this); // TextView c ScrollView для отображения результата
//        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        ScrollView scrollv = new ScrollView(this);
//        scrollv.setLayoutParams(blp);
//        scrollv.addView(textv);
//        this.addContentView(scrollv,blp);
//
//        EGL10 Egl = (EGL10) EGLContext.getEGL(); // получаем враппер Egl
//        EGLDisplay EglDisplay = Egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY); // получаем ссылку на дисплей
//        int[] version = new int[2]; // массив для получения версии EGL
//        Egl.eglInitialize(EglDisplay, version); // Инициализация EGL
//
//        int[] configSpec = {	  // шаблон конфигурации
//                EGL10.EGL_RENDERABLE_TYPE, 4, // поддержка GLES20
//                EGL10.EGL_NONE // конец
//        };
//        int[] mValue = new int[1];
//        Egl.eglChooseConfig(EglDisplay, configSpec, null, 0,mValue); // получаем колличество конфигураций подходящих под шаблон
//        int numConfigs = mValue[0];
//
//        EGLConfig[] configs = new EGLConfig[numConfigs];
//        int[] num_conf = new int[numConfigs];
//        Egl.eglChooseConfig(EglDisplay, configSpec, configs, numConfigs,mValue); // получаем массив конфигураций
//
//        String text ="EGL version "+version[0]+"."+version[1]+"\n";
//        text+= printConfigs(configs,EglDisplay,Egl)+"\n";
//
//        textv.setText(text);
    }

    private String printConfigs(EGLConfig[] conf,EGLDisplay EglDisplay,EGL10 Egl){
        String text="";
        for(int i = 0; i < conf.length; i++){
            int[] value = new int[1];
            if (conf[i] != null)
            {
                text+="==== Config №"+i+" ====\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_RED_SIZE, value);
                text+="EGL_RED_SIZE = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_GREEN_SIZE, value);
                text+="EGL_GREEN_SIZE = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_BLUE_SIZE, value);
                text+="EGL_BLUE_SIZE = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_ALPHA_SIZE, value);
                text+="EGL_ALPHA_SIZE = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_DEPTH_SIZE, value);
                text+="EGL_DEPTH_SIZE = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_SAMPLE_BUFFERS, value);
                text+="EGL_SAMPLE_BUFFERS = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL10.EGL_SAMPLES, value);
                text+="EGL_SAMPLES = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL_COVERAGE_BUFFERS_NV, value);
                text+="EGL_COVERAGE_BUFFERS_NV = "+value[0]+"\n";
                Egl.eglGetConfigAttrib(EglDisplay, conf[i], EGL_COVERAGE_SAMPLES_NV, value);
                text+="EGL_COVERAGE_SAMPLES_NV = "+value[0]+"\n\n";
            } else { break; }

        }
        return text;
    }















    @Override
    protected void onPause() {
        super.onPause();
        //mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mGLView.onResume();
    }

}
