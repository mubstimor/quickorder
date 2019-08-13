package mubstimor.android.quickorder.network.main;

import java.util.List;

import io.reactivex.Flowable;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.OrderDetail;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MainApi {

    @GET("/api/v1/orders/")
    Flowable<List<Order>> getUnpaidOrders();

    @GET("/api/v1/orders/{orderId}/details/")
    Flowable<List<OrderDetail>> getOrderDetails(
            @Path("orderId") int orderId
    );
}
