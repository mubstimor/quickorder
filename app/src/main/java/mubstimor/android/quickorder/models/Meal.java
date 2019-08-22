package mubstimor.android.quickorder.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meal {

    @SerializedName("id")
    @Expose
    private int mealId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image")
    @Expose
    private String image;

    public Meal(int mealId, String name, int price, String description, String image) {
        this.mealId = mealId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public Meal() {
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
