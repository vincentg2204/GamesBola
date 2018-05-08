package com.example.android.gamesbola;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
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

public class FragmentGames extends Fragment implements SensorEventListener, View.OnClickListener {
    private ImageView ivBoard;
    private Canvas canvas;
    private Bitmap bitmap;

    private Button btnNew, btnExit;
    private TextView tvWaktu;
    private boolean gameOver = false;

    private MainPresenter presenter;

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer, mSensorMagnetometer;
    private Display mDisplay;
    private float[] mAccelerometerData, mMagnetometerData;

    private Bola bola,lobang;
    private MainActivity ctx;
    private CounterAsyncTask cat;

    private TextView tvScore;
    private int currentScore = 0;

    private int currentWaktu = 31;

    public FragmentGames() {
    }
    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }

    public static FragmentGames newInstance(MainActivity mainActivity,MainPresenter presenter, String title) {
        FragmentGames fragment = new FragmentGames();
        fragment.setMainActivity(mainActivity);
        fragment.setMainPresenter(presenter);
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMainActivity(MainActivity ui) {
        this.ctx = ui;
    }
    public void setTextTVWaktu(int waktu){
        currentWaktu = waktu;
        String text = "00:";
        if(currentWaktu < 10){
            text += "0"+currentWaktu;
        }else{
            text += currentWaktu;
        }
        tvWaktu.setText(text);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        tvWaktu = view.findViewById(R.id.tv_waktu);
        btnNew = view.findViewById(R.id.new_btn);
        btnExit = view.findViewById(R.id.exit_btn);

        tvScore = view.findViewById(R.id.tvScore);

        ivBoard = view.findViewById(R.id.iv_board);
        ivBoard.post(new Runnable() {
            @Override
            public void run() {
                bitmap = Bitmap.createBitmap(ivBoard.getWidth(),ivBoard.getHeight(), Bitmap.Config.ARGB_8888);
                ivBoard.setImageBitmap(bitmap);
                canvas = new Canvas(bitmap);
                newGames();
            }
        });

        btnNew.setOnClickListener(this);
        btnExit.setOnClickListener(this);

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
        tampilanWaktu();
        if(canvas != null && lobang != null && bola != null){
            if(!gameOver) {
                updateBall(ivBoard,
                        (Math.abs(orientationValues[1]) < 0.05f) ? 0f : orientationValues[1],
                        (Math.abs(orientationValues[2]) < 0.05f) ? 0f : orientationValues[2]);
                canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                canvas.drawCircle(lobang.getX(), lobang.getY(), lobang.getRadius(), lobang.getPaint());
                canvas.drawCircle(bola.getX(), bola.getY(), bola.getRadius(), bola.getPaint());
                ivBoard.invalidate();
                if (presenter.isInside(bola, lobang)) {
                    nextLevel();
                }
            }else{
                btnNew.setText("TRY AGAIN");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void nextLevel(){
        currentWaktu = 31;
        btnNew.setText("NEW");
        gameOver = false;
        currentScore += (int)(currentWaktu * 3.3);
        tvScore.setText("Score: "+currentScore);

        Bola[] obj = presenter.newGames(ivBoard);
        lobang = obj[0];
        bola = obj[1];

        canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        canvas.drawCircle(lobang.getX(),lobang.getY(),lobang.getRadius(), lobang.getPaint());
        canvas.drawCircle(bola.getX(),bola.getY(),bola.getRadius(), bola.getPaint());

        ivBoard.invalidate();

        if(cat != null) {
            cat.cancel(true);
        }
        cat = new CounterAsyncTask(presenter);
        cat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 31);
    }

    private void newGames(){
        currentWaktu = 31;
        btnNew.setText("NEW");
        gameOver = false;
        currentScore = 0;
        tvScore.setText("Score: "+currentScore);

        Bola[] obj = presenter.newGames(ivBoard);
        lobang = obj[0];
        bola = obj[1];

        canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        canvas.drawCircle(lobang.getX(),lobang.getY(),lobang.getRadius(), lobang.getPaint());
        canvas.drawCircle(bola.getX(),bola.getY(),bola.getRadius(), bola.getPaint());

        ivBoard.invalidate();

        if(cat != null) {
            cat.cancel(true);
        }
        cat = new CounterAsyncTask(presenter);
        cat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 31);

    }

    @Override
    public void onClick(View v) {
        if(v == btnNew){
            newGames();
        }else if(v == btnExit){
            this.ctx.onBackPressed();
        }
    }

    private void updateBall(ImageView papan, float yAcceleration, float xAcceleration){
        presenter.updateBola(papan,bola,xAcceleration,yAcceleration);
    }
    private void tampilanWaktu(){
        if(gameOver) {
            tvWaktu.setTextColor(getResources().getColor(R.color.red));
            tvWaktu.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            tvWaktu.setTextColor(getResources().getColor(R.color.black));
            tvWaktu.setTypeface(Typeface.DEFAULT);
        }
    }
    public void setMainPresenter(MainPresenter mainPresenter) {
        this.presenter = mainPresenter;
    }
}
