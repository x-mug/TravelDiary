package com.claire.traveldiary.map.showdiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
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

    private Typeface mTypeface;


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

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("FONT", Context.MODE_PRIVATE);
                String fontType = sharedPreferences.getString("fontValue", "");
                switch (fontType) {
                    case "allura":
                        mTypeface = mContext.getResources().getFont(R.font.allura_regular);
                        setTypeface(mTypeface);
                        break;
                    case "amatic":
                        mTypeface = mContext.getResources().getFont(R.font.amatic_regular);
                        setTypefaceBig(mTypeface);
                        break;
                    case "blackjack":
                        mTypeface = mContext.getResources().getFont(R.font.blackjack);
                        setTypefaceMid(mTypeface);
                        break;
                    case "brizel":
                        mTypeface = mContext.getResources().getFont(R.font.brizel);
                        setTypefaceMid(mTypeface);
                        break;
                    case "dancing":
                        mTypeface = mContext.getResources().getFont(R.font.dancing_regular);
                        setTypeface(mTypeface);
                        break;
                    case "farsan":
                        mTypeface = mContext.getResources().getFont(R.font.farsan_regular);
                        setTypefaceMid(mTypeface);
                        break;
                    case "handwriting":
                        mTypeface = mContext.getResources().getFont(R.font.justan_regular);
                        setTypefaceBig(mTypeface);
                        break;
                    case "kaushan":
                        mTypeface = mContext.getResources().getFont(R.font.kaushan_regular);
                        setTypeface(mTypeface);
                        break;
                    case"default":
                        mTitle.setTypeface(Typeface.SERIF);
                        mDate.setTypeface(Typeface.SERIF);
                        break;
                }
            }
        }

        public void setTypeface(Typeface mTypeface) {
            mTitle.setTypeface(mTypeface);
            mDate.setTypeface(mTypeface);
            mTitle.setTextSize(24);
            mDate.setTextSize(18);
        }

        public void setTypefaceMid(Typeface mTypeface) {
            mTitle.setTypeface(mTypeface);
            mDate.setTypeface(mTypeface);
            mTitle.setTextSize(26);
            mDate.setTextSize(20);
        }

        public void setTypefaceBig(Typeface mTypeface) {
            mTitle.setTypeface(mTypeface);
            mDate.setTypeface(mTypeface);
            mTitle.setTextSize(30);
            mDate.setTextSize(24);
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

            if (mDiaryList.size() > 0) {
                if (mDiaryList.get(position).getImage().size() > 0) {
                    ((ShowDiaryHolder) holder).mImage.setImageURI(Uri.parse(mDiaryList.get(position).getImage().get(0)));
                }
                if (mDiaryList.get(position).getTitle() != null) {
                    ((ShowDiaryHolder) holder).mTitle.setText(mDiaryList.get(position).getTitle());
                }
                if (mDiaryList.get(position).getDate() != null) {
                    ((ShowDiaryHolder) holder).mDate.setText(mDiaryList.get(position).getDate());
                }

                //click card into edit page
                ((ShowDiaryHolder) holder).mCard.setOnClickListener(v -> {
                    mPresenter.openEdit(mDiaryList.get(position));
                    mPresenter.closePopup();
                });
            }
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
