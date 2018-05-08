package com.example.android.gamesbola;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterHighscore extends BaseAdapter {

    private ArrayList<Integer> listScore;
    private MainActivity mainActivity;
    private ViewHolder vh;

    public AdapterHighscore(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.listScore = new ArrayList<>();
    }

    public ArrayList<Integer> getListScore() {
        return listScore;
    }

    public void setListScore(ArrayList<Integer> listScore) {
        this.listScore = listScore;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public ViewHolder getVh() {
        return vh;
    }

    public void setVh(ViewHolder vh) {
        this.vh = vh;
    }

    public void sortListScore(){
        Collections.sort(listScore);
        Collections.reverse(listScore);
    }

    @Override
    public int getCount() {
        return listScore.size();
    }

    @Override
    public Integer getItem(int position) {
        return listScore.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(this.mainActivity).inflate(R.layout.isi_highscore,parent,false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }
//        for (int i = 0;i<listScore.size();i++){
//            vh.update(i+1, listScore.get(i));
//        }
        vh.update(position+1, getItem(position));
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_urutan,tv_highscore;

        public ViewHolder(View view){
            this.tv_urutan = view.findViewById(R.id.label_urutan);
            this.tv_highscore = view.findViewById(R.id.label_score);
        }

        private void update(int urutan,int highscore){
            this.tv_urutan.setText(urutan+"");
            this.tv_highscore.setText(highscore+"");
        }
    }
}
