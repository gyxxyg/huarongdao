package com.android.oy.huarongroad;

import android.view.MotionEvent;

import org.cocos2d.actions.ease.CCEaseSineOut;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class WelcomeLayer extends CCLayer {

    private int TAG_CUR;
    private int MOVE_GAP = 0;
    private CGPoint CUR_POINT, LEAVE_POINT;
    private int slide_num = 10;
    private CCSprite[] slides;
    private CGRect[] map_positions;
    private CGPoint[] map_points;
    private CGPoint[] map_sets;
    private int[] current_position;
    private CGRect map_bounding;
    private int init_height, init_width;
    private int character;
    private CCSprite pass, passBack, next, reload;
    private CCSprite backBottom, nextBottom, reloadBottom, tip, tipBack;
    private CCSprite back;
    private boolean banMove = false;
    private float scaleY, scaleX;
    private String levelFilename;
    private int levelNum;
    private int[] userScore;
    private int current_level;
    private int[][][] level_data;
    private int[] character_data;
    private Queue<TipItem> tipList;
    private int[][] slide_type;
    private LinkedList<TipItem> tip_log;
    private ArrayList<TipItem> success_list;
    private ArrayList<CCSprite> num_list;
    private int current_number = 0;
    private int tip_num = 0;
    private int current_move = -1;

    public WelcomeLayer(String rootPath, int level, int levelNum) {
        this.setIsTouchEnabled(true);
        this.setAnchorPoint(CGPoint.make(0, 0));
        this.setScale(1.0f);
        this.setContentSize(1080, 1794);
        this.levelFilename = rootPath + "/data/level.dat";
        this.current_level = level;
        this.success_list = new ArrayList<>();
        this.levelNum = levelNum;
        this.num_list = new ArrayList<>();
        tipList = new LinkedList<>();
        CCDirector.sharedDirector().displaySize().set(1080, 1794);
        initLevels();
        init();
        initReader();

    }

    private void initNumber() {
        changeNumber();
        current_number = 0;
        CCSprite sprite = new CCSprite("game/0.png");
        float width = CCDirector.sharedDirector().displaySize().width;
        sprite.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, 100 * scaleY)));
        sprite.setScaleX(1.0f / scaleX);
        sprite.setScaleY(0.7f / scaleY);
        num_list.add(sprite);
        showNumber();
    }

    private void showNumber() {
        for (int i = 0; i < num_list.size(); i++) {
            addChild(num_list.get(i));
        }
    }

    private void changeNumber() {
        for (int i = 0; i < num_list.size(); i++) {
            num_list.get(i).removeSelf();
        }
        num_list.clear();
    }

    private void addNumber() {
        current_number++;
        changeNumber();
        String number = current_number + "";
        float width = CCDirector.sharedDirector().displaySize().width;
        for (int i = 0; i < number.length(); i++) {
            String filename = "game/" + number.charAt(i) + ".png";
            CCSprite sprite = new CCSprite(filename);
            sprite.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2 + (i * 2 - number.length()) / 2.0f * 60 * scaleX, 100 * scaleY)));
            sprite.setScaleX(1.0f / scaleX);
            sprite.setScaleY(0.7f / scaleY);
            num_list.add(sprite);
        }
        showNumber();
    }

    private void initBack() {
        back = new CCSprite("game/chooseBack.jpg");
        float width = CCDirector.sharedDirector().displaySize().width;
        float height = CCDirector.sharedDirector().displaySize().height;
        back.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2, height / 2)));
        back.setScaleX(1.05f / scaleX);
        back.setScaleY(0.6f / scaleY);
        addChild(back);
    }

    private void initReader() {
        userScore = new int[levelNum];
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(levelFilename));
            while(this.success_list.add((TipItem) in.readObject()));
            System.out.println(this.success_list.size());
            in.close();
        }
        catch (Exception e) {
            System.out.println(this.success_list.size());
            System.out.println(e);
        }
    }

    private void init() {
        initMap();
        initBack();
        initNumber();
        initSprite();
        initBottomButton();
    }

    private void initLevels() {
        level_data = new int[levelNum][slide_num][3];
        character_data = new int[levelNum];
        slide_type = new int[levelNum][slide_num];
        level_data[0][0][0] = 0;level_data[0][0][1] = 100;level_data[0][0][2] = 81;slide_type[0][0] = 2;
        level_data[0][1][0] = 1;level_data[0][1][1] = 201;level_data[0][1][2] = 81;slide_type[0][1] = 1;
        level_data[0][2][0] = 3;level_data[0][2][1] = 100;level_data[0][2][2] = 81;slide_type[0][2] = 2;
        level_data[0][3][0] = 8;level_data[0][3][1] = 100;level_data[0][3][2] = 81;slide_type[0][3] = 2;
        level_data[0][4][0] = 9;level_data[0][4][1] = 201;level_data[0][4][2] = 40;slide_type[0][4] = 3;
        level_data[0][5][0] = 11;level_data[0][5][1] = 100;level_data[0][5][2] = 81;slide_type[0][5] = 2;
        level_data[0][6][0] = 13;level_data[0][6][1] = 100;level_data[0][6][2] = 40;slide_type[0][6] = 4;
        level_data[0][7][0] = 14;level_data[0][7][1] = 100;level_data[0][7][2] = 40;slide_type[0][7] = 4;
        level_data[0][8][0] = 16;level_data[0][8][1] = 100;level_data[0][8][2] = 40;slide_type[0][8] = 4;
        level_data[0][9][0] = 19;level_data[0][9][1] = 100;level_data[0][9][2] = 40;slide_type[0][9] = 4;
        character_data[0] = 1;
        level_data[1][0][0] = 0;level_data[1][0][1] = 100;level_data[1][0][2] = 81;slide_type[1][0] = 2;
        level_data[1][1][0] = 1;level_data[1][1][1] = 201;level_data[1][1][2] = 81;slide_type[1][1] = 1;
        level_data[1][2][0] = 3;level_data[1][2][1] = 100;level_data[1][2][2] = 81;slide_type[1][2] = 2;
        level_data[1][3][0] = 8;level_data[1][3][1] = 100;level_data[1][3][2] = 40;slide_type[1][3] = 4;
        level_data[1][4][0] = 9;level_data[1][4][1] = 201;level_data[1][4][2] = 40;slide_type[1][4] = 3;
        level_data[1][5][0] = 11;level_data[1][5][1] = 100;level_data[1][5][2] = 40;slide_type[1][5] = 4;
        level_data[1][6][0] = 12;level_data[1][6][1] = 100;level_data[1][6][2] = 81;slide_type[1][6] = 2;
        level_data[1][7][0] = 13;level_data[1][7][1] = 100;level_data[1][7][2] = 40;slide_type[1][7] = 4;
        level_data[1][8][0] = 14;level_data[1][8][1] = 100;level_data[1][8][2] = 40;slide_type[1][8] = 4;
        level_data[1][9][0] = 15;level_data[1][9][1] = 100;level_data[1][9][2] = 81;slide_type[1][9] = 2;
        character_data[1] = 1;
        level_data[2][0][0] = 4;level_data[2][0][1] = 100;level_data[2][0][2] = 81;slide_type[2][0] = 2;
        level_data[2][1][0] = 1;level_data[2][1][1] = 201;level_data[2][1][2] = 81;slide_type[2][1] = 1;
        level_data[2][2][0] = 7;level_data[2][2][1] = 100;level_data[2][2][2] = 81;slide_type[2][2] = 2;
        level_data[2][3][0] = 9;level_data[2][3][1] = 100;level_data[2][3][2] = 81;slide_type[2][3] = 2;
        level_data[2][4][0] = 10;level_data[2][4][1] = 100;level_data[2][4][2] = 81;slide_type[2][4] = 2;
        level_data[2][5][0] = 12;level_data[2][5][1] = 100;level_data[2][5][2] = 40;slide_type[2][5] = 4;
        level_data[2][6][0] = 15;level_data[2][6][1] = 100;level_data[2][6][2] = 40;slide_type[2][6] = 4;
        level_data[2][7][0] = 16;level_data[2][7][1] = 201;level_data[2][7][2] = 40;slide_type[2][7] = 3;
        level_data[2][8][0] = 18;level_data[2][8][1] = 100;level_data[2][8][2] = 40;slide_type[2][8] = 4;
        level_data[2][9][0] = 19;level_data[2][9][1] = 100;level_data[2][9][2] = 40;slide_type[2][9] = 4;
        character_data[2] = 1;
        level_data[3][0][0] = 0;level_data[3][0][1] = 100;level_data[3][0][2] = 81;slide_type[3][0] = 2;
        level_data[3][1][0] = 1;level_data[3][1][1] = 201;level_data[3][1][2] = 81;slide_type[3][1] = 1;
        level_data[3][2][0] = 3;level_data[3][2][1] = 100;level_data[3][2][2] = 81;slide_type[3][2] = 2;
        level_data[3][3][0] = 8;level_data[3][3][1] = 100;level_data[3][3][2] = 40;slide_type[3][3] = 4;
        level_data[3][4][0] = 9;level_data[3][4][1] = 100;level_data[3][4][2] = 40;slide_type[3][4] = 4;
        level_data[3][5][0] = 10;level_data[3][5][1] = 100;level_data[3][5][2] = 40;slide_type[3][5] = 4;
        level_data[3][6][0] = 11;level_data[3][6][1] = 100;level_data[3][6][2] = 40;slide_type[3][6] = 4;
        level_data[3][7][0] = 12;level_data[3][7][1] = 100;level_data[3][7][2] = 81;slide_type[3][7] = 2;
        level_data[3][8][0] = 13;level_data[3][8][1] = 201;level_data[3][8][2] = 40;slide_type[3][8] = 3;
        level_data[3][9][0] = 15;level_data[3][9][1] = 100;level_data[3][9][2] = 81;slide_type[3][9] = 2;
        character_data[3] = 1;
        level_data[4][0][0] = 0;level_data[4][0][1] = 100;level_data[4][0][2] = 40;slide_type[4][0] = 4;
        level_data[4][1][0] = 1;level_data[4][1][1] = 201;level_data[4][1][2] = 81;slide_type[4][1] = 1;
        level_data[4][2][0] = 3;level_data[4][2][1] = 100;level_data[4][2][2] = 40;slide_type[4][2] = 4;
        level_data[4][3][0] = 4;level_data[4][3][1] = 100;level_data[4][3][2] = 81;slide_type[4][3] = 2;
        level_data[4][4][0] = 7;level_data[4][4][1] = 100;level_data[4][4][2] = 81;slide_type[4][4] = 2;
        level_data[4][5][0] = 9;level_data[4][5][1] = 201;level_data[4][5][2] = 40;slide_type[4][5] = 3;
        level_data[4][6][0] = 12;level_data[4][6][1] = 100;level_data[4][6][2] = 81;slide_type[4][6] = 2;
        level_data[4][7][0] = 13;level_data[4][7][1] = 100;level_data[4][7][2] = 40;slide_type[4][7] = 4;
        level_data[4][8][0] = 14;level_data[4][8][1] = 100;level_data[4][8][2] = 40;slide_type[4][8] = 4;
        level_data[4][9][0] = 15;level_data[4][9][1] = 100;level_data[4][9][2] = 81;slide_type[4][9] = 2;
        character_data[4] = 1;
        level_data[5][0][0] = 0;level_data[5][0][1] = 100;level_data[5][0][2] = 81;slide_type[5][0] = 2;
        level_data[5][1][0] = 1;level_data[5][1][1] = 201;level_data[5][1][2] = 81;slide_type[5][1] = 1;
        level_data[5][2][0] = 3;level_data[5][2][1] = 100;level_data[5][2][2] = 40;slide_type[5][2] = 4;
        level_data[5][3][0] = 7;level_data[5][3][1] = 100;level_data[5][3][2] = 40;slide_type[5][3] = 4;
        level_data[5][4][0] = 8;level_data[5][4][1] = 100;level_data[5][4][2] = 81;slide_type[5][4] = 2;
        level_data[5][5][0] = 9;level_data[5][5][1] = 201;level_data[5][5][2] = 40;slide_type[5][5] = 3;
        level_data[5][6][0] = 11;level_data[5][6][1] = 100;level_data[5][6][2] = 81;slide_type[5][6] = 2;
        level_data[5][7][0] = 13;level_data[5][7][1] = 100;level_data[5][7][2] = 81;slide_type[5][7] = 2;
        level_data[5][8][0] = 16;level_data[5][8][1] = 100;level_data[5][8][2] = 40;slide_type[5][8] = 4;
        level_data[5][9][0] = 19;level_data[5][9][1] = 100;level_data[5][9][2] = 40;slide_type[5][9] = 4;
        character_data[5] = 1;
        level_data[6][0][0] = 0;level_data[6][0][1] = 100;level_data[6][0][2] = 40;slide_type[6][0] = 4;
        level_data[6][1][0] = 1;level_data[6][1][1] = 201;level_data[6][1][2] = 81;slide_type[6][1] = 1;
        level_data[6][2][0] = 3;level_data[6][2][1] = 100;level_data[6][2][2] = 40;slide_type[6][2] = 4;
        level_data[6][3][0] = 4;level_data[6][3][1] = 100;level_data[6][3][2] = 40;slide_type[6][3] = 4;
        level_data[6][4][0] = 7;level_data[6][4][1] = 100;level_data[6][4][2] = 40;slide_type[6][4] = 4;
        level_data[6][5][0] = 8;level_data[6][5][1] = 100;level_data[6][5][2] = 81;slide_type[6][5] = 2;
        level_data[6][6][0] = 9;level_data[6][6][1] = 100;level_data[6][6][2] = 81;slide_type[6][6] = 2;
        level_data[6][7][0] = 10;level_data[6][7][1] = 100;level_data[6][7][2] = 81;slide_type[6][7] = 2;
        level_data[6][8][0] = 11;level_data[6][8][1] = 100;level_data[6][8][2] = 81;slide_type[6][8] = 2;
        level_data[6][9][0] = 17;level_data[6][9][1] = 201;level_data[6][9][2] = 40;slide_type[6][9] = 3;
        character_data[6] = 1;
        level_data[7][0][0] = 0;level_data[7][0][1] = 100;level_data[7][0][2] = 40;slide_type[7][0] = 4;
        level_data[7][1][0] = 1;level_data[7][1][1] = 201;level_data[7][1][2] = 81;slide_type[7][1] = 1;
        level_data[7][2][0] = 3;level_data[7][2][1] = 100;level_data[7][2][2] = 40;slide_type[7][2] = 4;
        level_data[7][3][0] = 4;level_data[7][3][1] = 100;level_data[7][3][2] = 81;slide_type[7][3] = 2;
        level_data[7][4][0] = 7;level_data[7][4][1] = 100;level_data[7][4][2] = 81;slide_type[7][4] = 2;
        level_data[7][5][0] = 9;level_data[7][5][1] = 100;level_data[7][5][2] = 81;slide_type[7][5] = 2;
        level_data[7][6][0] = 10;level_data[7][6][1] = 100;level_data[7][6][2] = 81;slide_type[7][6] = 2;
        level_data[7][7][0] = 12;level_data[7][7][1] = 100;level_data[7][7][2] = 40;slide_type[7][7] = 4;
        level_data[7][8][0] = 15;level_data[7][8][1] = 100;level_data[7][8][2] = 40;slide_type[7][8] = 4;
        level_data[7][9][0] = 17;level_data[7][9][1] = 201;level_data[7][9][2] = 40;slide_type[7][9] = 3;
        character_data[7] = 1;
        level_data[8][0][0] = 0;level_data[8][0][1] = 100;level_data[8][0][2] = 81;slide_type[8][0] = 2;
        level_data[8][1][0] = 1;level_data[8][1][1] = 201;level_data[8][1][2] = 81;slide_type[8][1] = 1;
        level_data[8][2][0] = 3;level_data[8][2][1] = 100;level_data[8][2][2] = 40;slide_type[8][2] = 4;
        level_data[8][3][0] = 7;level_data[8][3][1] = 100;level_data[8][3][2] = 40;slide_type[8][3] = 4;
        level_data[8][4][0] = 8;level_data[8][4][1] = 100;level_data[8][4][2] = 81;slide_type[8][4] = 2;
        level_data[8][5][0] = 9;level_data[8][5][1] = 100;level_data[8][5][2] = 81;slide_type[8][5] = 2;
        level_data[8][6][0] = 10;level_data[8][6][1] = 100;level_data[8][6][2] = 81;slide_type[8][6] = 2;
        level_data[8][7][0] = 11;level_data[8][7][1] = 100;level_data[8][7][2] = 40;slide_type[8][7] = 4;
        level_data[8][8][0] = 15;level_data[8][8][1] = 100;level_data[8][8][2] = 40;slide_type[8][8] = 4;
        level_data[8][9][0] = 17;level_data[8][9][1] = 201;level_data[8][9][2] = 40;slide_type[8][9] = 3;
        character_data[8] = 1;
        level_data[9][0][0] = 0;level_data[9][0][1] = 100;level_data[9][0][2] = 81;slide_type[9][0] = 2;
        level_data[9][1][0] = 1;level_data[9][1][1] = 201;level_data[9][1][2] = 81;slide_type[9][1] = 1;
        level_data[9][2][0] = 3;level_data[9][2][1] = 100;level_data[9][2][2] = 40;slide_type[9][2] = 4;
        level_data[9][3][0] = 7;level_data[9][3][1] = 100;level_data[9][3][2] = 40;slide_type[9][3] = 4;
        level_data[9][4][0] = 8;level_data[9][4][1] = 100;level_data[9][4][2] = 81;slide_type[9][4] = 2;
        level_data[9][5][0] = 9;level_data[9][5][1] = 201;level_data[9][5][2] = 40;slide_type[9][5] = 3;
        level_data[9][6][0] = 11;level_data[9][6][1] = 100;level_data[9][6][2] = 81;slide_type[9][6] = 2;
        level_data[9][7][0] = 13;level_data[9][7][1] = 100;level_data[9][7][2] = 40;slide_type[9][7] = 4;
        level_data[9][8][0] = 14;level_data[9][8][1] = 100;level_data[9][8][2] = 81;slide_type[9][8] = 2;
        level_data[9][9][0] = 17;level_data[9][9][1] = 100;level_data[9][9][2] = 40;slide_type[9][9] = 4;
        character_data[9] = 1;
        level_data[10][0][0] = 0;level_data[10][0][1] = 100;level_data[10][0][2] = 81;slide_type[10][0] = 2;
        level_data[10][1][0] = 1;level_data[10][1][1] = 201;level_data[10][1][2] = 81;slide_type[10][1] = 1;
        level_data[10][2][0] = 3;level_data[10][2][1] = 100;level_data[10][2][2] = 40;slide_type[10][2] = 4;
        level_data[10][3][0] = 7;level_data[10][3][1] = 100;level_data[10][3][2] = 40;slide_type[10][3] = 4;
        level_data[10][4][0] = 8;level_data[10][4][1] = 100;level_data[10][4][2] = 81;slide_type[10][4] = 2;
        level_data[10][5][0] = 9;level_data[10][5][1] = 201;level_data[10][5][2] = 40;slide_type[10][5] = 3;
        level_data[10][6][0] = 11;level_data[10][6][1] = 100;level_data[10][6][2] = 40;slide_type[10][6] = 4;
        level_data[10][7][0] = 13;level_data[10][7][1] = 100;level_data[10][7][2] = 81;slide_type[10][7] = 2;
        level_data[10][8][0] = 14;level_data[10][8][1] = 100;level_data[10][8][2] = 81;slide_type[10][8] = 2;
        level_data[10][9][0] = 15;level_data[10][9][1] = 100;level_data[10][9][2] = 40;slide_type[10][9] = 4;
        character_data[10] = 1;
        level_data[11][0][0] = 0;level_data[11][0][1] = 100;level_data[11][0][2] = 40;slide_type[11][0] = 4;
        level_data[11][1][0] = 1;level_data[11][1][1] = 201;level_data[11][1][2] = 81;slide_type[11][1] = 1;
        level_data[11][2][0] = 3;level_data[11][2][1] = 100;level_data[11][2][2] = 40;slide_type[11][2] = 4;
        level_data[11][3][0] = 4;level_data[11][3][1] = 100;level_data[11][3][2] = 40;slide_type[11][3] = 4;
        level_data[11][4][0] = 7;level_data[11][4][1] = 100;level_data[11][4][2] = 40;slide_type[11][4] = 4;
        level_data[11][5][0] = 9;level_data[11][5][1] = 201;level_data[11][5][2] = 40;slide_type[11][5] = 3;
        level_data[11][6][0] = 12;level_data[11][6][1] = 100;level_data[11][6][2] = 81;slide_type[11][6] = 2;
        level_data[11][7][0] = 13;level_data[11][7][1] = 100;level_data[11][7][2] = 81;slide_type[11][7] = 2;
        level_data[11][8][0] = 14;level_data[11][8][1] = 100;level_data[11][8][2] = 81;slide_type[11][8] = 2;
        level_data[11][9][0] = 15;level_data[11][9][1] = 100;level_data[11][9][2] = 81;slide_type[11][9] = 2;
        character_data[11] = 1;
    }

    private void initSets() {
        scaleY = CCDirector.sharedDirector().displaySize().height / 1794;
        scaleX = CCDirector.sharedDirector().displaySize().width / 1080;
        map_sets[0] = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 200 * scaleY));
        map_sets[1] = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 200 * scaleY));
        map_sets[2] = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 200 * scaleY));
        map_sets[3] = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 200 * scaleY));
        map_sets[4] = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 430 * scaleY));
        map_sets[5] = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 430 * scaleY));
        map_sets[6] = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 430 * scaleY));
        map_sets[7] = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 430 * scaleY));
        map_sets[8] = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 660 * scaleY));
        map_sets[9] = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 660 * scaleY));
        map_sets[10] = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 660 * scaleY));
        map_sets[11] = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 660 * scaleY));
        map_sets[12] = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 890 * scaleY));
        map_sets[13] = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 890 * scaleY));
        map_sets[14] = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 890 * scaleY));
        map_sets[15] = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 890 * scaleY));
        map_sets[16] = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 1120 * scaleY));
        map_sets[17] = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 1120 * scaleY));
        map_sets[18] = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 1120 * scaleY));
        map_sets[19] = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 1120 * scaleY));
        map_bounding = this.getBoundingBox();
        init_height = 40;
        init_width = 100;
    }
    

    private void initMap() {
        map_positions = new CGRect[slide_num];
        map_sets = new CGPoint[20];
        current_position = new int[slide_num];
        tip_log = new LinkedList<>();
        tipList = new LinkedList<>();
        initSets();
        map_points = new CGPoint[slide_num];
        for (int i = 0; i < slide_num; i++) {
            map_points[i] = CGPoint.make(map_sets[level_data[current_level][i][0]].x, map_sets[level_data[current_level][i][0]].y);
            current_position[i] = level_data[current_level][i][0];
            map_positions[i] = CGRect.make(0, 0, level_data[current_level][i][1], level_data[current_level][i][2]);
        }
        character = character_data[current_level];
    }

    private void getTip() {
        banMove = true;
        tip_log = new LinkedList<>();
        int[] newTipArray = new int[20];
        for (int i = 0; i < 20; i++) {
            newTipArray[i] = 0;
        }
        for (int i = 0; i < slide_num; i++) {
            int pos = current_position[i];
            switch (slide_type[current_level][i]) {
                case 1:
                    newTipArray[pos] = 1;
                    newTipArray[pos + 1] = 1;
                    newTipArray[pos + 4] = 1;
                    newTipArray[pos + 5] = 1;
                    break;
                case 2:
                    newTipArray[pos] = 2;
                    newTipArray[pos + 4] = 2;
                    break;
                case 3:
                    newTipArray[pos] = 3;
                    newTipArray[pos + 1] = 3;
                    break;
                case 4:
                    newTipArray[pos] = 4;
                    break;
            }
        }
        TipItem newTip = new TipItem(newTipArray, null, 0, 0);
        if (success_list.contains(newTip)) {
            int index = success_list.indexOf(newTip);
            System.out.println(index + " " + success_list.size());
            if (index > 0) {
                banMove = false;
                tip_num = 0;
                moveTip(success_list.get(index - 1).list, success_list.get(index - 1).to);
                return;
            }
            else if(index == 0) {
                tip_num = 0;
                return;
            }
        }
        System.out.println("in back");
        if (tip_num == 0) {
            float width = CCDirector.sharedDirector().displaySize().width;
            float height = CCDirector.sharedDirector().displaySize().height;
            tipBack = new CCSprite("game/tipBack.png");
            tipBack.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(width / 2 - (130) * scaleX, height / 2)));
            tipBack.setScaleX(0.9f / scaleX);
            tipBack.setScaleY(0.5f / scaleY);
            this.addChild(tipBack, 1);
            tip_num++;
            return;
        }
        tipInit(newTip, newTipArray);
        tip_num = 0;
    }
    private void tipInit(TipItem newTip, int[] newTipArray){
        tip_log = new LinkedList<>();
        tipList = new LinkedList<>();
        tipList.add(newTip);
        tip_log.add(newTip);
        findNewMove(newTipArray, null);
        tipList.remove();
        int init_len = tipList.size();
        for (int i = 0; i < init_len; i++) {
            TipItem tempItem = tipList.remove();
            if (success_list.contains(tempItem)) {
                System.out.println(success_list.indexOf(tempItem) + " " + success_list.size());
                while(tempItem != null) {
                    success_list.add(tempItem);
                    tempItem = tempItem.before;
                }
                moveTip(success_list.get(success_list.size() - 1).list, success_list.get(success_list.size() - 1).to);
                success_list.add(newTip);
                banMove = false;
                tip_num = 0;
                return;
            }
//            System.out.println(tempItem.list[0] + " " + tempItem.list[1] + " " + tempItem.list[2] + " " + tempItem.list[3]);
//            System.out.println(tempItem.list[4] + " " + tempItem.list[5] + " " + tempItem.list[6] + " " + tempItem.list[7]);
//            System.out.println(tempItem.list[8] + " " + tempItem.list[9] + " " + tempItem.list[10] + " " + tempItem.list[11]);
//            System.out.println(tempItem.list[12] + " " + tempItem.list[13] + " " + tempItem.list[14] + " " + tempItem.list[15]);
//            System.out.println(tempItem.list[16] + " " + tempItem.list[17] + " " + tempItem.list[18] + " " + tempItem.list[19]);
            if (checkTipFinish(tempItem.list)) {
                banMove = false;
                tipBack.removeSelf();
                FinishCalculate(tempItem);
                return;
            }
            findNewMove(tempItem.list, tempItem);
        }
        while (tipList.size() > 0) {
            TipItem tempItem;
            tempItem = tipList.remove();
            if (success_list.contains(tempItem)) {
                System.out.println(success_list.indexOf(tempItem) + " " + success_list.size());
                while(tempItem != null) {
                    success_list.add(tempItem);
                    tempItem = tempItem.before;
                }
                moveTip(success_list.get(success_list.size() - 1).list, success_list.get(success_list.size() - 1).to);
                success_list.add(newTip);
                banMove = false;
                tip_num = 0;
                return;
            }
            if (checkTipFinish(tempItem.list)){
                banMove=false;
                if (tipBack != null) {
                    tipBack.removeSelf();
                }
                FinishCalculate(tempItem);
                success_list.add(newTip);
                return;
            }
                findNewMove(tempItem.list, tempItem);
        }
    }

    private void FinishCalculate(TipItem finishItem) {
        System.out.println("tip finish");
        while (finishItem != null) {
            success_list.add(finishItem);
            finishItem = finishItem.before;
        }
        tip_num = 0;
        moveTip(success_list.get(success_list.size() - 1).list, success_list.get(success_list.size() - 1).to);
        System.out.println("destroy back");
    }

    private void moveTip(int[] tipArray, int to) {
        for (int i = 0; i < slide_num; i++) {
            int pos = current_position[i];
            switch(slide_type[current_level][i]) {
                case 1:
                    if (tipArray[pos] == 0 || tipArray[pos + 1] == 0 || tipArray[pos + 4] == 0 || tipArray[pos + 5] == 0) {
                        TAG_CUR = i;
                    }
                    break;
                case 2:
                    if (tipArray[pos] == 0 || tipArray[pos + 4] == 0) {
                        TAG_CUR = i;
                    }
                    break;
                case 3:
                    if (tipArray[pos] == 0 || tipArray[pos + 1] == 0) {
                        TAG_CUR = i;
                    }
                    break;
                case 4:
                    if (tipArray[pos] == 0) {
                        TAG_CUR = i;
                    }
                    break;
            }
        }
        System.out.println(TAG_CUR);
        switch (to) {
            case 1:
                move("left");
                break;
            case 2:
                move("up");
                break;
            case 3:
                move("right");
                break;
            case 4:
                move("down");
                break;
        }
    }

    private boolean checkTipFinish(int[] tipArray) {
        if (tipArray[13] == 1 && tipArray[14] == 1 && tipArray[17] == 1 && tipArray[18] == 1) {
            return true;
        }
        return false;
    }

    private void findNewMove(int[] tipArray, TipItem ancestor) {
        ArrayList<Integer> jumpList = new ArrayList<Integer>();
        for (int i = 0; i < 20; i++) {
            if (jumpList.contains(i)) {
                continue;
            }
            switch(tipArray[i]) {
                case 1:
                    jumpList.add(i + 1);
                    jumpList.add(i + 4);
                    jumpList.add(i + 5);
                    if (i % 4 != 2 && tipArray[i + 2] == 0 && tipArray[i + 6] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 4] = 0;
                        newArray[i + 2] = 1;
                        newArray[i + 6] = 1;
                        TipItem newItem = new TipItem(newArray, ancestor, 1, 3);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i % 4 != 0 && tipArray[i - 1] == 0 && tipArray[i + 3] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i + 1] = 0;
                        newArray[i + 5] = 0;
                        newArray[i - 1] = 1;
                        newArray[i + 3] = 1;
                        TipItem newItem = new TipItem(newArray, ancestor, 1, 1);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i > 3 && tipArray[i - 4] == 0 && tipArray[i - 3] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i + 4] = 0;
                        newArray[i + 5] = 0;
                        newArray[i - 4] = 1;
                        newArray[i - 3] = 1;
                        TipItem newItem = new TipItem(newArray, ancestor, 1, 2);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i < 12 && tipArray[i + 8] == 0 && tipArray[i + 9] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 1] = 0;
                        newArray[i + 8] = 1;
                        newArray[i + 9] = 1;
                        TipItem newItem = new TipItem(newArray, ancestor, 1, 4);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    break;
                case 2:
                    jumpList.add(i + 4);
                    if (i % 4 != 0 && tipArray[i - 1] == 0 && tipArray[i + 3] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 4] = 0;
                        newArray[i - 1] = 2;
                        newArray[i + 3] = 2;
                        TipItem newItem = new TipItem(newArray, ancestor, 2, 1);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i % 4 != 3 && tipArray[i + 1] == 0 && tipArray[i + 5] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 4] = 0;
                        newArray[i + 1] = 2;
                        newArray[i + 5] = 2;
                        TipItem newItem = new TipItem(newArray, ancestor, 2, 3);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i > 3 && tipArray[i - 4] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i + 4] = 0;
                        newArray[i - 4] = 2;
                        TipItem newItem = new TipItem(newArray, ancestor, 2, 2);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i < 12 && tipArray[i + 8] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 8] = 2;
                        TipItem newItem = new TipItem(newArray, ancestor, 2, 4);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    break;
                case 3:
                    jumpList.add(i + 1);
                    if (i % 4 != 0 && tipArray[i - 1] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i + 1] = 0;
                        newArray[i - 1] = 3;
                        TipItem newItem = new TipItem(newArray, ancestor, 3, 1);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i % 4 != 2 && tipArray[i + 2] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 2] = 3;
                        TipItem newItem = new TipItem(newArray, ancestor, 3, 3);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i < 16 && tipArray[i + 4] == 0 && tipArray[i + 5] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 1] = 0;
                        newArray[i + 4] = 3;
                        newArray[i + 5] = 3;
                        TipItem newItem = new TipItem(newArray, ancestor, 3, 4);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i > 3 && tipArray[i - 4] == 0 && tipArray[i - 3] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 1] = 0;
                        newArray[i - 4] = 3;
                        newArray[i - 3] = 3;
                        TipItem newItem = new TipItem(newArray, ancestor, 3, 2);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    break;
                case 4:
                    if (i % 4 != 0 && tipArray[i - 1] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i - 1] = 4;
                        TipItem newItem = new TipItem(newArray, ancestor, 4, 1);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i % 4 != 3 && tipArray[i + 1] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 1] = 4;
                        TipItem newItem = new TipItem(newArray, ancestor, 4, 3);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i > 3 && tipArray[i - 4] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i - 4] = 4;
                        TipItem newItem = new TipItem(newArray, ancestor, 4, 2);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    if (i < 16 && tipArray[i + 4] == 0) {
                        int[] newArray = tipArray.clone();
                        newArray[i] = 0;
                        newArray[i + 4] = 4;
                        TipItem newItem = new TipItem(newArray, ancestor, 4, 4);
                        synchronized (tip_log) {
                            if (!tip_log.contains(newItem)) {
                                tipList.add(newItem);
                                tip_log.add(newItem);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onExit() {
        System.out.println("on Exit");
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(levelFilename));
            for (int i = 0; i < success_list.size(); i++) {
                out.writeObject(success_list.get(i));
            }
            out.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        super.onExit();
    }

    private void initBottomButton() {
        backBottom = new CCSprite("game/back_bottom.png");
        backBottom.setScaleX(0.5f);
        backBottom.setScaleY(0.2f);
        backBottom.setAnchorPoint(0, 1);
        CGPoint point = CCDirector.sharedDirector().convertToGL(CGPoint.make(85 * scaleX, 1450 * scaleY));
        backBottom.setPosition(point);
        this.addChild(backBottom, 0, slide_num );
        CCFadeIn fadeIn = CCFadeIn.action(0.5f);
        backBottom.runAction(fadeIn);

        reloadBottom = new CCSprite("game/reload_bottom.png");
        reloadBottom.setScaleX(0.5f);
        reloadBottom.setScaleY(0.2f);
        reloadBottom.setAnchorPoint(0, 1);
        point = CCDirector.sharedDirector().convertToGL(CGPoint.make(312 * scaleX, 1450 * scaleY));
        reloadBottom.setPosition(point);
        this.addChild(reloadBottom, 0, slide_num + 1);
        fadeIn = CCFadeIn.action(0.5f);
        reloadBottom.runAction(fadeIn);

        tip = new CCSprite("game/tip.png");
        tip.setScaleX(0.5f);
        tip.setScaleY(0.2f);
        tip.setAnchorPoint(0, 1);
        point = CCDirector.sharedDirector().convertToGL(CGPoint.make(540 * scaleX, 1450 * scaleY));
        tip.setPosition(point);
        this.addChild(tip, 0, slide_num + 2);
        fadeIn = CCFadeIn.action(0.5f);
        tip.runAction(fadeIn);

        nextBottom = new CCSprite("game/next_bottom.png");
        nextBottom.setScaleX(0.5f);
        nextBottom.setScaleY(0.2f);
        nextBottom.setAnchorPoint(0, 1);
        point = CCDirector.sharedDirector().convertToGL(CGPoint.make(768 * scaleX, 1450 * scaleY));
        nextBottom.setPosition(point);
        this.addChild(nextBottom, 0, slide_num + 3);
        fadeIn = CCFadeIn.action(0.5f);
        nextBottom.runAction(fadeIn);

    }

    private void reloadMap(int finishId) {
        for (int i = 0; i < finishId; i++) {
            CCSprite sprite = (CCSprite) getChildByTag(i);
            sprite.setVisible(false);
            sprite.removeSelf();
        }
        initMap();
        initNumber();
        initSprite();
        initBottomButton();
        banMove = false;
    }

    private void nextLevel(int finishId) {
        if (current_level < levelNum - 1) {
            current_level++;
        }
        reloadMap(finishId);
    }

    private void preLevel(int finishId) {
        if (current_level > 0) {
            current_level--;
        }
        reloadMap(finishId);
    }

    private void checkFinish() {
        if (current_position[character] == 13) {
            passBack = new CCSprite("game/passBack.png");
            passBack.setPosition(CCDirector.sharedDirector().convertToGL(CGPoint.make(500, 500)));
            this.addChild(passBack, 1, slide_num + 4);

            pass = new CCSprite("game/pass.png");
            pass.setScaleY(0.5f);
            CGPoint startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 2, 0));
            CGPoint finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 2, 654 * scaleY));
            pass.setPosition(startPoint);
            this.addChild(pass, 2, slide_num + 5);
            CCIntervalAction actionTo = CCMoveTo.action((float)0.5, finishPoint);
            CCEaseSineOut easeSineOut = CCEaseSineOut.action(actionTo);
            pass.runAction(easeSineOut);

            banMove = true;

            next = new CCSprite("game/next.png");
            next.setScaleY(0.375f / scaleY);
            startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 4 * 3, 1775 * scaleY));
            finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 4 * 3, 1250 * scaleY));
            next.setPosition(startPoint);
            this.addChild(next, 2, slide_num + 6);
            actionTo = CCMoveTo.action((float)0.5, finishPoint);
            easeSineOut = CCEaseSineOut.action(actionTo);
            next.runAction(easeSineOut);

            reload = new CCSprite("game/reload.png");
            reload.setScaleY(0.375f / scaleY);
            startPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 4, 1775 * scaleY));
            finishPoint = CCDirector.sharedDirector().convertToGL(CGPoint.make(CCDirector.sharedDirector().displaySize().width / 4, 1250 * scaleY));
            reload.setPosition(startPoint);
            this.addChild(reload, 2, slide_num + 7);
            actionTo = CCMoveTo.action((float)0.5, finishPoint);
            easeSineOut = CCEaseSineOut.action(actionTo);
            reload.runAction(easeSineOut);


        }
    }

    private void initSprite() {
        slides = new CCSprite[slide_num];
        for (int i = 0; i < slide_num; i++) {
            CCSprite sprite = new CCSprite("game/slide.png", map_positions[i]);
            sprite.setTag(i);
            sprite.setPosition(map_points[i]);
            sprite.setAnchorPoint(CGPoint.make(0, 1));
            this.addChild(sprite, 0, i);
            slides[i] = sprite;
            CCFadeIn fadeIn = CCFadeIn.action(0.5f);
            sprite.runAction(fadeIn);
        }
    }

    private boolean judgeBounding(CGRect rect, int tag) {
        for (int i = 0; i < slide_num; i++) {
            if (i != tag) {
                CGRect boundingRect = slides[i].getBoundingBox();
                if (CGRect.intersects(boundingRect, rect)) {
                    return false;
                }
            }
        }
        return CGRect.containsRect(map_bounding, rect);
    }

    private void move(String position) {
        if (tipBack != null) {
            tipBack.removeSelf();
            tipBack.setPosition(-1000, -1000);
        }
        CCSprite sprite = (CCSprite) getChildByTag(TAG_CUR);
        CGRect currentRect = CGRect.make(sprite.getBoundingBox());
        CGPoint currentPosition = CGPoint.make(currentRect.origin.x, currentRect.origin.y);
        int current_at = current_position[TAG_CUR];
        int new_current = current_at;
        switch (position) {
            case "left":
                if (current_at % 4 != 0) {
                    currentPosition.set(map_sets[current_at - 1]);
                    new_current = current_at - 1;
                }
                else {
                    return;
                }
                break;
            case "right":
                if (current_at % 4 != 3 && (current_at % 4 != 2 || currentRect.size.width <= init_width)) {
                    currentPosition.set(map_sets[current_at + 1]);
                    new_current = current_at + 1;
                }
                else {
                    return;
                }
                break;
            case "up":
                if (current_at > 3) {
                    currentPosition.set(map_sets[current_at - 4]);
                    new_current = current_at - 4;
                }
                else {
                    return;
                }
                break;
            case "down":
                if (current_at < 16 && (currentRect.size.height <= init_height || current_at < 12)) {
                    currentPosition.set(map_sets[current_at + 4]);
                    new_current = current_at + 4;
                }
                else {
                    return;
                }
                break;
        }
        CGPoint rectPosition = CGPoint.make(currentPosition.x , currentPosition.y - currentRect.size.height);
        CGRect rect = CGRect.make(rectPosition, currentRect.size);
        if (judgeBounding(rect, TAG_CUR)) {
            CCIntervalAction actionTo = CCMoveTo.action((float)0.3, currentPosition);
            CCEaseSineOut easeSineOut = CCEaseSineOut.action(actionTo);
            sprite.runAction(easeSineOut);
            //sprite.setPosition(currentPosition);
            current_position[TAG_CUR] = new_current;
            if (current_move != TAG_CUR) {
                addNumber();
                current_move = TAG_CUR;
            }
            checkFinish();
        }
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = 0; i < slide_num; i++) {
            CCSprite sprite = slides[i];
            boolean flag = CGRect.containsPoint(sprite.getBoundingBox(), cgPoint);
            if (flag) {
                TAG_CUR = sprite.getTag();
            }
        }
        CUR_POINT = cgPoint;
        return super.ccTouchesBegan(event);
    }

    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        if (CGRect.containsPoint(slides[TAG_CUR].getBoundingBox(), cgPoint)) {
            LEAVE_POINT = cgPoint;
        }
        return super.ccTouchesMoved(event);
    }

    private void touchButton(MotionEvent event) {
        if (reload == null || next == null) {
            return;
        }
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        CGRect reloadRect = reload.getBoundingBox();
        if (CGRect.containsPoint(reloadRect, cgPoint) && CGRect.containsPoint(reloadRect, CUR_POINT)) {
            reloadMap(slide_num + 8);
        }

        CGRect nextRect = next.getBoundingBox();
        if (CGRect.containsPoint(nextRect, cgPoint) && CGRect.containsPoint(nextRect, CUR_POINT)) {
            nextLevel(slide_num + 8);
        }
    }

    private boolean checkBottomButton(MotionEvent event) {
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        for (int i = slide_num; i < slide_num + 4; i++) {
            CCSprite sprite = (CCSprite)getChildByTag(i);
            CGRect rect = sprite.getBoundingBox();
            if (CGRect.containsPoint(rect, cgPoint) && CGRect.containsPoint(rect, CUR_POINT)) {
                if (i == slide_num ) {
                    preLevel(slide_num + 4);
                }
                else if (i == slide_num + 1) {
                    reloadMap(slide_num + 4);
                }
                else if (i == slide_num + 2) {
                    if (success_list.size() > 0) {
                        tip_num = -1;
                    }
                    getTip();
                }
                else {
                    nextLevel(slide_num + 4);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean ccTouchesEnded(MotionEvent event) {
        if (tipBack != null) {
            tipBack.removeSelf();
            tipBack.setPosition(-1000, -1000);
        }
        if (tip_num > 0) {
            getTip();
            tip_num = 0;
        }
        if (banMove) {
            touchButton(event);
            return super.ccTouchesEnded(event);
        }
        if (checkBottomButton(event)) {
            return super.ccTouchesEnded(event);
        }
        if (LEAVE_POINT == null) {
            return super.ccTouchesEnded(event);
        }
        float moveX = LEAVE_POINT.x - CUR_POINT.x;
        float moveY = LEAVE_POINT.y - CUR_POINT.y;
        if (Math.abs(moveX) > Math.abs(moveY) * 1.2 && Math.abs(moveX) > MOVE_GAP) {
            if (moveX > 0) {
                move("right");
            }
            else {
                move("left");
            }
        }
        else if (Math.abs(moveY) > Math.abs(moveX) && Math.abs(moveY) > MOVE_GAP) {
            if (moveY > 0) {
                move("up");
            }
            else {
                move("down");
            }
        }
        else if (Math.abs(moveY) == Math.abs(moveX) && Math.abs(moveY) > MOVE_GAP) {
            if (moveX > 0) {
                move("right");
                move("up");
            }
            else {
                move("down");
                move("left");
            }
        }
        return super.ccTouchesEnded(event);
    }
}
