package mubstimor.android.quickorder.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table")
public class Table {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private int tableId;

    @SerializedName("number")
    @Expose
    private int number;

    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    @Expose
    private String imageUrl;

    public Table(int tableId, int number, String description, String imageUrl) {
        this.tableId = tableId;
        this.number = number;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Table() {
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
