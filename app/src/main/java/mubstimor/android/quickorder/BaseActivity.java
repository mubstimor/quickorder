package mubstimor.android.quickorder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import mubstimor.android.quickorder.models.User;
import mubstimor.android.quickorder.ui.auth.AuthActivity;
import mubstimor.android.quickorder.ui.auth.AuthResource;
import mubstimor.android.quickorder.util.PreferencesManager;

public abstract class BaseActivity extends DaggerAppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Inject
    public SessionManager sessionManager;

    PreferencesManager preferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(this);
        subscribeObservers();

    }

    private void subscribeObservers(){
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if(userAuthResource != null){
                    switch (userAuthResource.status){
                        case LOADING:{
                            break;
                        }

                        case AUTHENTICATED:{
                            Log.d(TAG, "onChanged: LOGIN SUCCESS:  " + userAuthResource.data.getEmail());
                            break;
                        }

                        case ERROR:{
                            Log.e(TAG, "onChanged: " + userAuthResource.message);

                            break;
                        }

                        case NOT_AUTHENTICATED:{
                            navLoginScreen();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void navLoginScreen(){
        preferencesManager.clear();
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
