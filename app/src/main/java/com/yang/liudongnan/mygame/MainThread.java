package com.yang.liudongnan.mygame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    //帧数部分
    public static  final int MAX_FPS = 100;
    private double averageFPS;

    private SurfaceHolder surfaceHolder;
    //游戏页面
    private GamePanel gamePanel;

    private  boolean running ;
    //背景板
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime ;
        long timeMillis = 1000/MAX_FPS;
        long waitTime ;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (running){
            startTime = System.nanoTime();
            canvas = null ;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime()-startTime)/10000000;
            waitTime = targetTime-timeMillis;

            try {
                if(waitTime > 0){
                    this.sleep(waitTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //计算渲染时间 输出平均帧数
            totalTime += System.nanoTime() - startTime;
            frameCount ++ ;
             if(frameCount == MAX_FPS){
                 averageFPS = 1000/((totalTime/frameCount)/1000000);
                 frameCount = 0;
                 totalTime = 0;
                 System.out.println(averageFPS);
             }


        }



    }



}
