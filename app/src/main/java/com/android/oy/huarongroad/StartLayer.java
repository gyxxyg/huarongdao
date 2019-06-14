package com.android.oy.huarongroad;

import android.view.MotionEvent;

import org.cocos2d.actions.ease.CCEaseSineOut;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

public class StartLayer extends CCLayer {

    private int levelNum, CUR_TAG;
    private float scaleY, scaleX, width, height;
    private boolean banButton = false;
    private String path;
    CCSprite initBack, onBack, onExit, onContinue, onConfirm;
    CCSprite[] buttons;

    public StartLayer(String path, int levelNum) {
        this.setIsTouchEnabled(true);
        this.setIsKeyEnabled(true);
        this.path = path;
        this.levelNum = levelNum;
        initSprite();
    }

    private void initSprite() {
        scaleY = CCDirector.sharedDirector().displaySize().height / 1794;
        scaleX = CCDirector.sharedDirector().displaySize().width / 1080;
        width = CCDirector.sharedDirector().displaySize().width;
        height = CCDirector.sharedDirector().displaySize().height;

        initBack = new CCSprite("game/initBack.jpg");
        initBack.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 2)));
        initBack.setScaleX(1.05f / scaleX);
        initBack.setScaleY(0.60f / scaleY);
        addChild(initBack);

        buttons = new CCSprite[2];
        buttons[0] = new CCSprite("game/initButton1.png");
        buttons[0].setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height)));
        CGPoint finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 4 * 3));
        buttons[0].setScaleX(1.0f / scaleX);
        buttons[0].setScaleY(0.4f / scaleY);
        CCIntervalAction actionTo = CCMoveTo.action((float)0.7, finishPoint);
        CCEaseSineOut easeSineOut = CCEaseSineOut.action(actionTo);
        buttons[0].runAction(easeSineOut);
        addChild(buttons[0]);

        buttons[1] = new CCSprite("game/initButton2.png");
        buttons[1].setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 4 * 5)));
        finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 8 * 7));
        buttons[1].setScaleX(1.0f / scaleX);
        buttons[1].setScaleY(0.4f / scaleY);
        actionTo = CCMoveTo.action((float)0.7, finishPoint);
        easeSineOut = CCEaseSineOut.action(actionTo);
        buttons[1].runAction(easeSineOut);
        addChild(buttons[1]);
    }

    private void changeToChoose() {
        CCScene scene = CCScene.node();
        scene.addChild(new ChooseLevelLayer(path, levelNum));
        this.removeSelf();
        CCDirector.sharedDirector().runWithScene(scene);
    }

    public void checkExit() {
        banButton = true;
        System.out.println("on key down");
        onBack = new CCSprite("game/passBack.png");
        onBack.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(500, 500)));
        this.addChild(onBack, 1);

        onConfirm = new CCSprite("game/onconfirm.png");
        onConfirm.setScaleX(1.5f / scaleX);
        onConfirm.setScaleY(0.5f/ scaleY);
        CGPoint startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, 0));
        CGPoint finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, 654 * scaleY));
        onConfirm.setPosition(startPoint);
        this.addChild(onConfirm, 2);
        CCIntervalAction actionTo = CCMoveTo.action((float)0.5, finishPoint);
        CCEaseSineOut easeSineOut = CCEaseSineOut.action(actionTo);
        onConfirm.runAction(easeSineOut);

        onExit = new CCSprite("game/onexit.png");
        onExit.setScaleX(1.5f / scaleX);
        onExit.setScaleY(0.5f/ scaleY);
        startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 4 * 1, 1775 * scaleY));
        finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 4 * 1, 1000 * scaleY));
        onExit.setPosition(startPoint);
        this.addChild(onExit, 2);
        actionTo = CCMoveTo.action((float)0.5, finishPoint);
        easeSineOut = CCEaseSineOut.action(actionTo);
        onExit.runAction(easeSineOut);

        onContinue = new CCSprite("game/oncontinue.png");
        onContinue.setScaleX(1.5f / scaleX);
        onContinue.setScaleY(0.5f/ scaleY);
        startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 4 * 3, 1775 * scaleY));
        finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 4 * 3, 1000 * scaleY));
        onContinue.setPosition(startPoint);
        this.addChild(onContinue, 2);
        actionTo = CCMoveTo.action((float)0.5, finishPoint);
        easeSineOut = CCEaseSineOut.action(actionTo);
        onContinue.runAction(easeSineOut);
    }

    private void checkExitButton(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        CGRect exitRect = onExit.getBoundingBox();
        CGRect continueRect = onContinue.getBoundingBox();
        System.out.println(continueRect);
        System.out.println(exitRect);
        if (CGRect.containsPoint(continueRect, cgPoint)) {
            onContinue.removeSelf();
            onExit.removeSelf();
            onConfirm.removeSelf();
            onBack.removeSelf();
            banButton = false;
        }
        else if (CGRect.containsPoint(exitRect, cgPoint)) {
            this.removeSelf();
            CCDirector.sharedDirector().end();
            CCDirector.sharedDirector().getActivity().finish();
        }
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        if (banButton) {
            return super.ccTouchesBegan(event);
        }
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = 0; i < 2; i++) {
            CGRect rect = buttons[i].getBoundingBox();
            if (CGRect.containsPoint(rect, cgPoint)) {
                if (CUR_TAG == i) {
                    if (i == 0) {
                        changeToChoose();
                    }
                    else {
                        checkExit();
                    }
                }
                return super.ccTouchesBegan(event);
            }
        }
        return super.ccTouchesEnded(event);
    }



    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        if (banButton) {
            checkExitButton(event);
            return super.ccTouchesBegan(event);
        }
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = 0; i < 2; i++) {
            CGRect rect = buttons[i].getBoundingBox();
            if (CGRect.containsPoint(rect, cgPoint)) {
                CUR_TAG = i;
                return super.ccTouchesBegan(event);
            }
        }
        return super.ccTouchesBegan(event);
    }
}
