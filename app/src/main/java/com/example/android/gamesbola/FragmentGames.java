package com.example.android.gamesbola;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.android.gamesbola.R.color.black;

public class FragmentGames extends Fragment implements SensorEventListener {
    private ImageView ivBoard;
    private Canvas canvas;
    private Bitmap bitmap;

    private Button btnNew, btnExit;
    private TextView tvWaktu;

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer, mSensorMagnetometer;
    private Display mDisplay;
    private float[] mAccelerometerData, mMagnetometerData;

    private Bola bola,lobang;
    private MainActivity ctx;

    public FragmentGames() {
    }

    public static FragmentGames newInstance(MainActivity mainActivity, String title) {
        FragmentGames fragment = new FragmentGames();
        fragment.setMainActivity(mainActivity);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMainActivity(MainActivity ui) {
        this.ctx = ui;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        tvWaktu = view.findViewById(R.id.tv_waktu);
        btnNew = view.findViewById(R.id.new_btn);
        btnExit = view.findViewById(R.id.exit_btn);

        ivBoard = view.findViewById(R.id.iv_board);
        ivBoard.post(new Runnable() {
            @Override
            public void run() {
                bitmap = Bitmap.createBitmap(ivBoard.getWidth(),ivBoard.getHeight(), Bitmap.Config.ARGB_8888);
                ivBoard.setImageBitmap(bitmap);
                canvas = new Canvas(bitmap);

                float x1 = (float) (Math.random()*(ivBoard.getWidth()-300) + 100);
                float y1 = (float) (Math.random()*(ivBoard.getHeight()/2) + 100);
                float x2 = (float) (Math.random()*(ivBoard.getWidth()-300) + 100);
                float y2 = (float) (Math.random()*(ivBoard.getHeight()/2)+(ivBoard.getHeight()/2)-100);

                Paint paint1 = new Paint();
                paint1.setColor(Color.BLACK);
                paint1.setAntiAlias(true);
                lobang = new Bola(x1,y1,paint1,85f);
                Paint paint2 = new Paint();
                paint2.setColor(Color.RED);
                paint2.setAntiAlias(true);
                bola = new Bola(x2,y2,paint2,85f);
                canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                canvas.drawCircle(lobang.getX(),lobang.getY(),lobang.getRadius(), lobang.getPaint());
                canvas.drawCircle(bola.getX(),bola.getY(),bola.getRadius(), bola.getPaint());

                ivBoard.invalidate();
            }
        });

        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
        mAccelerometerData = new float[9];
        mMagnetometerData = new float[9];


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sType = event.sensor.getType();
        switch (sType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = event.values.clone();
                break;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerData, mMagnetometerData);
        float[] rotationmatrixAdjusted = new float[9];
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                rotationmatrixAdjusted = rotationMatrix.clone();
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationmatrixAdjusted);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y, rotationmatrixAdjusted);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, rotationmatrixAdjusted);
                break;
        }

        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationmatrixAdjusted, orientationValues);
        }
        float azimuth = orientationValues[0];
        float pitch = orientationValues[1];
        float roll = orientationValues[2];
        if (Math.abs(pitch) < 0.05f) {
            pitch = 0;
        }
        if (Math.abs(roll) < 0.05f) {
            roll = 0;
        }
        if(canvas != null && lobang != null && bola != null){
            bola.setX(bola.getX()+roll*10);
            bola.setY(bola.getY()-pitch*10);

            canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
            canvas.drawCircle(lobang.getX(),lobang.getY(),lobang.getRadius(),lobang.getPaint());
            canvas.drawCircle(bola.getX(),bola.getY(),bola.getRadius(),bola.getPaint());
            ivBoard.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
