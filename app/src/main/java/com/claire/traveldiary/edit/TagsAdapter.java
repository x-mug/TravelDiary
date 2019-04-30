package com.claire.traveldiary.edit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;

import java.util.List;

import mabbas007.tagsedittext.TagsEditText;

public class TagsAdapter extends RecyclerView.Adapter {

    private static final String TAG = "TagsAdapter";

    private EditContract.Presenter mPresenter;
    private EditAdapter mEditAdapter;

    private Context mContext;

    private Diary mDiary;
    private List<String> mTagsList;

    private boolean canEdit = true;

    public TagsAdapter(EditContract.Presenter presenter, Diary diary) {
        mPresenter = presenter;
        mDiary = diary;
    }

    private class TagsViewHolder extends RecyclerView.ViewHolder {

        private TagsEditText mTags;

        TagsViewHolder(View itemView) {
            super(itemView);

            mTags = itemView.findViewById(R.id.edit_tags);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new TagsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tags, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof TagsViewHolder) {

            if (canEdit) {
                ((TagsViewHolder) holder).mTags.setFocusableInTouchMode(true);
            } else {
                ((TagsViewHolder) holder).mTags.setFocusableInTouchMode(false);
            }

            if (mDiary.getTags() != null) {
                ((TagsViewHolder) holder).mTags.setTags(mDiary.getTags().toString().replace("[","").replace("]","").split("\\,"));
            } else {
                ((TagsViewHolder) holder).mTags.setHint("");
                mTagsList = ((TagsViewHolder) holder).mTags.getTags();
            }

        }
    }

    public void setEditStatus(int status) {
        if (status == 0) {
            canEdit = false;
        } else {
            canEdit = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
