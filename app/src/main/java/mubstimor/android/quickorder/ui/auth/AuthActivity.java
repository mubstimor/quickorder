package mubstimor.android.quickorder.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.User;
import mubstimor.android.quickorder.ui.main.MainActivity;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";

    private AuthViewModel viewModel;

    private EditText username, password;

    private TextView response;

    private ProgressBar progressBar;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progress_bar);

        response = findViewById(R.id.txtResponse);

        findViewById(R.id.btnLogin).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers(){
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if(userAuthResource != null){
                    switch (userAuthResource.status){
                        case LOADING:{
                            showProgressBar(true);
                            break;
                        }

                        case AUTHENTICATED:{
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: LOGIN SUCCESS:  " + userAuthResource.data.getEmail());
                            onLoginSuccess();
                            break;
                        }

                        case ERROR:{
                            showProgressBar(false);
                            response.setText("Invalid Login Credentials");
                            break;
                        }

                        case NOT_AUTHENTICATED:{
                            showProgressBar(false);
                            break;
                        }
                    }
                }
            }
        });

    }

    private void onLoginSuccess(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressBar(boolean isVisible){
        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:{
                attemptLogin();
                break;
            }
        }

    }


    private void attemptLogin(){
        if(TextUtils.isEmpty(username.getText().toString()) ||
                TextUtils.isEmpty(password.getText().toString())
        ){
            return;
        }
        viewModel.authenticateWithUser(username.getText().toString(), password.getText().toString());
    }
}
