package com.android.oy.huarongroad;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private CCDirector director = CCDirector.sharedDirector();
    private int level_num = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        int i = ContextCompat.checkSelfPermission(this, android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int j = ContextCompat.checkSelfPermission(this, android
                .Manifest.permission.READ_EXTERNAL_STORAGE);
        if (i != PackageManager.PERMISSION_GRANTED || j != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else {
            createFiles();
            CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
            setContentView(surfaceView);
            director = CCDirector.sharedDirector();
            director.attachInView(surfaceView);
            director.setScreenSize(480, 320);
            director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
            director.setDisplayFPS(false);
            CCScene scene = CCScene.node();
            //scene.addChild(new WelcomeLayer(this.getFilesDir().toPath().toString(), 0));
            scene.addChild(new com.android.oy.huarongroad.StartLayer(this.getFilesDir().toPath().toString(), level_num));
            director.runWithScene(scene);
        }

    }

    @Override
    protected void onResume() {
        director.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        director.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        director.end();
        super.onDestroy();
    }

    private void createFiles(){
        File fileRoot = this.getFilesDir();
        System.out.println(fileRoot.toPath());
        File fileParent = new File(fileRoot, "data");
        File file = new File(fileParent, "level.dat");
        if (!fileRoot.exists()) {
            fileRoot.mkdir();
        }
        if (!fileParent.exists()) {
            boolean mkdirs = fileParent.mkdir();
            System.out.println(mkdirs);
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                System.out.println("parent " + e);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println(CCDirector.sharedDirector().getRunningScene().getChildren().get(0).getClass());
            if (CCDirector.sharedDirector().getRunningScene().getChildren().get(0).getClass() == StartLayer.class) {
                return false;
            }
            if (CCDirector.sharedDirector().getRunningScene().getChildren().get(0).getClass() == ChooseLevelLayer.class) {
                CCDirector.sharedDirector().getRunningScene().removeSelf();
                CCScene scene = CCScene.node();
                scene.addChild(new StartLayer(this.getFilesDir().toPath().toString(), level_num));
                CCDirector.sharedDirector().runWithScene(scene);
                return false;
            }
            if (CCDirector.sharedDirector().getRunningScene().getChildren().get(0).getClass() == WelcomeLayer.class) {
                CCDirector.sharedDirector().getRunningScene().removeSelf();
                CCScene scene = CCScene.node();
                scene.addChild(new ChooseLevelLayer(this.getFilesDir().toPath().toString(), level_num));
                CCDirector.sharedDirector().runWithScene(scene);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                               @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFiles();
                    CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
                    setContentView(surfaceView);
                    director = CCDirector.sharedDirector();
                    director.attachInView(surfaceView);
                    director.setScreenSize(480, 320);
                    director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
                    director.setDisplayFPS(false);
                    CCScene scene = CCScene.node();
                    scene.addChild(new com.android.oy.huarongroad.StartLayer(this.getFilesDir().toPath().toString(), level_num));
                    director.runWithScene(scene);
                }
        }
    }

}
