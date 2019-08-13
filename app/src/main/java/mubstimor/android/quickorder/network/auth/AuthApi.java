package mubstimor.android.quickorder.network.auth;

import java.util.HashMap;

import io.reactivex.Flowable;
import mubstimor.android.quickorder.models.User;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/api/v1/users/login/")
    Flowable<User> getUser(@Body HashMap<String, User> loginInfo);
}
