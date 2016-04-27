package com.votafore.applicationdesigner;

import android.app.ActivityManager;
import android.content.Context;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.votafore.applicationdesigner.support.CustomGLSurfaceView;

public class ActivityProject extends AppCompatActivity {

    private CustomGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        if(info.reqGlEsVersion < 0x20000){
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        if (Build.VERSION.SDK_INT >= 14)
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        if (Build.VERSION.SDK_INT >= 16)
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= 18)
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Intent i = getIntent();

        int projectID = i.getExtras().getInt("ProjectID");

        mGLView = new CustomGLSurfaceView(this, projectID);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

}
