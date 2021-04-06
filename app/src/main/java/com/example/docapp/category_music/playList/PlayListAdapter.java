package com.example.docapp.category_music.playList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.R;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlaylistViewholder> implements Filterable {
    ArrayList<PlayListModel> mPlayListArrayList;
    ArrayList<PlayListModel> mPlayListArrayListFull;


    private OnItemClickListener mListner;
    private OnItemLongClickListener mLongClickListener;

    private static final String TAG = "PlayListAdapter";


    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        mListner = listner;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public PlayListAdapter(ArrayList<PlayListModel> playListArrayList) {
        mPlayListArrayList = playListArrayList;
        mPlayListArrayListFull = new ArrayList<>(mPlayListArrayList);
    }

    @NonNull
    @Override
    public PlaylistViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_playlist_row, parent, false);
        return new PlaylistViewholder(view, mListner, mLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewholder holder, int position) {

        holder.mSongName.setText(mPlayListArrayList.get(position).getSongName());
        holder.mSongCount.setText(mPlayListArrayList.get(position).getSongLikeCount());


    }

    @Override
    public int getItemCount() {
        return mPlayListArrayList.size();
    }

    //TODo/....... impliment other
    @Override
    public Filter getFilter() {
        return songListFilter;
    }

    private Filter songListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PlayListModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mPlayListArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PlayListModel model : mPlayListArrayListFull) {

                    if (model.getSongName().toLowerCase().contains(filterPattern)) {

                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            mPlayListArrayList.clear();
            mPlayListArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class PlaylistViewholder extends RecyclerView.ViewHolder {

        public TextView mSongName;
        public TextView mSongCount;
        private View mView;

        public PlaylistViewholder(@NonNull View itemView, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener) {
            super(itemView);

            mView = itemView;
            mSongName = mView.findViewById(R.id.id_PlayListSongName);
            mSongCount = mView.findViewById(R.id.id_songListCount);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemLongClickListener.OnItemLongClick(getAdapterPosition());
                    return false;
                }
            });

        }
    }
}
