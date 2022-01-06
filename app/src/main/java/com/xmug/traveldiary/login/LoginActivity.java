package com.xmug.traveldiary.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.xmug.traveldiary.MainActivity;
import com.xmug.traveldiary.R;
import com.xmug.traveldiary.component.MyBounceInterpolator;
import com.xmug.traveldiary.data.User;
import com.xmug.traveldiary.data.room.DiaryDAO;
import com.xmug.traveldiary.data.room.DiaryDatabase;
import com.xmug.traveldiary.settings.SettingsAdapter;
import com.xmug.traveldiary.util.UserManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private DiaryDatabase mRoomDb;
    private DiaryDAO mDiaryDAO;
    private User user;
    private String TAG = "LOGIN_ACTIVITY";

    private InputValidation inputValidation;
    private Button loginBtn;
    private Button SignBtn;
    private EditText email;
    private EditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitViews();
        InitListeners();
        InitObjects();
    }

    private void InitViews() {
        loginBtn = (Button) findViewById(R.id.SigninBtn);
        SignBtn = (Button) findViewById(R.id.SignupBtn);

        email = (EditText) findViewById(R.id.login_email);
        emailLayout = (TextInputLayout) findViewById(R.id.login_email_layout);
        password = (EditText) findViewById(R.id.login_password);
        passwordLayout = (TextInputLayout) findViewById(R.id.login_password_layout);
    }

    private void InitListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!verifyFormat()) return;

                User user = TryGetUserByEmailPassword();

                int duration = 3;
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Email/Password is not correct", duration).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);   //Intent intent=new Intent(MainActivity.this,JumpToActivity.class);
                    startActivity(intent);
                }
            }
        });

        SignBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (!verifyFormat()) return;
                //save user to room
                mRoomDb = DiaryDatabase.getIstance(LoginActivity.this);
                DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();

                User user = TryUpdateUserByEmail();
                if (user == null) {
                    // Insert
                    Toast.makeText(getApplicationContext(), "Welcome!" + user.getId(), 3).show();
                    user = new User();
                    user.setName(email.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(SHA256(password.getText().toString()));
                } else {
                    // Update
                    Toast.makeText(getApplicationContext(), "User Exists. Password Updated" + user.getId(), 3).show();
                    user.setName(email.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(SHA256(password.getText().toString()));
                }

                diaryDAO.insertOrUpdateUser(user);
            }
        });
    }

    private void InitObjects() {
        inputValidation = new InputValidation(LoginActivity.this);
    }

    private User TryGetUserByEmailPassword() {
        mRoomDb = DiaryDatabase.getIstance(LoginActivity.this);
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        return diaryDAO.getUserByEmailPassword(email.getText().toString(), SHA256(password.getText().toString()));
    }

    private User TryUpdateUserByEmail() {
        mRoomDb = DiaryDatabase.getIstance(LoginActivity.this);
        DiaryDAO diaryDAO = mRoomDb.getDiaryDAO();
        return diaryDAO.getUserByEmail(email.getText().toString());
    }

    public String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    private String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                messageDigest.update(strText.getBytes());
                byte byteBuffer[] = messageDigest.digest();

                StringBuffer strHexString = new StringBuffer();

                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private boolean verifyFormat() {
        if (!inputValidation.isInputEditTextFilled(email, emailLayout, "Email is empty")) {
            return false;
        }
        if (!inputValidation.isInputEditTextEmail(email, emailLayout, "Email is not valid")) {
            return false;
        }
        if (!inputValidation.isInputEditTextFilled(password, passwordLayout, "Not correct password")) {
            return false;
        }
        return true;
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        email.setText(null);
        password.setText(null);
    }

    private int GetUserID(String email) {
        return 1;
    }
}