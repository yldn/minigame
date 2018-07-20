package com.yang.liudongnan.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    public ObstacleManager(int playerGap , int obstacleGap,int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime =initTime= System.currentTimeMillis();

        obstacles = new ArrayList<>();
        populateObstacles();
    }
    //obstacles generator
    private void populateObstacles(){
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while (currY < 0){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH-playerGap));
            obstacles.add(new Obstacle(obstacleHeight,color,xStart,currY,playerGap));
            currY += obstacleHeight + obstacleGap;
        }

    }

    public boolean playerCollide(RectPlayer player){
        for (Obstacle ob : obstacles){
            if(ob.playerCollide(player)){
                return true ;
            }
        }
        return  false;
    }

    public void update(){
        if(startTime < Constants.INIT_TIME){
            startTime = Constants.INIT_TIME;
        }
        int elapsedTime = (int)(System.currentTimeMillis()-startTime);
        startTime = System.currentTimeMillis();
        //10 second smooth down the screen
        //速度每2sec增加
        float speed = (float) (Math.sqrt(1+ (startTime - initTime)/2000.0f)* Constants.SCREEN_HEIGHT/(10000.0f));
        for(Obstacle ob :obstacles){
            ob.incremnentY(speed*elapsedTime);
        }
        //what happens when leaving the screen
        if(obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT){
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH-playerGap));
            obstacles.add(0,new Obstacle(obstacleHeight,color,xStart,obstacles.get(0).getRectangle().top-obstacleHeight-obstacleGap,playerGap));
            obstacles.remove(obstacles.size()-1);
            //加分
            score ++;
        }

    }

    public void draw(Canvas canvas){
        for(Obstacle ob :obstacles){

            //draw score
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.MAGENTA);

            //x和y是text输出的位置
            //y加上一个paint的高度用来防止字体显示到屏幕外
            canvas.drawText("score:"+score,50,50+ paint.descent()-paint.ascent(),paint);
            ob.draw(canvas);
        }
    }


}
