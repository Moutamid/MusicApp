package com.moutimid.musicapp;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> songList;
    private ArrayList<String> songnameList;
    private ArrayList<String> songdetailsList;
    private int[] imageResources; // Array to hold image resources corresponding to each song

    public SongAdapter(Context context, ArrayList<String> songList, ArrayList<String> songnameList, ArrayList<String> songdetailsList, int[] imageResources) {
        this.context = context;
        this.songList = songList;
        this.songnameList = songnameList;
        this.songdetailsList = songdetailsList;
        this.imageResources = imageResources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String songName = songList.get(position);
        String songNameList = songnameList.get(position);
        String details = songdetailsList.get(position);
        int imageResource = imageResources[position]; // Get corresponding image resource for the song
        holder.songNameTextView.setText(songNameList);
        holder.songImageView.setImageResource(imageResource);
        holder.song_details_text_view.setText(details);
        holder.itemView.setOnClickListener(view -> {

            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putStringArrayListExtra("playlist", songList);
            intent.putExtra("position", position);
            intent.putExtra("name", songNameList);
            intent.putExtra("details", details);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songNameTextView, song_details_text_view;
        ImageView songImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_name_text_view);
            song_details_text_view = itemView.findViewById(R.id.song_details_text_view);
            songImageView = itemView.findViewById(R.id.song_image_view);
        }
    }
}
