package com.yang.liudongnan.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private  Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;


    private boolean movingPlayer = false;

    private boolean gameOver = false;
    private long gameOvertime;


    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);

        player = new RectPlayer(new Rect(100,100,200,200),
                Color.rgb(0,255,255));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350,75,Color.BLACK);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(),this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (true){
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                retry = false;
            }
        }
    }
    //manage touch input
    private String TAG = GestureDetector.class.getName();
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX() , (int) event.getY() )){
                    movingPlayer = true;
                }
                //如果点击则reset
                if(gameOver){
                    reset();
                    gameOver = false;
                }
                Log.d(TAG,"Action Down");
                break;
            case MotionEvent.ACTION_MOVE:
                if(movingPlayer & !gameOver) {
                    playerPoint.set((int) event.getX(), (int) event.getY());
                }
                Log.d(TAG,"Action Move");
                break;
            case MotionEvent.ACTION_UP:

                movingPlayer = false;
                Log.d(TAG,"Action Up");
                break;
        }
        return true;
//        return  super.onTouchEvent(event);
    }

    public void reset() {

        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(200, 350,75,Color.BLACK);
        movingPlayer = false;

    }

    public void update(){
        if(!gameOver) {
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOvertime = System.currentTimeMillis();
            }
        }
        //如果超过2sec 则reset
        if(gameOver){
            if( System.currentTimeMillis() - gameOvertime >= 2000  ){
                reset();
                gameOver = false;
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint . setColor(Color.MAGENTA);
            drawCenterText(canvas,paint,"Game Over");
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

}
