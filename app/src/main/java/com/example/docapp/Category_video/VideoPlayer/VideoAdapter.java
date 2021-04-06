package com.example.docapp.Category_video.VideoPlayer;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.R;


import java.util.ArrayList;
import java.util.HashMap;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    ArrayList<VideoModel> arrayList;

    private VideoAdapter.OnItemClickListener mListner;
    ;


    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(VideoAdapter.OnItemClickListener listner) {
        mListner = listner;
    }

    public VideoAdapter(ArrayList<VideoModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_row, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view, mListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        //  holder.mSongName.setText(mPlayListArrayList.get(position).getSongName());
        holder.name.setText(arrayList.get(position).getmVideoName());

        try {
            Bitmap bitmap;
            bitmap = retriveVideoFrameFromVideo(arrayList.get(position).getmVideoURL());
            if (bitmap != null) {
                holder.imageView.setImageBitmap(bitmap);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView count;

        public VideoViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.id_video_thumbnail);
            name = itemView.findViewById(R.id.id_video_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            throw new Throwable("Exception in retriveVVideo" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }

        }
        return bitmap;
    }
}
