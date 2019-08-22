package mubstimor.android.quickorder.ui.main.orders.menu;

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
import mubstimor.android.quickorder.models.Meal;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class SelectMenuViewModel extends ViewModel {

    private static final String TAG = "SelectMealViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Meal>>> meals;

    @Inject
    public SelectMenuViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "OrdersViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<List<Meal>>> observePosts(){
        if(meals == null){
            meals = new MediatorLiveData<>();
            meals.setValue(Resource.loading((List<Meal>)null));

            final LiveData<Resource<List<Meal>>> source = LiveDataReactiveStreams.fromPublisher(
                  mainApi.getMeals()
                    .onErrorReturn(new Function<Throwable, List<Meal>>() {
                        @Override
                        public List<Meal> apply(Throwable throwable) throws Exception {
                            Log.e(TAG, "apply: ", throwable);
                            Meal meal = new Meal();
                            meal.setMealId(-1);
                            ArrayList<Meal> meals = new ArrayList<>();
                            meals.add(meal);
                            return meals;
                        }
                    })
                    .map(new Function<List<Meal>, Resource<List<Meal>>>() {
                        @Override
                        public Resource<List<Meal>> apply(List<Meal> meals) throws Exception {
                            if(meals.size() > 0){
                                if(meals.get(0).getMealId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(meals);
                        }
                    })
                    .subscribeOn(Schedulers.io())
            );
            meals.addSource(source, new Observer<Resource<List<Meal>>>() {
                @Override
                public void onChanged(Resource<List<Meal>> listResource) {
                    meals.setValue(listResource);
                    meals.removeSource(source);
                }
            });
        }
        return meals;
    }

}
