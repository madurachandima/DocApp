package com.example.docapp.category_main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docapp.R;

import java.util.List;

public class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolderAdapter.ViewHolderCategory> {
    private Context mContext;
    private List<CategoryModel> mCategories;

    private static final String TAG = "ViewHolderAdapter";

    public ViewHolderAdapter(Context context, List<CategoryModel> categories) {
        mContext = context;
        mCategories = categories;
    }

    @NonNull
    @Override
    public ViewHolderAdapter.ViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.catogary_list, parent, false);
        return new ViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter.ViewHolderCategory holder, final int position) {

        holder.mTextViewcategoryType.setText(mCategories.get(position).getCategoryType());
        holder.mImageViewBookThumbnail.setImageResource(mCategories.get(position).getThumbnail());


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = mCategories.get(position).getActivityCategoryTypes().trim();
                try {

                    Class newClass = Class.forName(cat); // My Class Name
                    Intent intent = new Intent(mContext, newClass);
                    Log.d(TAG, "onClick: "+newClass);
                    //Intent intent = new Intent(mContext,Category_music.class);

                    intent.putExtra("Category", mCategories.get(position).getCategoryType());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(intent);


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class ViewHolderCategory extends RecyclerView.ViewHolder {
        TextView mTextViewcategoryType;
        ImageView mImageViewBookThumbnail;
        CardView mCardView;

        public ViewHolderCategory(@NonNull View itemView) {
            super(itemView);

            mTextViewcategoryType = itemView.findViewById(R.id.id_txt_category);
            mImageViewBookThumbnail = itemView.findViewById(R.id.id_img_category);
            mCardView = itemView.findViewById(R.id.card_view);


        }

    }
}


