package com.claire.traveldiary.map.showdiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.claire.traveldiary.R;
import com.claire.traveldiary.data.Diary;
import com.claire.traveldiary.data.DiaryPlace;
import com.claire.traveldiary.data.room.DiaryDatabase;

import java.util.List;

public class ShowDiaryAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ShowDiaryAdapter";

    private ShowDiaryContract.Presenter mPresenter;
    private Context mContext;

    private DiaryDatabase mDatabase;

    private List<Diary> mDiaryList;


    public ShowDiaryAdapter(ShowDiaryContract.Presenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
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

            mDatabase = DiaryDatabase.getIstance(mContext);
            mDiaryList = mDatabase.getDiaryDAO().getDiarys();

            //((ShowDiaryHolder) holder).mTitle.setText(mPlacesList.get(position));

        }
    }

    public void showDiary(List<Diary> diaries) {
        mDiaryList = diaries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
