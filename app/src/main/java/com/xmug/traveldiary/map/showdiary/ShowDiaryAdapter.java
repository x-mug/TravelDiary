package com.xmug.traveldiary.map.showdiary;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmug.traveldiary.R;
import com.xmug.traveldiary.data.Diary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ShowDiaryAdapter extends RecyclerView.Adapter {

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

            mCard = itemView.findViewById(R.id.card_diary_map);
            mImage = itemView.findViewById(R.id.img_background);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDate = itemView.findViewById(R.id.tv_date);

            mPresenter.setFontType(mTitle, mDate);
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
                if (mDiaryList.get(position).getImage() != null) {
                    if (mDiaryList.get(position).getImage().size() > 0) {
                        ((ShowDiaryHolder) holder).mImage.setImageURI(Uri.parse(mDiaryList.get(position).getImage().get(0)));
                    }
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
        Collections.sort(diaries, new Comparator<Diary>() {
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ROOT);
            @Override
            public int compare(Diary o1, Diary o2) {
                try {
                    return dateFormat.parse(o1.getDate()).compareTo(dateFormat.parse(o2.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        Collections.reverse(diaries);
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
