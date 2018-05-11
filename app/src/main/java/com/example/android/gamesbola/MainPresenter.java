package com.example.android.gamesbola;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
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
    private float frameTime = 0.666f;
    private float xVelocity, yVelocity;
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
            ){
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
        xVelocity = 0f;
        yVelocity = 0f;

        float x1 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
//        float y1 = (float) (Math.random() * (ivBoard.getHeight() / 2) + 100);
        float y1 = 200;

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setAntiAlias(true);
        Bola lubang = new Bola(x1, y1, paint1, 85f);

        float x2 = (float) (Math.random() * (ivBoard.getWidth() - 300) + 100);
//        float y2 = (float) (Math.random() * (ivBoard.getHeight() / 2) + (ivBoard.getHeight() / 2) - 100);
        float y2 = ivBoard.getHeight() - 200;

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
        Bola bola = new Bola(x2, y2, paint2, radius);
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

    public int getWaktu() {
        return waktu;
    }

    public void setGameOver(boolean gameOver) {
        mainActivity.setGameOver(gameOver);
    }
}
