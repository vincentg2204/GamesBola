package com.example.android.gamesbola;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

public class MainPresenter {
    /* coefficient of restitution */
    private static final float COR = 0.7f;
    private float frameTime = 0.666f;
    private float xVelocity, yVelocity, xAccelerationOld, yAccelerationOld;
    private int waktu;
    private MainActivity mainActivity;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Bola[] newGames(ImageView ivBoard) {
        waktu = 30;
        xVelocity = 0f;
        yVelocity = 0f;
        xAccelerationOld = 0f;
        yAccelerationOld = 0f;

        float x1 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
//        float y1 = (float) (Math.random() * (ivBoard.getHeight() / 2) + 100);
        float y1 = 200;

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        Bola lubang = new Bola(x1, y1, paint1, 85f);

        float x2 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
//        float y2 = (float) (Math.random() * (ivBoard.getHeight() / 2) + (ivBoard.getHeight() / 2) - 100);
        float y2 = ivBoard.getHeight() - 200;

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        Bola bola = new Bola(x2, y2, paint2, 70f);
        return new Bola[]{lubang, bola};
    }

    public boolean isInside(Bola bola, Bola lobang) {
//        boolean a = (bola.getX() - bola.getRadius()) > (lobang.getX()-lobang.getRadius());
//        boolean b = (bola.getY() - bola.getRadius()) > (lobang.getY()-lobang.getRadius());
//        boolean c = (bola.getX() + bola.getRadius()) < (lobang.getX()+lobang.getRadius());
//        boolean d = (bola.getY() + bola.getRadius()) < (lobang.getY()+lobang.getRadius());
        return (bola.getX() - bola.getRadius()) > (lobang.getX() - lobang.getRadius()) &&
                (bola.getY() - bola.getRadius()) > (lobang.getY() - lobang.getRadius()) &&
                (bola.getX() + bola.getRadius()) < (lobang.getX() + lobang.getRadius()) &&
                (bola.getY() + bola.getRadius()) < (lobang.getY() + lobang.getRadius());
//        Log.d("ISINSIDE","KIRI: "+a+"\nATAS: "+b+"\nKANAN: "+c+"\nBAWAH: "+d);
//        return a && b && c && d;
    }

    public void updateBola(ImageView papan, Bola bola, float xAcceleration, float yAcceleration) {
        float batas = 20f;
        if (xAcceleration == 0f) {
            if (xVelocity > 0f) {
                xVelocity -= frameTime;
                if (xVelocity < 0f) {
                    xVelocity = 0f;
                }
            } else if (xVelocity < 0f) {
                xVelocity += frameTime;
                if (xVelocity > 0f) {
                    xVelocity = 0f;
                }
            }
        } else {
            if (xAcceleration < 0f) {
                xVelocity = (xVelocity - frameTime < -batas) ? -batas : xVelocity - frameTime;
            } else if (xAcceleration > 0f) {
                xVelocity = (xVelocity + frameTime > batas) ? batas : xVelocity + frameTime;
            }
        }
        if (yAcceleration == 0f) {
            if (yVelocity > 0f) {
                yVelocity -= frameTime;
                if (yVelocity < 0f) {
                    yVelocity = 0f;
                }
            } else if (yVelocity < 0f) {
                yVelocity += frameTime;
                if (yVelocity > 0f) {
                    yVelocity = 0f;
                }
            }
        } else {
            if (yAcceleration < 0f) {
                yVelocity = (yVelocity + frameTime > batas) ? batas : yVelocity + frameTime;
            } else if (yAcceleration > 0f) {
                yVelocity = (yVelocity - frameTime < -batas) ? -batas : yVelocity - frameTime;
            }

        }

        bola.setX(bola.getX() + xVelocity);
        bola.setY(bola.getY() + yVelocity);
        checkCollision(papan, bola);

    }

    private void checkCollision(ImageView papan, Bola bola) {
        if (bola.getX() + bola.getRadius() > papan.getWidth()) {
            bola.setX(papan.getWidth() - bola.getRadius());
            xVelocity = -xVelocity * COR;
        } else if (bola.getX() - bola.getRadius() < papan.getX()) {
            bola.setX(papan.getX() + bola.getRadius());
            xVelocity = -xVelocity * COR;
        }
        if (bola.getY() + bola.getRadius() > papan.getHeight()) {
            bola.setY(papan.getHeight() - bola.getRadius());
            yVelocity = -yVelocity * COR;
        } else if (bola.getY() - bola.getRadius() < papan.getY()) {
            bola.setY(papan.getY() + bola.getRadius());
            yVelocity = -yVelocity * COR;
        }
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
        mainActivity.setTime();
    }

    public String getWaktu() {
        String result = "00:";
        if (waktu < 10) {
            result += "0" + waktu;
        } else {
            result += waktu;
        }
        return result;
    }

    public void setGameOver(boolean gameOver) {
        mainActivity.setGameOver(gameOver);
    }
}
