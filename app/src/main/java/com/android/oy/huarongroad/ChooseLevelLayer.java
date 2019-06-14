package com.android.oy.huarongroad;

import android.view.MotionEvent;

import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

public class ChooseLevelLayer extends CCLayer {

    private int levelNum;
    private CCSprite[] buttons;
    private String button_path = "game/level_button";
    private CGPoint[] positions;
    private CCSprite chooseBack;
    private float width, height, scaleX, scaleY;
    private int CUR_TAG;
    private String path;

    public ChooseLevelLayer(String path, int levelNum) {
        
        this.setIsTouchEnabled(true);
        this.levelNum = levelNum;
        this.path = path;
        initPosition();
        initSprite();
    }

    private void initPosition() {
        width = CCDirector.sharedDirector().displaySize().width;
        height = CCDirector.sharedDirector().displaySize().height;
        scaleY = CCDirector.sharedDirector().displaySize().height / 1794;
        scaleX = CCDirector.sharedDirector().displaySize().width / 1080;
        int init_width = (int)width / 4;
        int init_height = (int)height / 5;
        positions = new CGPoint[levelNum];
        positions[0] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width, init_height));
        positions[1] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 2, init_height));
        positions[2] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 3, init_height));
        positions[3] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width, init_height  * 2));
        positions[4] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 2, init_height * 2));
        positions[5] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 3, init_height * 2));
        positions[6] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width, init_height * 3));
        positions[7] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 2, init_height * 3));
        positions[8] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 3, init_height * 3));
        positions[9] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width, init_height * 4));
        positions[10] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 2, init_height * 4));
        positions[11] = CCDirector.sharedDirector().convertToGL(CGPoint.make(init_width * 3, init_height * 4));
    }

    private void initSprite() {
        buttons = new CCSprite[levelNum];
        chooseBack = new CCSprite("game/chooseBack.jpg");
        chooseBack.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 2)));
        chooseBack.setScaleX(1.05f / scaleX);
        chooseBack.setScaleY(0.6f / scaleY);
        addChild(chooseBack);
        CCFadeIn fadeIn = CCFadeIn.action(0.2f);
        chooseBack.runAction(fadeIn);
        for (int i = 0; i < levelNum; i++) {
            String current_path = button_path + (i + 1) + ".png";
            buttons[i] = new CCSprite(current_path);
            buttons[i].setPosition(positions[i]);
            buttons[i].setScaleX(0.1f / scaleX);
            buttons[i].setScaleY(0.04f / scaleY);
            addChild(buttons[i], 1, i);
            fadeIn = CCFadeIn.action(0.2f);
            buttons[i].runAction(fadeIn);
        }
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = 0; i < levelNum; i++) {
            CGRect rect = buttons[i].getBoundingBox();
            if (CGRect.containsPoint(rect, cgPoint)) {
                CUR_TAG = i;
                return super.ccTouchesBegan(event);
            }
        }
        return super.ccTouchesBegan(event);
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = 0; i < levelNum; i++) {
            CGRect rect = buttons[i].getBoundingBox();
            if (CGRect.containsPoint(rect, cgPoint)) {
                if (CUR_TAG == i) {
                    changeToGame(i);
                }
                return super.ccTouchesBegan(event);
            }
        }
        return super.ccTouchesEnded(event);
    }

    private void changeToGame(int i) {
        CCScene scene = CCScene.node();
        scene.addChild(new WelcomeLayer(path, i, levelNum));
        this.removeSelf();
        CCDirector.sharedDirector().runWithScene(scene);
    }
}
