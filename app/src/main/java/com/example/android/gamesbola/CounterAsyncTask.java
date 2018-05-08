package com.example.android.gamesbola;

import android.os.AsyncTask;

public class CounterAsyncTask extends AsyncTask<Integer, Integer, String> {
    private int countNumber;
    private MainPresenter presenter;

    public CounterAsyncTask(MainPresenter presenter) {
        this.countNumber = 30;
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(Integer... integers) {
        this.countNumber = integers[0];
        while(countNumber != 0){
            countNumber--;
            publishProgress(countNumber);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isCancelled()) {
                break;
            }
        }
        return countNumber + "";
    }

    @Override
    protected void onPostExecute(String result) {
        //TODO WAKTU HABIS,GAME OVER
        presenter.setGameOver(true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int waktu = values[0];
        presenter.setWaktu(waktu);
    }
}
