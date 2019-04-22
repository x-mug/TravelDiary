package com.claire.traveldiary.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.claire.traveldiary.TravelDiaryApplication;
import com.claire.traveldiary.data.User;
import com.claire.traveldiary.data.room.DiaryDAO;
import com.claire.traveldiary.data.room.DiaryDatabase;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final String TAG = "UserManager";

    private Context mContext;

    private User mUser;

    private DiaryDatabase mDatabase;
    private FirebaseFirestore mFirebaseDb;
    private DatabaseReference mFirebaseRef;

    private CallbackManager mFbCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;


    private static class UserManagerHolder {
        private static final UserManager INSTANCE = new UserManager();
    }

    public UserManager() {
    }

    public static UserManager getInstance() {
        return UserManagerHolder.INSTANCE;
    }

    public void loginDiaryByFacebook(Context context, final LoadCallback loadCallback) {

        mFbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "FB Login Success");
                Log.i(TAG, "loginResult.getAccessToken().getToken() = " + loginResult.getAccessToken().getToken());
                Log.i(TAG, "loginResult.getAccessToken().getUserId() = " + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "loginResult.getAccessToken().getApplicationId() = " + loginResult.getAccessToken().getApplicationId());

                getUserInfo(loginResult.getAccessToken(),loadCallback);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB Login Cancel");
                loadCallback.onFail("FB Login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FB Login Error");
                loadCallback.onFail("FB Login Error: " + exception.getMessage());
            }
        });

        loginFacebook(context);
    }

    public void logoutDiaryFromFacebook(final LoadCallback loadCallback) {
        mFbCallbackManager = CallbackManager.Factory.create();
        // Defining the AccessTokenTracker
        mAccessTokenTracker = new AccessTokenTracker() {
            // This method is invoked everytime access token changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                getUserInfo(currentAccessToken, loadCallback);
            }
        };

        logoutFacebook();
    }

    private void loginFacebook(Context context) {
        LoginManager.getInstance().logInWithReadPermissions(
                (Activity) context, Arrays.asList("email"));
    }

    public void logoutFacebook() {
        LoginManager.getInstance().logOut();
    }

    private void getUserInfo(AccessToken accessToken,LoadCallback loadCallback) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    if (object == null) {
                        loadCallback.onSuccess();
                        clearUserLogin();

                    } else {
                        Long id = object.getLong("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                        Log.d(TAG,"user information " + id + name + email + picture);

                        //save user to room
                        mDatabase = DiaryDatabase.getIstance(TravelDiaryApplication.getAppContext());
                        DiaryDAO diaryDAO = mDatabase.getDiaryDAO();

                        User user = new User();
                        user.setId(id);
                        user.setName(name);
                        user.setEmail(email);
                        user.setPicture(picture);

                        diaryDAO.insertOrUpdateUser(user);
                        Log.d(TAG,"User" + diaryDAO.getUser().getName());

                        //save data to firebase
                        mFirebaseDb = FirebaseFirestore.getInstance();

                        Map<String, Object> users = new HashMap<>();
                        users.put("id", id);
                        users.put("name", name);
                        users.put("email", email);
                        users.put("picture", picture);

                        // Add a new document with a generated ID
                        mFirebaseDb.collection("Users").document(id.toString())
                                .set(users)
                                .addOnSuccessListener(documentReference ->
                                        Log.d(TAG, "DocumentSnapshot successful written! "))
                                .addOnFailureListener(e ->
                                        Log.w(TAG, "Error adding document", e));

                        loadCallback.onSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }


    public void clearUserLogin() {
        setUser(null);
    }

    public CallbackManager getFbCallbackManager() {
        return mFbCallbackManager;
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }


    public interface LoadCallback {

        void onSuccess();

        void onFail(String errorMessage);

        void onInvalidToken(String errorMessage);
    }
}
