package mubstimor.android.quickorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("meal")
    @Expose
    private int mealId;

    @SerializedName("meal_accompaniments")
    @Expose
    private String[] accompaniments;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("order")
    @Expose
    private int orderId;

    public OrderDetail(int mealId, String[] accompaniments, int quantity, int orderId) {
        this.mealId = mealId;
        this.accompaniments = accompaniments;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public OrderDetail() {
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String[] getAccompaniments() {
        return accompaniments;
    }

    public void setAccompaniments(String[] accompaniments) {
        this.accompaniments = accompaniments;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
