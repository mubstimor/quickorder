package mubstimor.android.quickorder.di;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.util.Constants;
import mubstimor.android.quickorder.util.PreferencesManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    //    @Inject
//    static PreferencesManager preferencesManager;
//    private String authenticationToken;


//    public AppModule(String authToken) {
//        authenticationToken = authToken;
//    }

//    @Inject
//    SessionManager sessionManager;

    @Singleton
    @Provides
    Retrofit provideRetrofitInstance(final String authToken) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Log.i("requestUrl", "" + originalRequest.url());
                        String requestUrl = originalRequest.url().toString();
                        if (!requestUrl.contains("login")) {
//                                preferencesManager = new PreferencesManager(this);
//                            Log.i("usertoken on module", preferencesManager.getValue(Constants.KEY_USERTOKEN));
                            Request newRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + authToken)
                                    .build();
                            return chain.proceed(newRequest);
                        } else {
                            return chain.proceed(originalRequest);
                        }
                    }
                })
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions
                .placeholderOf(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application) {
        return ContextCompat.getDrawable(application, R.mipmap.reze_logo_hd);
    }

}

