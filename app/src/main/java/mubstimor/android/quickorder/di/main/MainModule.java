package mubstimor.android.quickorder.di.main;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.orders.OrdersRecyclerAdapter;
import mubstimor.android.quickorder.ui.main.orders.details.DetailsRecyclerAdapter;
import retrofit2.Retrofit;

@Module
public class MainModule {

//    @MainScope
//    @Provides
//    static OrdersRecyclerAdapter provideAdapter(){
//        return new OrdersRecyclerAdapter();
//    }

//    @MainScope
//    @Provides
//    static DetailsRecyclerAdapter provideDetailsAdapter(){
//        return new DetailsRecyclerAdapter(this);
//    }

    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit){
        return retrofit.create(MainApi.class);
    }

}
