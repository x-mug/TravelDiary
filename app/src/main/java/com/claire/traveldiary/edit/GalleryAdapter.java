package com.claire.traveldiary.edit;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.claire.traveldiary.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter {

    private static final String TAG = "GalleryAdapter";

    private EditContract.Presenter mPresenter;

    private Context mContext;
    private ArrayList<String> mImages;

    private boolean canOpenGallery = true;

    public GalleryAdapter(EditContract.Presenter presenter, ArrayList<String> images) {
       mPresenter = presenter;
       mImages = images;
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageGallery;
        private ImageButton mButtonAdd;

        GalleryViewHolder(View itemView) {
            super(itemView);

            mImageGallery = itemView.findViewById(R.id.img_gallery);
            mButtonAdd = itemView.findViewById(R.id.imgBtn_addImage);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GalleryViewHolder) {

            if (canOpenGallery) {
                ((GalleryViewHolder) holder).mButtonAdd.setOnClickListener(v -> {
                    mPresenter.openGallery();
                });
                ((GalleryViewHolder) holder).mImageGallery.setOnClickListener(v -> {
                    mPresenter.openGallery();
                });
            } else {
                ((GalleryViewHolder) holder).mImageGallery.setClickable(false);
            }

            //upload photo
            if (mImages != null) {
                for(int i = 0; i < mImages.size(); i++) {
                    ((GalleryViewHolder) holder).mButtonAdd.setVisibility(View.INVISIBLE);
                    ((GalleryViewHolder) holder).mImageGallery.setImageURI(Uri.parse(mImages.get(position)));
                }
            }
        }
    }

    public void setImage(ArrayList<String> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    public void setOpenGallery(int status) {
        if (status == 0) {
            canOpenGallery = false;
        } else {
            canOpenGallery = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mImages == null) {
            return 1;
        }
        return mImages.size();
    }
}
