package mubstimor.android.quickorder.ui.main.orders;

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
import mubstimor.android.quickorder.models.Order;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private OnOrderListener onOrderListener;

    public OrdersRecyclerAdapter(OnOrderListener orderListener) {
        this.onOrderListener = orderListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list_item, parent, false);
        return new PostViewHolder(view, onOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Order selectedOrder = orders.get(position);
        ((PostViewHolder)holder).bind(selectedOrder);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableName, paymentStatus;
        MaterialCardView itemLayout;
        OnOrderListener onOrderListener;

        public PostViewHolder(@NonNull View itemView, OnOrderListener onOrderListener) {
            super(itemView);
            tableName = itemView.findViewById(R.id.primary_text);
            paymentStatus = itemView.findViewById(R.id.txtPayStatus);
            itemLayout = itemView.findViewById(R.id.item_parent_layout);
            this.onOrderListener = onOrderListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Order order){
            tableName.setText(order.getTable());
            paymentStatus.setText(order.getPaymentStatus());
        }

        @Override
        public void onClick(View v) {

            onOrderListener.onOrderClick(getAdapterPosition(), orders.get(getAdapterPosition()) );
            Log.d("orderclick", "onClick: orderclicked " + orders.get(getAdapterPosition()) );

        }
    }

    public interface OnOrderListener{
        void onOrderClick(int position, Order order);
    }
}