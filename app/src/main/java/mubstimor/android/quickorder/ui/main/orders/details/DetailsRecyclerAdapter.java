package mubstimor.android.quickorder.ui.main.orders.details;

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
import mubstimor.android.quickorder.models.OrderDetail;

public class DetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderDetail> ordersDetails = new ArrayList<>();
    private OnOrderListener onOrderListener;

    public DetailsRecyclerAdapter(OnOrderListener onOrderListener) {
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_detail_list_item, parent, false);
        return new DetailViewHolder(view, onOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final OrderDetail selectedOrderDetail = ordersDetails.get(position);
        ((DetailViewHolder)holder).bind(selectedOrderDetail);
    }

    @Override
    public int getItemCount() {
        return ordersDetails.size();
    }

    public void setOrderDetails(List<OrderDetail> orders){
        this.ordersDetails = orders;
        notifyDataSetChanged();
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mealName, quantity, accompaniment;
        MaterialCardView itemLayout;
        OnOrderListener onOrderListener;

        public DetailViewHolder(@NonNull View itemView, OnOrderListener onOrderListener) {
            super(itemView);
            mealName = itemView.findViewById(R.id.order_primary_text);
            quantity = itemView.findViewById(R.id.order_txtPayStatus);
            accompaniment = itemView.findViewById(R.id.order_condiment);
            itemLayout = itemView.findViewById(R.id.order_parent_layout);
            this.onOrderListener = onOrderListener;

            itemView.setOnClickListener(this);
        }

        public void bind(OrderDetail order){
            mealName.setText(Integer.toString(order.getMealId()));
            quantity.setText(Integer.toString(order.getQuantity()));
            String condiments = "(" + String.join(",", order.getAccompaniments()) + ")";
            accompaniment.setText(condiments);

        }

        @Override
        public void onClick(View v) {
            onOrderListener.onOrderClick(getAdapterPosition());
            Log.d("onClick", "onClick: override");
        }
    }

    public interface OnOrderListener{
        void onOrderClick(int position);
    }
}