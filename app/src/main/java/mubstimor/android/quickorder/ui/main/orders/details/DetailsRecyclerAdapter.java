package mubstimor.android.quickorder.ui.main.orders.details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.models.Order;
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
        return new PostViewHolder(view, onOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final OrderDetail selectedOrderDetail = ordersDetails.get(position);
        ((PostViewHolder)holder).bind(selectedOrderDetail);
    }

    @Override
    public int getItemCount() {
        return ordersDetails.size();
    }

    public void setOrderDetails(List<OrderDetail> orders){
        this.ordersDetails = orders;
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableName, paymentStatus;
        MaterialCardView itemLayout;
        OnOrderListener onOrderListener;

        public PostViewHolder(@NonNull View itemView, OnOrderListener onOrderListener) {
            super(itemView);
            tableName = itemView.findViewById(R.id.order_primary_text);
            paymentStatus = itemView.findViewById(R.id.order_txtPayStatus);
            itemLayout = itemView.findViewById(R.id.order_parent_layout);
            this.onOrderListener = onOrderListener;

            itemView.setOnClickListener(this);
        }

        public void bind(OrderDetail order){
            Log.d("order details", "bind: " + order.getMealId());
            tableName.setText(Integer.toString(order.getMealId()));
            paymentStatus.setText(Integer.toString(order.getQuantity()));
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