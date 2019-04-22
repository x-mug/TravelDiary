package com.claire.traveldiary.mainpage;

import android.content.Context;
import android.graphics.BitmapFactory;
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
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainPageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MainPageAdapter";

    private MainPageContract.Presenter mPresenter;

    private Context mContext;

    private FirebaseFirestore mFirebaseDb;
    private DiaryDatabase mRoomDb;

    private List<Diary> mDiaryList;

    private boolean isLongclick = false;


    public MainPageAdapter(MainPageContract.Presenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    private class MainPageViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private ConstraintLayout mLongClickView;
        private CardView mCard;
        private ImageView mMainImage;
        private TextView mDiaryTitle;
        private TextView mDiaryDate;

        private ImageButton mDelete;
        private ImageButton mShare;

        public MainPageViewHolder(@NonNull View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.card_space);
            mLongClickView = itemView.findViewById(R.id.constraint_background);
            mCard = itemView.findViewById(R.id.cardview_mainpage);
            mMainImage = itemView.findViewById(R.id.main_img_card);
            mMainImage.setAlpha(0.9f);
            mDiaryTitle = itemView.findViewById(R.id.edit_diary_title);
            mDiaryDate = itemView.findViewById(R.id.tv_diary_date);
            mDelete = itemView.findViewById(R.id.btn_delete_diary);
            mShare = itemView.findViewById(R.id.btn_share_diary);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_in_mainpage, parent, false);
        return new MainPageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MainPageViewHolder) {

            mRoomDb = DiaryDatabase.getIstance(mContext);
            mDiaryList = mRoomDb.getDiaryDAO().getAllDiaries();

            //show card
            if (mDiaryList.size() > 0) {
                if (mDiaryList.get(position).getImages() != null) {
                    ((MainPageViewHolder) holder).mMainImage.setImageBitmap(BitmapFactory.decodeFile(mDiaryList.get(position).getImages().get(0)));
                }

                if (mDiaryList.get(position).getTitle() != null) {
                    ((MainPageViewHolder) holder).mDiaryTitle.setText(mDiaryList.get(position).getTitle());
                } else {
                    ((MainPageViewHolder) holder).mDiaryTitle.setText("Travel");
                }

                if (mDiaryList.get(position).getDate() != null) {
                    ((MainPageViewHolder) holder).mDiaryDate.setText(mDiaryList.get(position).getDate());
                } else {
                    ((MainPageViewHolder) holder).mDiaryDate.setText("11 January 2019");
                }
            }

            //click space return original card view
            ((MainPageViewHolder) holder).mLayout.setOnClickListener(v -> {
                ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);
            });


            ((MainPageViewHolder) holder).mCard.setOnClickListener(v -> {
                if (isLongclick == true) {
                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);
                    isLongclick = false;
                } else {
                    //click card
                    mPresenter.openEdit(mRoomDb.getDiaryDAO().getAllDiaries().get(position));
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
                    mPresenter.deleteDiary(mDiaryList.get(position).getId());
                    mDiaryList.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();


                    Toast.makeText(v.getContext(), "Delete Diary", Toast.LENGTH_SHORT).show();
                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);

                });

                //click share
                ((MainPageViewHolder) holder).mShare.setOnClickListener(v12 -> {
                    Log.d(TAG, "Oh Share.....");
                    ((MainPageViewHolder) holder).mLongClickView.setVisibility(View.GONE);
                    ((MainPageViewHolder) holder).mDiaryTitle.setVisibility(View.VISIBLE);
                    ((MainPageViewHolder) holder).mDiaryDate.setVisibility(View.VISIBLE);
                });

                isLongclick = true;

                return true;
            });
        }
    }

    private void readData() {
        mFirebaseDb.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        if (mDiaryList == null) {
            return 1;
        }
        return mDiaryList.size();
    }
}
