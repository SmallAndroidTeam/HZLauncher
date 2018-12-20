package com.ofilm.android.hozonlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ofilm.android.hozonlauncher.R;

import java.util.ArrayList;
import java.util.List;

import of.media.hz.bean.Music;


public class MusicListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Music> musicList = new ArrayList<Music>();


    public MusicListAdapter(Context mContext, List<Music> list) {
        this.mContext = mContext;
        this.musicList = list;
    }

    @Override
    public int getCount() {
        if (musicList != null) {
            return musicList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (musicList != null) {
            return musicList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.music_list_item, null);
            holder.musicNumberTx = (TextView)convertView.findViewById(R.id.music_number_id);
            holder.musicNameTx = (TextView)convertView.findViewById(R.id.music_name_id);
            holder.musicSingerTx = (TextView)convertView.findViewById(R.id.music_singer_id);
            holder.musicAlbumTx = (TextView)convertView.findViewById(R.id.music_album_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Music musicItem = (Music)getItem(position);
        holder.musicNumberTx.setText(String.valueOf(musicItem.getId()));
        holder.musicNameTx.setText(musicItem.getTitle());
        holder.musicSingerTx.setText(musicItem.getArtist());
        //holder.musicAlbumTx.setText(musicItem.getArtist());
        return convertView;
    }

    static class ViewHolder {
        TextView musicNumberTx;
        TextView musicNameTx;
        TextView musicSingerTx;
        TextView musicAlbumTx;
    }
}
