package com.example.docapp.Category_consulter.consulter_dashboard.feedback_reply;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.Category_consulter.ConsulterAdapter;
import com.example.docapp.R;

import java.util.ArrayList;

public class FeedbackReplyAdapter extends RecyclerView.Adapter<FeedbackReplyAdapter.feedbackViewHolder> {
    ArrayList<FeedbackReplyModel> modelArrayList;


    private static final String TAG = "FeedbackReplyAdapter";


    private FeedbackReplyAdapter.OnItemClickListener mListener;
    private FeedbackReplyAdapter.OnItemLongClickListener mLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public void setonItemClickListener(FeedbackReplyAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public FeedbackReplyAdapter(ArrayList<FeedbackReplyModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public feedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_reply_row, parent, false);
        feedbackViewHolder viewHolder = new feedbackViewHolder(view, mListener,mLongClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull feedbackViewHolder holder, int position) {

        holder.viewDate.setText(modelArrayList.get(position).getDate());
        holder.viewUserFeedback.setText(modelArrayList.get(position).getFeedback());
        holder.viewUserName.setText(modelArrayList.get(position).getUserName());
        holder.viewFeedbackReply.setText(modelArrayList.get(position).getFeedbackReply());

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class feedbackViewHolder extends RecyclerView.ViewHolder {

        public TextView viewUserName;
        public TextView viewUserFeedback;
        public TextView viewDate;
        public TextView viewFeedbackReply;
        public ImageView imageViewReplyIcon;

        public feedbackViewHolder(@NonNull View itemView, final OnItemClickListener clickListener, final OnItemLongClickListener longClickListener) {
            super(itemView);
            viewUserName = itemView.findViewById(R.id.id_userFeedBack_Reply_name);
            viewUserFeedback = itemView.findViewById(R.id.id_userFeedBack);
            viewDate = itemView.findViewById(R.id.id_userFeedBack_reply_date);
            viewFeedbackReply = itemView.findViewById(R.id.consulter_reply);
            imageViewReplyIcon = itemView.findViewById(R.id.btn_feedback_reply);

            viewFeedbackReply.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(getAdapterPosition());
                    return false;
                }
            });
            imageViewReplyIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
