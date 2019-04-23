package com.claire.traveldiary.map.showdiary;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.util.ImageManager;

import java.util.List;

public class ShowDiaryAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ShowDiaryAdapter";

    private ShowDiaryContract.Presenter mPresenter;
    private Context mContext;

    private List<Diary> mDiaryList;


    public ShowDiaryAdapter(ShowDiaryContract.Presenter presenter, Context context, List<Diary> diaries) {
        mPresenter = presenter;
        mContext = context;
        mDiaryList = diaries;
    }

    public class ShowDiaryHolder extends RecyclerView.ViewHolder {

        private CardView mCard;
        private ImageView mImage;
        private TextView mTitle;
        private TextView mDate;

        public ShowDiaryHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.cardview_map);
            mImage = itemView.findViewById(R.id.image_in_map);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDate = itemView.findViewById(R.id.tv_date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ShowDiaryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_in_map, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ShowDiaryHolder) {


            if (mDiaryList.get(position).getImage().get(0).startsWith("https")) {
                ImageManager.getInstance().setImageByUrl(
                        ((ShowDiaryHolder) holder).mImage, mDiaryList.get(position).getImage().get(0));
            } else {
                ((ShowDiaryHolder) holder).mImage.setImageURI(Uri.parse(mDiaryList.get(position).getImage().get(0)));
            }
            ((ShowDiaryHolder) holder).mTitle.setText(mDiaryList.get(position).getTitle());
            ((ShowDiaryHolder) holder).mDate.setText(mDiaryList.get(position).getDate());

            //click card into edit page
            ((ShowDiaryHolder) holder).mCard.setOnClickListener(v -> {
                mPresenter.openEdit(mDiaryList.get(position));
                mPresenter.closePopup();
            });

        }
    }

    public void showDiary(List<Diary> diaries) {
        mDiaryList = diaries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDiaryList == null) {
            return 0;
        }
        return mDiaryList.size();
    }
}
