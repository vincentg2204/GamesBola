package com.example.android.gamesbola;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainPresenter {
    /* coefficient of restitution */
    private static final float COR = 0.7f;
    private float frameTime = 0.33f;
    private float xAccelerationOld, yAccelerationOld;
    private int waktu;
    private MainActivity mainActivity;
    private ArrayList<Integer> listOfScore = new ArrayList<>();

    private Gson gson = new Gson();
    private RequestQueue queue;

    private int difficulty = 1;
    private int curColor = 1;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        queue = Volley.newRequestQueue(mainActivity);
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getCurColor() {
        return curColor;
    }

    public void setCurColor(int curColor) {
        this.curColor = curColor;
    }

    public ArrayList<Integer> getListOfScore() {
        return listOfScore;
    }

    public void updateListOfScore() {
        listOfScore.clear();
        for (int i = 1; i <= 4; i++) {
            getResponse("http://pab.labftis.net/api.php?api_key=2015730021&page=" + i);
        }
    }

    public void updateListOfScore(int newScore) {
        listOfScore.add(newScore);
        setListOfScore(listOfScore);
        updateWebService();
    }

    public void setListOfScore(ArrayList<Integer> listOfScore) {
        this.listOfScore = listOfScore;
        Collections.sort(this.listOfScore);
        Collections.reverse(this.listOfScore);
        this.listOfScore.remove(this.listOfScore.size() - 1);
    }

    public void updateWebService() {
        int page = 1;
        for (int i = 1; i <= this.listOfScore.size(); i++) {
            if (i != 1 && i % 3 == 1) {
                page++;
            }
            String url = "http://pab.labftis.net/api.php?" + page;
            final int finalI = i - 1;
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("ResponsePost", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", "ERROR");
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("api_key", "2015730021");
                    params.put("order", finalI + 1 + "");
                    params.put("value", listOfScore.get(finalI) + "");

                    return params;
                }
            };
            queue.add(postRequest);
        }
    }

    private void getResponse(String url) {
        Log.d("getResponse", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ONRESPONSE", response);
                Value[] values = processResult(response);
                for (int i = 0; i < values.length; i++) {
                    Log.d("getResponse", values[i].getValue() + "");
                    listOfScore.add(Integer.parseInt(values[i].getValue()));
                }
                mainActivity.updateHighScore(listOfScore);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ONERRORRESPONSE", "ERROR");
            }
        });
        queue.add(stringRequest);
    }

    private Value[] processResult(String content) {
        Log.d("result_string", content);
        try {
            JSONObject json = new JSONObject(content);
            JSONArray data = json.getJSONArray("data");
            return this.gson.fromJson(data.toString(), Value[].class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("ResourceAsColor")
    public Bola[] newGames(ImageView ivBoard) {
        waktu = 30;
        xAccelerationOld = 0f;
        yAccelerationOld = 0f;

        float xLobang = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
        float yLobang = 200;

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setAntiAlias(true);
        Bola lubang = new Bola(xLobang, yLobang, paint1, 85f);

        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        if (curColor == 1) {
            paint2.setColor(mainActivity.getResources().getColor(R.color.type1));
        } else if (curColor == 2) {
            paint2.setColor(mainActivity.getResources().getColor(R.color.type2));
        } else if (curColor == 3) {
            paint2.setColor(mainActivity.getResources().getColor(R.color.type3));
        }

        float radius = 0;
        if (difficulty == 1) {
            //easy
            radius = 65f;
        } else if (difficulty == 2) {
            //medium
            radius = 75f;
        } else if (difficulty == 3) {
            //hard
            radius = 80f;
        }
        float xBola1 = (float) (Math.random() * (ivBoard.getWidth()/2 - 2*radius) + radius);
        float yBola1 = ivBoard.getHeight() - 200;

        Bola bola1 = new Bola(xBola1, yBola1, paint2, radius);

        float xBola2 = (float) (Math.random() * (ivBoard.getWidth() - 2*radius) + (ivBoard.getWidth()/2) + radius);
        float yBola2 = ivBoard.getHeight() - 200;

        Bola bola2 = new Bola(xBola2, yBola2, paint2, radius);
        return new Bola[]{lubang, bola1, bola2};
    }

    public boolean isInside(Bola bola, Bola lobang) {
        return (bola.getX() - bola.getRadius()) > (lobang.getX() - lobang.getRadius()) &&
                (bola.getY() - bola.getRadius()) > (lobang.getY() - lobang.getRadius()) &&
                (bola.getX() + bola.getRadius()) < (lobang.getX() + lobang.getRadius()) &&
                (bola.getY() + bola.getRadius()) < (lobang.getY() + lobang.getRadius());
    }

    public void updateBola(ImageView papan, Bola[] bola, float xAcceleration, float yAcceleration) {
        for (int i = 0; i < bola.length; i++) {
            float batas = 20f;
            float kecepatanLambat = 0.1f;
            if (xAcceleration == 0f) {
                if (bola[i].getxVelo() > 0f) {
                    bola[i].setxVelo(bola[i].getxVelo() - kecepatanLambat);
                    if (bola[i].getxVelo() < 0f) {
                        bola[i].setxVelo(0f);
                    }
                } else if (bola[i].getxVelo() < 0f) {
                    bola[i].setxVelo(bola[i].getxVelo() + kecepatanLambat);
                    if (bola[i].getxVelo() > 0f) {
                        bola[i].setxVelo(0f);
                    }
                }
            } else {
                if (xAcceleration < 0f) {
                    bola[i].setxVelo(((bola[i].getxVelo() - frameTime) < -batas) ? -batas : bola[i].getxVelo() - Math.abs(xAcceleration * frameTime));
                } else if (xAcceleration > 0f) {
                    bola[i].setxVelo((bola[i].getxVelo() + frameTime > batas) ? batas : bola[i].getxVelo() + Math.abs(xAcceleration * frameTime));
                }
            }
            if (yAcceleration == 0f) {
                if (bola[i].getyVelo() > 0f) {
                    bola[i].setyVelo(bola[i].getyVelo() - kecepatanLambat);
                    if (bola[i].getyVelo() < 0f) {
                        bola[i].setyVelo(0f);
                    }
                } else if (bola[i].getyVelo() < 0f) {
                    bola[i].setyVelo(bola[i].getyVelo() + kecepatanLambat);
                    if (bola[i].getyVelo() > 0f) {
                        bola[i].setyVelo(0f);
                    }
                }
            } else {
                if (yAcceleration < 0f) {
                    bola[i].setyVelo((bola[i].getyVelo() + frameTime > batas) ? batas : bola[i].getyVelo() + Math.abs(yAcceleration * frameTime));
                } else if (yAcceleration > 0f) {
                    bola[i].setyVelo((bola[i].getyVelo() - frameTime < -batas) ? -batas : bola[i].getyVelo() - Math.abs(yAcceleration * frameTime));
                }
            }
            bola[i].setX(bola[i].getX() + bola[i].getxVelo());
            bola[i].setY(bola[i].getY() + bola[i].getyVelo());
        }
        checkCollision(papan, bola);
    }
    private boolean isInside(Bola[] bola){
        for(Bola b : bola){
            if(b.isInside()){
                return true;
            }
        }
        return false;
    }

    private void checkCollision(ImageView papan, Bola[] bola) {
        for (Bola b : bola) {
            if (b.getX() + b.getRadius() > papan.getWidth()) {
                b.setX(papan.getWidth() - b.getRadius());
                b.setxVelo(-b.getxVelo() * COR);
            } else if (b.getX() - b.getRadius() < papan.getX()) {
                b.setX(papan.getX() + b.getRadius());
                b.setxVelo(-b.getxVelo() * COR);
            }
            if (b.getY() + b.getRadius() > papan.getHeight()) {
                b.setY(papan.getHeight() - b.getRadius());
                b.setyVelo(-b.getyVelo() * COR);
            } else if (b.getY() - b.getRadius() < papan.getY()) {
                b.setY(papan.getY() + b.getRadius());
                b.setyVelo(-b.getyVelo() * COR);
            }
        }
        if (!isInside(bola)) {
            if(checkCollision(bola)){
                if(bola[0].getX()+bola[0].getRadius() >= bola[1].getX()-bola[1].getRadius()){
                    bola[0].setX(bola[0].getX()-1);
                    bola[1].setX(bola[1].getX()+1);
                    for(Bola b : bola) {
                        b.setxVelo(-b.getxVelo() * COR);
//                        b.setyVelo(-b.getyVelo() * COR);
                    }
                }
                if(bola[1].getX()+bola[1].getRadius() <= bola[0].getX()-bola[0].getRadius()){
                    bola[0].setX(bola[0].getX()+1);
                    bola[1].setX(bola[1].getX()-1);
                    for(Bola b : bola) {
                        b.setxVelo(-b.getxVelo() * COR);
//                        b.setyVelo(-b.getyVelo() * COR);
                    }
                }

            }
        }
    }
    private boolean checkCollision(Bola[] bola){
        Rect rB1 = new Rect((int)(bola[0].getX()-bola[0].getRadius()),
                (int)(bola[0].getY()-bola[0].getRadius()),
                (int)(bola[0].getX()+bola[0].getRadius()),
                (int)(bola[0].getY()+bola[0].getRadius()));
        Rect rB2 = new Rect((int)(bola[1].getX()-bola[1].getRadius()),
                (int)(bola[1].getY()-bola[1].getRadius()),
                (int)(bola[1].getX()+bola[1].getRadius()),
                (int)(bola[1].getY()+bola[1].getRadius()));
        return rB1.intersect(rB2);
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
        mainActivity.setTime();
    }

    public int getWaktu() {
        return waktu;
    }

    public void setGameOver(boolean gameOver) {
        mainActivity.setGameOver(gameOver);
    }
}
