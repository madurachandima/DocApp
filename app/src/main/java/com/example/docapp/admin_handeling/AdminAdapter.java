package com.example.docapp.admin_handeling;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.docapp.Category_consulter.ConsulterAdapter;
import com.example.docapp.R;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {




    ArrayList<AdminModel> mArrayList;
    private static final String TAG = "AdminAdapter";

    private AdminAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setonItemClickListener(AdminAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public AdminAdapter(ArrayList<AdminModel> arrayList) {
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_row, parent, false);
        AdminViewHolder viewHolder = new AdminViewHolder(view,mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.mNameCon.setText(mArrayList.get(position).getConName());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameCon;

        public AdminViewHolder(@NonNull View itemView, final OnItemClickListener itemClickListener ) {
            super(itemView);
            mNameCon = itemView.findViewById(R.id.id_request_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });


        }
    }
}
