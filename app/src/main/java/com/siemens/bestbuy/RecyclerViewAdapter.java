package com.siemens.bestbuy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mReleaseDate = new ArrayList<>();
    private ArrayList<Double> mPrice = new ArrayList<>();
    private ArrayList<Double> mRating = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mImageName, ArrayList<String> mImages,
                               ArrayList<String> mReleaseDate, ArrayList<Double> mPrice,
                               ArrayList<Double> mRating, Context mContext) {
        this.mImageName = mImageName;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mPrice = mPrice;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.image);

        viewHolder.imageName.setText(mImageName.get(i));
        viewHolder.imageRating.setText(mRating.get(i).toString());
        viewHolder.imagePrice.setText(mPrice.get(i).toString());
        viewHolder.imageDate.setText(mReleaseDate.get(i));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mImageName.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        TextView imageDate;
        TextView imageRating;
        TextView imagePrice;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            imageDate = itemView.findViewById(R.id.image_release);
            imagePrice = itemView.findViewById(R.id.image_price);
            imageRating = itemView.findViewById(R.id.image_rating);

        }
    }
}
