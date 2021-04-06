package com.example.docapp.Category_consulter.SingleConsulter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SingleConsulterAdapter extends RecyclerView.Adapter<SingleConsulterAdapter.SingleConsulterViewHolder> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "SingleConsulterAdapter";
    ArrayList<SingleConsulterModel> mArrayList;
    //   private OnItemClickListener mListener;
    private onItemLongClickLitner mLongClickLitner;

    public interface onItemLongClickLitner {
        void onLongClick(int position);
    }


    public void setonItemLongClickListener(onItemLongClickLitner longClickListener) {
        mLongClickLitner = longClickListener;
    }

    public SingleConsulterAdapter(ArrayList<SingleConsulterModel> arrayList) {
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public SingleConsulterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_consulter_row, parent, false);
        SingleConsulterViewHolder viewHolder = new SingleConsulterViewHolder(view, mLongClickLitner);
      //  notifyDataSetChanged();
        Log.d(TAG, "onCreateViewHolder: Invoke");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleConsulterViewHolder holder, int position) {

        String mCUrentUserID = mAuth.getCurrentUser().getUid();
        String mFeedbackUserID = mArrayList.get(position).getUserId();

        Log.d(TAG, "onBindViewHolder: " + mCUrentUserID);
        Log.d(TAG, "onBindViewHolder: " + mFeedbackUserID);

        if (mFeedbackUserID.equals(mCUrentUserID)) {

            holder.mFeedbackUserName.setText("You");

        } else {

            holder.mFeedbackUserName.setText(mArrayList.get(position).getUserName());
        }

        holder.mFeedback.setText(mArrayList.get(position).getFeedback());
        holder.mDate.setText(mArrayList.get(position).getDate());
       // notifyItemChanged(holder.getAdapterPosition());
        Log.d(TAG, "onBindViewHolder: Invoke..");
//        holder.mFeedbackUserName.setText(mArrayList.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public static class SingleConsulterViewHolder extends RecyclerView.ViewHolder {
        public TextView mDate;
        public TextView mFeedback;
        public TextView mFeedbackUserName;
        private View mView;

        public SingleConsulterViewHolder(@NonNull View itemView, final onItemLongClickLitner longClickLitner) {
            super(itemView);
            mView = itemView;
            mDate = mView.findViewById(R.id.id_userFeedBack_date);
            mFeedback = mView.findViewById(R.id.id_userFeedBack);
            mFeedbackUserName = mView.findViewById(R.id.id_userFeedBack_name);

            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG, "onLongClick: Work..");
                    longClickLitner.onLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
