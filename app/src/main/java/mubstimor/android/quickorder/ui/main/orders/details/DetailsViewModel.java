package mubstimor.android.quickorder.ui.main.orders.details;

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
import mubstimor.android.quickorder.models.OrderDetail;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<OrderDetail>>> orderDetails;

    @Inject
    public DetailsViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "DetailsViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<List<OrderDetail>>> observePosts(){
        if(orderDetails == null){
            orderDetails = new MediatorLiveData<>();
            orderDetails.setValue(Resource.loading((List<OrderDetail>)null));

//            String username = sessionManager.getAuthUser().getValue().data.getUsername();
            int orderId = 1;

            final LiveData<Resource<List<OrderDetail>>> source = LiveDataReactiveStreams.fromPublisher(
                  mainApi.getOrderDetails(orderId)
                    .onErrorReturn(new Function<Throwable, List<OrderDetail>>() {
                        @Override
                        public List<OrderDetail> apply(Throwable throwable) throws Exception {
                            Log.e(TAG, "apply: ", throwable);
                            OrderDetail order = new OrderDetail();
                            order.setOrderId(-1);
                            ArrayList<OrderDetail> orders = new ArrayList<>();
                            orders.add(order);
                            return orders;
                        }
                    })
                    .map(new Function<List<OrderDetail>, Resource<List<OrderDetail>>>() {
                        @Override
                        public Resource<List<OrderDetail>> apply(List<OrderDetail> orderDetails) throws Exception {
                            if(orderDetails.size() > 0){
                                if(orderDetails.get(0).getOrderId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                            }
                            return Resource.success(orderDetails);
                        }
                    })
                    .subscribeOn(Schedulers.io())
            );
            orderDetails.addSource(source, new Observer<Resource<List<OrderDetail>>>() {
                @Override
                public void onChanged(Resource<List<OrderDetail>> listResource) {
                    orderDetails.setValue(listResource);
                    orderDetails.removeSource(source);
                }
            });
        }
        return orderDetails;
    }
}
