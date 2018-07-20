package com.yang.liudongnan.mygame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Arrays;

public class OrientationData implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    private float[] accelOutput;
    private float[] magOutput;

//    private float[] orientation = new float[3];

    private float[] orientation;
    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation() {
        return startOrientation;
    }
    public void newGame(){
        startOrientation  = null;
    }

    public OrientationData() {
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        orientation = new float[3];

    }

    public void register(){
        ///设置delay决定多久再呼一次
        manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this,magnometer,Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void pause(){
        manager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelOutput = sensorEvent.values;
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magOutput = sensorEvent.values;
    }
//        System.out.println("accelerometer: "+Arrays.toString(accelOutput));
//        System.out.println("magnetic field: "+Arrays.toString(magOutput));
        if(accelOutput != null && magOutput != null){
            float [] R = new float[9];
            float [] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,accelOutput,magOutput);
            if(success){
                SensorManager.getOrientation(R,orientation);

                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation,0,startOrientation,0,orientation.length);
                }
            }
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
