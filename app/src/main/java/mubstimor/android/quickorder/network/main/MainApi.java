package mubstimor.android.quickorder.network.main;

import java.util.List;

import io.reactivex.Flowable;
import mubstimor.android.quickorder.models.Condiment;
import mubstimor.android.quickorder.models.Meal;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.OrderDetail;
import mubstimor.android.quickorder.models.Table;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MainApi {

    @GET("/api/v1/orders/")
    Flowable<List<Order>> getUnpaidOrders();

    @GET("/api/v1/orders/{orderId}/details/")
    Flowable<List<OrderDetail>> getOrderDetails(
            @Path("orderId") int orderId
    );

    @GET("/api/v1/tables/")
    Flowable<List<Table>> getTables();

    @GET("/api/v1/meals/")
    Flowable<List<Meal>> getMeals();

    @GET("/api/v1/condiments/")
    Flowable<List<Condiment>> getCondiments();

    @POST("/api/v1/orders/")
    Flowable<Order> createOrder(@Body Order order);

    @POST("/api/v1/orders/{orderId}/details/")
    Flowable<OrderDetail> createOrderDetail(
            @Path("orderId") int orderId,
            @Body OrderDetail orderDetail
    );
}
