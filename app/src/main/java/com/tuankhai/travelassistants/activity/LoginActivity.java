package com.tuankhai.travelassistants.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.activity.base.BaseActivity;
import com.tuankhai.travelassistants.library.slideractivity.Slider;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderPosition;
import com.tuankhai.travelassistants.utils.AppContansts;
import com.tuankhai.travelassistants.webservice.DTO.UserDTO;
import com.tuankhai.travelassistants.webservice.main.MyCallback;
import com.tuankhai.travelassistants.webservice.main.RequestService;
import com.tuankhai.travelassistants.webservice.request.AddUserRequest;

public class LoginActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    private Toolbar toolbar;
    private TextView txtName;
    private ImageView imgPhoto;
    private LinearLayout layoutUser, layoutLogo;
    private FrameLayout layoutSignin, layoutSignout;
    private TextView btnSignOut, btnSignInG, btnSignInF;
    private LoginButton btnFacebook;

    private ProgressDialog progressDialog;

    private int request_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        initToolbar();
        initSlider();
        initGoogle();
        initFacebook();

        addControls();
    }

    private void initSlider() {
        SliderConfig mConfig = new SliderConfig.Builder()
                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimary))
                .position(SliderPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.2f)
                .edge(true)
                .edgeSize(0.2f)
                .build();
        Slider.attach(this, mConfig);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        ((TextView)findViewById(R.id.txt_title)).setText(getString(R.string.txt_title_activity_login));
    }

    private void initFacebook() {
        btnFacebook = (LoginButton) findViewById(R.id.btn_sign_in_login_facebook);
        btnFacebook.setReadPermissions("email", "public_profile");
        btnFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logError("facebook: onSuccess", loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                logError("facebook: onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                logError("facebook: onErr", exception.toString());
            }
        });
    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mCallbackManager = CallbackManager.Factory.create();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            hideProgressDialog();
                        } else {
                            logError("signInWithCredential: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideProgressDialog();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(mUser);
    }

    private void addControls() {
        request_code = getIntent().getIntExtra(AppContansts.INTENT_DATA, 0);

        txtName = (TextView) findViewById(R.id.txt_name_login);
        imgPhoto = (ImageView) findViewById(R.id.img_photo_login);
        btnSignInG = (TextView) findViewById(R.id.btn_sign_in_google);
        btnSignInF = (TextView) findViewById(R.id.btn_sign_in_facebook);
        btnSignOut = (TextView) findViewById(R.id.btn_sign_out);
        layoutUser = (LinearLayout) findViewById(R.id.layout_login_user);
        layoutLogo = (LinearLayout) findViewById(R.id.layout_login_logo);
        layoutSignin = (FrameLayout) findViewById(R.id.layout_login_login);
        layoutSignout = (FrameLayout) findViewById(R.id.layout_login_signout);

        layoutLogo.setVisibility(View.VISIBLE);
        layoutUser.setVisibility(View.GONE);
        layoutSignout.setVisibility(View.GONE);
        layoutSignin.setVisibility(View.VISIBLE);

        btnSignOut.setOnClickListener(this);
        btnSignInF.setOnClickListener(this);
        btnSignInG.setOnClickListener(this);

        progressDialog = new ProgressDialog(this, R.style.ProgressDialog);
        progressDialog.setMessage("Sign in...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showProgressDialog();
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    updateUI(null);
                    hideProgressDialog();
                }
            } else {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.e("status", "result ERR");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        logError("firebaseAuthWithGoogle", acct.getId());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            logError("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            hideProgressDialog();
                        } else {
                            logError("signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void showProgressDialog() {
        progressDialog.show();
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            updateUI(null);
                            hideProgressDialog();
                        }
                    }
                });
    }

    private void revokeAccess() {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                        hideProgressDialog();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            txtName.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).into(imgPhoto);
            layoutUser.setVisibility(View.VISIBLE);
            layoutLogo.setVisibility(View.GONE);
            layoutSignout.setVisibility(View.VISIBLE);
            layoutSignin.setVisibility(View.GONE);
            new RequestService().load(
                    new AddUserRequest(
                            "",
                            user.getUid(),
                            user.getEmail(),
                            user.getDisplayName(),
                            user.getPhotoUrl().toString()),
                    false,
                    new MyCallback() {
                        @Override
                        public void onSuccess(Object response) {
                            super.onSuccess(response);
//                            hideProgressDialog();
                        }

                        @Override
                        public void onFailure(Object error) {
                            super.onFailure(error);
//                            hideProgressDialog();
                        }
                    }, UserDTO.class);
            switch (request_code) {
                case AppContansts.REQUEST_LOGIN:
                    setResult(RESULT_OK);
                    finish();
                    break;
                case AppContansts.REQUEST_LOGIN_SCHEDULE: {
                    Intent intent = new Intent(this, ScheduleActivity.class);
                    startActivity(intent);
                }
            }
        } else {
            layoutLogo.setVisibility(View.VISIBLE);
            layoutUser.setVisibility(View.GONE);
            layoutSignout.setVisibility(View.GONE);
            layoutSignin.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        logError("onConnectionFailed", connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in_google:
                signInGoogle();
                break;
            case R.id.btn_sign_in_facebook:
                btnFacebook.performClick();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
        }
    }
}
