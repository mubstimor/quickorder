package mubstimor.android.quickorder.ui.main.orders.menu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.models.Meal;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private OnMealListener onMealListener;

    public MenuRecyclerAdapter(OnMealListener onMealListener) {
        this.onMealListener = onMealListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_list_item, parent, false);
        return new TableViewHolder(view, onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Meal selectedMeal = meals.get(position);
        ((TableViewHolder)holder).bind(selectedMeal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setOrders(List<Meal> meals){
        this.meals = meals;
        notifyDataSetChanged();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mealName, price;
        MaterialCardView itemLayout;
        OnMealListener onMealListener;

        public TableViewHolder(@NonNull View itemView, OnMealListener onMealListener) {
            super(itemView);
            mealName = itemView.findViewById(R.id.meal_name);
            price = itemView.findViewById(R.id.meal_price);
            itemLayout = itemView.findViewById(R.id.item_parent_layout);
            this.onMealListener = onMealListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Meal meal){
            mealName.setText(meal.getName());
            price.setText(Integer.toString(meal.getPrice()));
        }

        @Override
        public void onClick(View v) {

            onMealListener.onTableClick(getAdapterPosition(), meals.get(getAdapterPosition()) );
            Log.d("orderclick", "onClick: orderclicked " + meals.get(getAdapterPosition()) );

        }
    }

    public interface OnMealListener {
        void onTableClick(int position, Meal meal);
    }
}