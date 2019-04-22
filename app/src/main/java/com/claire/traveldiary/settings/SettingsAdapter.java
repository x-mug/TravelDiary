package com.claire.traveldiary.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.claire.traveldiary.R;
import com.claire.traveldiary.TravelDiaryApplication;
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.claire.traveldiary.util.UserManager;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class SettingsAdapter extends RecyclerView.Adapter{

    private static final String TAG = "SettingsAdapter";

    private SettingsContract.Presenter mPresenter;

    private Context mContext;
    private DiaryDatabase mDatabase;
    private UserManager mUserManager;

    public SettingsAdapter(SettingsContract.Presenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {

        private Button mLogin;
        private ImageView mUserImg;
        private TextView mUserName;
        private TextView mSync;
        private TextView mTextSize;
        private TextView mLanguage;
        private TextView mPassword;
        private TextView mNotification;
        private TextView mFeedback;

        public SettingsHolder(@NonNull View itemView) {
            super(itemView);

            mLogin = itemView.findViewById(R.id.btn_login);
            mUserImg = itemView.findViewById(R.id.img_profile);
            mUserName = itemView.findViewById(R.id.tv_user_name);
            mSync = itemView.findViewById(R.id.tv_sync);
            mTextSize = itemView.findViewById(R.id.tv_textsize);
            mLanguage = itemView.findViewById(R.id.tv_language);
            mPassword = itemView.findViewById(R.id.tv_key);
            mNotification = itemView.findViewById(R.id.tv_notification);
            mFeedback = itemView.findViewById(R.id.tv_mail);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new SettingsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof SettingsHolder) {

            mDatabase = DiaryDatabase.getIstance(mContext);
            DiaryDAO diaryDAO = mDatabase.getDiaryDAO();

            //can sync
            ((SettingsHolder) holder).mSync.setOnClickListener(v -> {
                mPresenter.openSyncDialog();
            });

            if (mUserManager.getInstance().isLoggedIn()) {
                //set image
                Picasso.get()
                        .load(diaryDAO.getUser().getPicture())
                        .placeholder(R.mipmap.ic_profile)
                        .transform(new CircleTransform())
                        .into(((SettingsHolder) holder).mUserImg);

                //set user name
                ((SettingsHolder) holder).mUserName.setText(diaryDAO.getUser().getName());

                //can log out
                ((SettingsHolder) holder).mLogin.setText(R.string.settings_logout);
                ((SettingsHolder) holder).mLogin.setOnClickListener(v -> mPresenter.logoutFacebook());
            } else {
                //set image
                ((SettingsHolder) holder).mUserImg.setImageResource(R.mipmap.ic_profile);

                //set name
                ((SettingsHolder) holder).mUserName.setText(R.string.settings_user);

                //can log in
                ((SettingsHolder) holder).mLogin.setText(R.string.settings_login);
                ((SettingsHolder) holder).mLogin.setOnClickListener(v -> mPresenter.loginFacebook());
            }
        }
    }

    public void updateView() {
        notifyDataSetChanged();
    }


    public class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
