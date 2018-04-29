package com.example.android.gamesbola;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

public class MainPresenter {
    /* coefficient of restitution */
    private static final float COR = 0.7f;
    private float frameTime = 0.666f;
    private float xVelocity, yVelocity,xAccelerationOld,yAccelerationOld;

    public MainPresenter() {
    }

    public Bola[] newGames(ImageView ivBoard) {
        xVelocity = 0f;
        yVelocity = 0f;
        xAccelerationOld = 0f;
        yAccelerationOld = 0f;

        float x1 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
        float y1 = (float) (Math.random() * (ivBoard.getHeight() / 2) + 100);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setAntiAlias(true);
        Bola lubang = new Bola(x1, y1, paint1, 85f);

        float x2 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
        float y2 = (float) (Math.random() * (ivBoard.getHeight() / 2) + (ivBoard.getHeight() / 2) - 100);

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setAntiAlias(true);
        Bola bola = new Bola(x2, y2, paint2, 85f);
        return new Bola[]{lubang, bola};
    }
    public void updateBola(ImageView papan, Bola bola ,float xAcceleration, float yAcceleration) {
        float batas = 20f;
        if(xAcceleration == 0f){
            if(xVelocity > 0f){
                xVelocity -= frameTime;
                if(xVelocity < 0f){
                    xVelocity = 0f;
                }
            }else if(xVelocity < 0f){
                xVelocity += frameTime;
                if(xVelocity > 0f){
                    xVelocity = 0f;
                }
            }
        }else{
            if(xAcceleration < 0f){
                xVelocity = (xVelocity - frameTime < -batas)? -batas : xVelocity - frameTime;
            }else if(xAcceleration > 0f){
                xVelocity = (xVelocity + frameTime > batas)? batas : xVelocity + frameTime;
            }
        }
        if(yAcceleration == 0f){
            if(yVelocity> 0f){
                yVelocity -= frameTime;
                if(yVelocity < 0f){
                    yVelocity = 0f;
                }
            }else if(yVelocity < 0f){
                yVelocity += frameTime;
                if(yVelocity > 0f){
                    yVelocity = 0f;
                }
            }
        }else{
            if(yAcceleration < 0f){
                yVelocity = (yVelocity + frameTime > batas)?batas : yVelocity + frameTime;
            }else if(yAcceleration > 0f){
                yVelocity = (yVelocity - frameTime < -batas)?-batas : yVelocity - frameTime;
            }

        }

        bola.setX(bola.getX() + xVelocity);
        bola.setY(bola.getY() + yVelocity);
        checkCollision(papan,bola);

    }

//    public void updateBola(ImageView papan, Bola bola ,float xAcceleration, float yAcceleration, long timeStamp) {
//        xVelocity += -xAcceleration * frameTime;
//        yVelocity += yAcceleration * frameTime;
//
//        bola.setX(bola.getX() - xVelocity * frameTime);
//        bola.setY(bola.getY() - yVelocity * frameTime);
//        checkCollision(papan,bola);
//
//    }
    private void checkCollision(ImageView papan, Bola bola){
        if(bola.getX()+bola.getRadius() > papan.getWidth()){
            bola.setX(papan.getWidth()-bola.getRadius());
            xVelocity = -xVelocity * COR;
        }else if(bola.getX() < papan.getX()){
            bola.setX(papan.getX());
            xVelocity = -xVelocity * COR;
        }
        if(bola.getY()+bola.getRadius() > papan.getHeight()){
            bola.setY(papan.getHeight()-bola.getRadius());
            yVelocity = -yVelocity * COR;
        }else if(bola.getY() < papan.getY()){
            bola.setY(papan.getY());
            yVelocity = -yVelocity * COR;
        }
    }
}
