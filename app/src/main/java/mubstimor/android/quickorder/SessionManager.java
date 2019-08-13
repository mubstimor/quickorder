package mubstimor.android.quickorder;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import mubstimor.android.quickorder.models.User;
import mubstimor.android.quickorder.ui.auth.AuthResource;

@Singleton
public class SessionManager {
    private static final String TAG = "SessionManager";

    private MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {
    }

    public void authenticateWithUser(final LiveData<AuthResource<User>> source){
        if(cachedUser != null){
            cachedUser.setValue(AuthResource.loading((User)null));
            cachedUser.addSource(source, new Observer<AuthResource<User>>() {
                @Override
                public void onChanged(AuthResource<User> userAuthResource) {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);
                }
            });
        } else {
            Log.d(TAG, "authenticateWithUser: previous session detected. Retrieving user from cache.");
        }
    }

    public void logOut(){
        Log.d(TAG, "logOut: logging out ....");
        cachedUser.setValue(AuthResource.<User>logout());
    }

    public LiveData<AuthResource<User>> getAuthUser(){
        return cachedUser;
    }
}
