package com.claire.traveldiary.mainpage;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;

import java.util.List;

public class MainPageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MainPageAdapter";

    private int VIEW_TYPE = TYPE_WATERFALL;

    private static final int TYPE_WATERFALL = 0;
    private static final int TYPE_LINEAR = 1;

    private MainPageContract.Presenter mPresenter;

    private Context mContext;

    private List<Diary> mDiaryList;

    private boolean isLongClick = false;
    private boolean isSearch = false;


    public MainPageAdapter(MainPageContract.Presenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    private class MainPageViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLongClickView;
        private CardView mCard;
        private ImageView mMainImage;
        private TextView mDiaryTitle;
        private TextView mDiaryDate;

        private ImageButton mDelete;
        private ImageButton mShare;

        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);

            mLongClickView = itemView.findViewById(R.id.constraint_background);
            mCard = itemView.findViewById(R.id.card_diary_mainpage);
            mMainImage = itemView.findViewById(R.id.img_background);
            mMainImage.setAlpha(0.9f);
            mDiaryTitle = itemView.findViewById(R.id.tv_title);
            mDiaryDate = itemView.findViewById(R.id.tv_date);
            mDelete = itemView.findViewById(R.id.btn_delete_diary);
            mShare = itemView.findViewById(R.id.btn_share_diary);

            mPresenter.setFontType(mDiaryTitle, mDiaryDate);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_WATERFALL) {
            Log.d(TAG,"Waterfall");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_waterfall, parent, false);
            return new MainPageViewHolder(view);
        }
        if (viewType == TYPE_LINEAR) {
            Log.d(TAG,"Linear");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_linear, parent, false);
            return new MainPageViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MainPageViewHolder) {

            //show card
            if (mDiaryList.size() > 0) {
                if (mDiaryList.get(position).getImage() != null) {
                    if (mDiaryList.get(position).getImage().size() > 0) {
                        ((MainPageViewHolder) holder).mMainImage.setImageURI(Uri.parse(mDiaryList.get(position).getImage().get(0)));
                    }
                }

                if (mDiaryList.get(position).getTitle() != null) {
                    ((MainPageViewHolder) holder).mDiaryTitle.setText(mDiaryList.get(position).getTitle());
                } else {
                    ((MainPageViewHolder) holder).mDiaryTitle.setText(R.string.diary_title);
                }

                if (mDiaryList.get(position).getDate() != null) {
                    ((MainPageViewHolder) holder).mDiaryDate.setText(mDiaryList.get(position).getDate());
                } else {
                    ((MainPageViewHolder) holder).mDiaryDate.setText(R.string.diary_date);
                }
            }

            //click card
            ((MainPageViewHolder) holder).mCard.setOnClickListener(v -> {
                if (isLongClick) {
                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);
                    isLongClick = false;
                } else {
                    //click card
                    mPresenter.openEdit(mDiaryList.get(position));
                }
            });

            //long click delete or share
            ((MainPageViewHolder) holder).mCard.setOnLongClickListener(v -> {
                ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.VISIBLE);
                ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.GONE);
                ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.GONE);

                //click delete
                ((MainPageViewHolder) holder).mDelete.setOnClickListener(v1 -> {
                    //delete diary from room
                    mPresenter.removeDiary(mDiaryList.get(position).getId());
                    mDiaryList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                    Toast.makeText(v.getContext(), mContext.getResources().getString(R.string.delete_diary), Toast.LENGTH_SHORT).show();
                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);

                });

                //click share
                ((MainPageViewHolder) holder).mShare.setOnClickListener(v12 -> {
                    mPresenter.shareDiary(mDiaryList.get(position));

                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);
                });
                isLongClick = true;
                return true;
            });
        }
    }

    public void changeLayout(int viewType) {
        if (viewType == 0) {
            VIEW_TYPE = TYPE_WATERFALL;
        } else {
            VIEW_TYPE = TYPE_LINEAR;
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Diary> diaries) {
        if (!isSearch) {
            mDiaryList = diaries;
            notifyDataSetChanged();
        }
    }

    public void updateSearchData(List<Diary> diaries) {
        mDiaryList = diaries;
        isSearch = true;
        notifyDataSetChanged();
    }

    public void updateSearchDataStatus(List<Diary> diaries) {
        mDiaryList = diaries;
        isSearch = false;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        if (mDiaryList == null) {
            return 1;
        }
        return mDiaryList.size();
    }
}
