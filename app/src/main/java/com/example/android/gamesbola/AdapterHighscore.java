package com.example.android.gamesbola;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterHighscore extends BaseAdapter {

    public ArrayList<Integer> listUrutan,listScore;
    MainActivity mainActivity;
    ViewHolder vh;

    public AdapterHighscore(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.listScore = new ArrayList<>();

    }
    @Override
    public int getCount() {
        return listScore.size();
    }

    @Override
    public Object getItem(int position) {
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
        for (int i = 1;i<5;i++){
            vh.update(i,i+1);
        }
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
