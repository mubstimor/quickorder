package mubstimor.android.quickorder.ui.main.orders.confirm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.models.Condiment;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.OrderDetail;
import mubstimor.android.quickorder.network.main.MainApi;
import mubstimor.android.quickorder.ui.main.Resource;

public class ConfirmViewModel extends ViewModel {

    private static final String TAG = "SelectMealViewModel";

    // inject
    private final SessionManager sessionManager;
    private final MainApi mainApi;

    private MediatorLiveData<Resource<List<Condiment>>> condiments;

    @Inject
    public ConfirmViewModel(SessionManager sessionManager, MainApi mainApi) {
        this.sessionManager = sessionManager;
        this.mainApi = mainApi;
        Log.d(TAG, "ConfirmViewModel: viewmodel is working ...");
    }

    public LiveData<Resource<OrderDetail>> observeCreateOrderDetail(int orderId, OrderDetail orderDetail){
        return LiveDataReactiveStreams.fromPublisher(
                mainApi.createOrderDetail(orderId, orderDetail)
                        .onErrorReturn(new Function<Throwable, OrderDetail>() {
                            @Override
                            public OrderDetail apply(Throwable throwable) throws Exception {
                                OrderDetail errorOrderDetail = new OrderDetail();
                                errorOrderDetail.setOrderId(-1);
                                return errorOrderDetail;
                            }
                        })
                        .map(new Function<OrderDetail, Resource<OrderDetail>>() {
                            @Override
                            public Resource<OrderDetail> apply(OrderDetail orderDetail) throws Exception {
                                if(orderDetail.getOrderId() == -1){
                                    return Resource.error("Something went wrong", null);
                                }
                                return Resource.success(orderDetail);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
    }
}
