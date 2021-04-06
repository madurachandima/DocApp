package com.example.docapp.Category_consulter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ConsulterAdapter extends RecyclerView.Adapter<ConsulterAdapter.ConsulterViewHolder> implements Filterable {

    ArrayList<ConsulterModel> mModelArrayList;
    ArrayList<ConsulterModel> mModelArrayListFull;

    private static final String TAG = "ConsulterAdapter";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setonItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ConsulterAdapter(ArrayList<ConsulterModel> modelArrayList) {
        mModelArrayList = modelArrayList;
        mModelArrayListFull = new ArrayList<>(mModelArrayList);
    }


    @NonNull
    @Override
    public ConsulterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consulter_row, parent, false);
        return new ConsulterViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ConsulterViewHolder holder, int position) {

        holder.mConsulterName.setText(mModelArrayList.get(position).getConName());
        holder.mConsulterLocation.setText(mModelArrayList.get(position).getConLocation());
        Picasso.get().load(mModelArrayList.get(position).getImageURL()).into(holder.mImageUrl);
    }

    @Override
    public int getItemCount() {
        return mModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return profileListFilter;
    }

    private Filter profileListFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ConsulterModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(mModelArrayListFull);

            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ConsulterModel model : mModelArrayListFull) {

                    if (model.getConName().toLowerCase().contains(filterPattern)) {

                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mModelArrayList.clear();
            mModelArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ConsulterViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageUrl;
        public TextView mConsulterName;
        public TextView mConsulterLocation;
        private View mView;

        public ConsulterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mView = itemView;

            mImageUrl = mView.findViewById(R.id.id_consulter_image);
            mConsulterLocation = mView.findViewById(R.id.id_consulter_location);
            mConsulterName = mView.findViewById(R.id.id_consulter_name);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
