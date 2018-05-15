package com.example.android.gamesbola;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentGames extends Fragment implements SensorEventListener, View.OnClickListener {
    private ImageView ivBoard;
    private Canvas canvas;
    private Bitmap bitmap;

    private Button btnNew, btnExit;
    private TextView tvWaktu;
    private boolean gameOver = false;
    private ImageButton ibStartPause;

    private MainPresenter presenter;

    private SensorManager mSensorManager;
    private Sensor mSensorAccelerometer, mSensorMagnetometer;
    private Display mDisplay;
    private float[] mAccelerometerData, mMagnetometerData;

    private Bola bola1,bola2,lobang;
    private MainActivity ctx;
    private CounterAsyncTask cat;

    private TextView tvScore;
    private int currentScore = 0;

    private int currentWaktu = 61;
    private int flag = 0;

    private int jumlahMasuk = 0;
    private boolean isPaused = false;

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
        ibStartPause = view.findViewById(R.id.ib_start_pause);
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
        ibStartPause.setOnClickListener(this);

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
            mSensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!isPaused) {
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
            if (canvas != null && lobang != null && bola1 != null) {
                if (!gameOver) {
                    updateBall(ivBoard,
                            (Math.abs(orientationValues[1]) < 0.05f) ? 0f : orientationValues[1],
                            (Math.abs(orientationValues[2]) < 0.05f) ? 0f : orientationValues[2]);
                    canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    canvas.drawCircle(lobang.getX(), lobang.getY(), lobang.getRadius(), lobang.getPaint());
                    if (!bola1.isInside()) {
                        canvas.drawCircle(bola1.getX(), bola1.getY(), bola1.getRadius(), bola1.getPaint());
                    }
                    if (!bola2.isInside()) {
                        canvas.drawCircle(bola2.getX(), bola2.getY(), bola2.getRadius(), bola2.getPaint());
                    }
                    ivBoard.invalidate();

                    if (presenter.isInside(bola1, lobang)) {
                        bola1.setInside(true);
                    }
                    if (presenter.isInside(bola2, lobang)) {
                        bola2.setInside(true);
                    }
                    nextLevel();
                } else {
                    if (flag == 0) {
                        btnNew.setText("TRY AGAIN");
                        presenter.updateListOfScore(currentScore);
                        flag = 1;
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void nextLevel(){
        if(bola1.isInside() && bola2.isInside()){
            currentScore += (int) (currentWaktu * 3.3);
            init();
        }
    }

    private void newGames(){
        currentScore = 0;
        init();
    }
    private void init(){
        jumlahMasuk = 0;
        flag = 0;
        gameOver = false;
        isPaused = false;
        currentWaktu = 61;
        btnNew.setText("NEW");

        Bola[] obj = presenter.newGames(ivBoard);
        lobang = obj[0];
        bola1 = obj[1];
        bola2 = obj[2];

        canvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        canvas.drawCircle(lobang.getX(), lobang.getY(), lobang.getRadius(), lobang.getPaint());
        canvas.drawCircle(bola1.getX(), bola1.getY(), bola1.getRadius(), bola1.getPaint());

        ivBoard.invalidate();

        if (cat != null) {
            cat.cancel(true);
        }
        cat = new CounterAsyncTask(presenter);
        cat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentWaktu);

        tvScore.setText("Score: " + currentScore);
    }

    @Override
    public void onClick(View v) {
        if(v == btnNew){
            newGames();
        }else if(v == btnExit){
            gameOver = false;
            flag = 0;
            this.ctx.onBackPressed();
        }else if(v == ibStartPause){
            if(!isPaused){
                isPaused = true;
                cat.cancel(true);
                ibStartPause.setImageResource(R.drawable.ic_start);
            }else{
                isPaused = false;
                cat = new CounterAsyncTask(presenter);
                cat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentWaktu);
                ibStartPause.setImageResource(R.drawable.ic_pause);
            }
        }
    }

    private void updateBall(ImageView papan, float yAcceleration, float xAcceleration){
        Bola[] bolas = new Bola[]{bola1,bola2};
        presenter.updateBola(papan, bolas,xAcceleration,yAcceleration);
    }
    private void tampilanWaktu(){
        if(gameOver || currentWaktu < 10) {
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
