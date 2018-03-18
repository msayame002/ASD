package com.varry.facebooksofteng;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView textView;
    private LoginButton loginButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printKey();
        initComponents();
        loginFb();
    }

    private void loginFb() {

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "me/posts",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        Intent intent = new Intent(MainActivity.this, Homepage.class);

                                        try
                                        {
                                            JSONArray rawPost = response.getJSONObject().getJSONArray("data");
                                            intent.putExtra("jsondata", rawPost.toString());
                                            startActivity(intent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        ).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        textView.setText("Bitch");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        textView.setText("Cuck");
                    }
                });
    }

    private void initComponents() {
        callbackManager = CallbackManager.Factory.create();
        textView = (TextView)findViewById(R.id.textView);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("user_posts");
    }

    private void printKey() {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.varry.facebooksofteng", PackageManager.GET_SIGNATURES);
            for(Signature signature : packageInfo.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
