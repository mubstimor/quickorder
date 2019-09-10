package mubstimor.android.quickorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private int orderId;

    @SerializedName("registered_by")
    @Expose
    private int registeredBy;

    @SerializedName("table")
    @Expose
    private int table;

    @SerializedName("prep_status")
    @Expose
    private String prepStatus;

    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    @SerializedName("added_date")
    @Expose
    private String addedDate;

    public Order() {
    }

    public Order(int orderId, int registeredBy, int table, String prepStatus, String paymentStatus, String addedDate) {
        this.orderId = orderId;
        this.registeredBy = registeredBy;
        this.table = table;
        this.prepStatus = prepStatus;
        this.paymentStatus = paymentStatus;
        this.addedDate = addedDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(int registeredBy) {
        this.registeredBy = registeredBy;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getPrepStatus() {
        return prepStatus;
    }

    public void setPrepStatus(String prepStatus) {
        this.prepStatus = prepStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
