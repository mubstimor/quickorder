package mubstimor.android.quickorder.di.main;

import dagger.Module;
import dagger.Provides;
import mubstimor.android.quickorder.network.main.MainApi;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit){
        return retrofit.create(MainApi.class);
    }

}
