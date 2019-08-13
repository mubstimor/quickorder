package mubstimor.android.quickorder.ui.main.orders;

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
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class OrdersViewModel extends ViewModel {

    private static final String TAG = "OrdersViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Order>>> orders;

    @Inject
    public OrdersViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "OrdersViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<List<Order>>> observePosts(){
        if(orders == null){
            orders = new MediatorLiveData<>();
            orders.setValue(Resource.loading((List<Order>)null));

//            String username = sessionManager.getAuthUser().getValue().data.getUsername();

            final LiveData<Resource<List<Order>>> source = LiveDataReactiveStreams.fromPublisher(
                  mainApi.getUnpaidOrders()
                    .onErrorReturn(new Function<Throwable, List<Order>>() {
                        @Override
                        public List<Order> apply(Throwable throwable) throws Exception {
                            Log.e(TAG, "apply: ", throwable);
                            Order order = new Order();
                            order.setId(-1);
                            ArrayList<Order> orders = new ArrayList<>();
                            orders.add(order);
                            return orders;
                        }
                    })
                    .map(new Function<List<Order>, Resource<List<Order>>>() {
                        @Override
                        public Resource<List<Order>> apply(List<Order> orders) throws Exception {
                            if(orders.size() > 0){
                                if(orders.get(0).getId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(orders);
                        }
                    })
                    .subscribeOn(Schedulers.io())
            );
            orders.addSource(source, new Observer<Resource<List<Order>>>() {
                @Override
                public void onChanged(Resource<List<Order>> listResource) {
                    orders.setValue(listResource);
                    orders.removeSource(source);
                }
            });
        }
        return orders;
    }

}
