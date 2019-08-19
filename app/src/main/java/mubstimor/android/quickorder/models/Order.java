package mubstimor.android.quickorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

//    @SerializedName("orderId")
//    @Expose
//    private int orderId;

    @SerializedName("registered_by")
    @Expose
    private int id;

    @SerializedName("table")
    @Expose
    private String table;

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

    public Order(int id, String table, String prepStatus, String paymentStatus, String addedDate) {
        this.id = id;
        this.table = table;
        this.prepStatus = prepStatus;
        this.paymentStatus = paymentStatus;
        this.addedDate = addedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
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
