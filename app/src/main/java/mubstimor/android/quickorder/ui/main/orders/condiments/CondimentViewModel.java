package mubstimor.android.quickorder.ui.main.orders.condiments;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.models.Condiment;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class CondimentViewModel extends ViewModel {

    private static final String TAG = "SelectMealViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Condiment>>> condiments;

    @Inject
    public CondimentViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "CondimentViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<List<Condiment>>> observePosts(){
        if(condiments == null){
            condiments = new MediatorLiveData<>();
            condiments.setValue(Resource.loading((List<Condiment>)null));

            final LiveData<Resource<List<Condiment>>> source = LiveDataReactiveStreams.fromPublisher(
                  mainApi.getCondiments()
                    .onErrorReturn(new Function<Throwable, List<Condiment>>() {
                        @Override
                        public List<Condiment> apply(Throwable throwable) throws Exception {
                            Log.e(TAG, "apply: ", throwable);
                            Condiment condiment = new Condiment();
                            condiment.setCondimentId(-1);
                            ArrayList<Condiment> condiments = new ArrayList<>();
                            condiments.add(condiment);
                            return condiments;
                        }
                    })
                    .map(new Function<List<Condiment>, Resource<List<Condiment>>>() {
                        @Override
                        public Resource<List<Condiment>> apply(List<Condiment> condimentList) throws Exception {
                            if(condimentList.size() > 0){
                                if(condimentList.get(0).getCondimentId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(condimentList);
                        }
                    })
                    .subscribeOn(Schedulers.io())
            );
            condiments.addSource(source, new Observer<Resource<List<Condiment>>>() {
                @Override
                public void onChanged(Resource<List<Condiment>> listResource) {
                    condiments.setValue(listResource);
                    condiments.removeSource(source);
                }
            });
        }
        return condiments;
    }

}
