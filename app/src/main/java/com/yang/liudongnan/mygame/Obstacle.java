package com.yang.liudongnan.mygame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Debug;
import android.util.Log;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private  int color;

    //getters
    public Rect getRectangle() {
        return rectangle;
    }

    public int getColor() {
        return color;
    }

    public void incremnentY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }


    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0,startY,startX,startY+rectHeight);
        rectangle2 = new Rect(startX+playerGap,startY,Constants.SCREEN_WIDTH,startY+rectHeight);
    }

    public boolean playerCollide(RectPlayer player){
        if(Rect.intersects(rectangle,player.getRectangle()) ||
                Rect.intersects(rectangle2,player.getRectangle()) ){
            System.out.println("collision detected!");
            return  true  ;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangle2,paint);
    }

    @Override
    public void update() {

    }

}
