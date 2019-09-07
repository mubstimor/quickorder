package mubstimor.android.quickorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Condiment {

    @SerializedName("id")
    @Expose
    private int condimentId;

    @SerializedName("name")
    @Expose
    private String name;

    public Condiment(int condimentId, String name) {
        this.condimentId = condimentId;
        this.name = name;
    }

    public Condiment() {
    }

    public int getCondimentId() {
        return condimentId;
    }

    public void setCondimentId(int condimentId) {
        this.condimentId = condimentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
