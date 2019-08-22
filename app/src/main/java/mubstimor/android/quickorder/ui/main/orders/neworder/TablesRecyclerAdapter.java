package mubstimor.android.quickorder.ui.main.orders.neworder;

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
import mubstimor.android.quickorder.models.Table;

public class TablesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Table> tables = new ArrayList<>();
    private OnTableListener onTableListener;

    public TablesRecyclerAdapter(OnTableListener onTableListener) {
        this.onTableListener = onTableListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_table_list_item, parent, false);
        return new TableViewHolder(view, onTableListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Table selectedTable = tables.get(position);
        ((TableViewHolder)holder).bind(selectedTable);
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public void setOrders(List<Table> tables){
        this.tables = tables;
        notifyDataSetChanged();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tableName;
        MaterialCardView itemLayout;
        OnTableListener onTableListener;

        public TableViewHolder(@NonNull View itemView, OnTableListener onTableListener) {
            super(itemView);
            tableName = itemView.findViewById(R.id.primary_text);
            itemLayout = itemView.findViewById(R.id.item_parent_layout);
            this.onTableListener = onTableListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Table table){
            tableName.setText("Table " + Integer.toString(table.getNumber()));
        }

        @Override
        public void onClick(View v) {

            onTableListener.onTableClick(getAdapterPosition(), tables.get(getAdapterPosition()) );
            Log.d("orderclick", "onClick: orderclicked " + tables.get(getAdapterPosition()) );

        }
    }

    public interface OnTableListener {
        void onTableClick(int position, Table table);
    }
}