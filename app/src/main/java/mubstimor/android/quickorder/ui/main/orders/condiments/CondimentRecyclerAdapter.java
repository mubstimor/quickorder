package mubstimor.android.quickorder.ui.main.orders.condiments;

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
import mubstimor.android.quickorder.models.Condiment;

public class CondimentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Condiment> condiments = new ArrayList<>();
    private OnMealListener onMealListener;

    public CondimentRecyclerAdapter(OnMealListener onMealListener) {
        this.onMealListener = onMealListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_condiment_list_item, parent, false);
        return new TableViewHolder(view, onMealListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Condiment selectedCondiment = condiments.get(position);
        ((TableViewHolder)holder).bind(selectedCondiment);
    }

    @Override
    public int getItemCount() {
        return condiments.size();
    }

    public void setOrders(List<Condiment> meals){
        this.condiments = meals;
        notifyDataSetChanged();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView condimentName, price;
        MaterialCardView itemLayout;
        OnMealListener onMealListener;

        public TableViewHolder(@NonNull View itemView, OnMealListener onMealListener) {
            super(itemView);
            condimentName = itemView.findViewById(R.id.meal_name);
            itemLayout = itemView.findViewById(R.id.item_parent_layout);
            this.onMealListener = onMealListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Condiment condiment){
            condimentName.setText(condiment.getName());
        }

        @Override
        public void onClick(View v) {

            onMealListener.onTableClick(getAdapterPosition(), condiments.get(getAdapterPosition()) );
            Log.d("condiment", "onClick: orderclicked " + condiments.get(getAdapterPosition()) );

        }
    }

    public interface OnMealListener {
        void onTableClick(int position, Condiment condiment);
    }
}