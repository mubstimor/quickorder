package mubstimor.android.quickorder.ui.main.orders.neworder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.Table;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuFragment;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SelectTableFragment extends DaggerFragment implements TablesRecyclerAdapter.OnTableListener {

    private SelectTableViewModel viewModel;
    private RecyclerView recyclerView;
    private TablesRecyclerAdapter adapter;
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    NavController navController;
    Bundle bundle;
    int orderId = -1;


    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bundle = new Bundle();
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        viewModel = ViewModelProviders.of(this, providerFactory).get(SelectTableViewModel.class);
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.hide();

        initRecyclerView();
        subscribeObservers();
//        subscribeOrderObserver();
    }

    private void subscribeObservers(){
        viewModel.observeTables().removeObservers(getViewLifecycleOwner());
        viewModel.observeTables().observe(getViewLifecycleOwner(), new Observer<Resource<List<Table>>>() {
            @Override
            public void onChanged(Resource<List<Table>> listResource) {
                if(listResource != null){
                    switch (listResource.status){
                        case LOADING:{
                            Log.d(TAG, "onChanged: LOADING ....");
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: got tables ....");
                            adapter.setOrders(listResource.data);
                            if(listResource.data.size() > 0){
                                adapter.setOrders(listResource.data);
                                emptyView.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                        case ERROR:{
                            Log.e(TAG, "onChanged: ERROR .... " + listResource.message );
                            break;
                        }
                    }
                }
            }
        });
    }

    private void subscribeOrderObserver(final int tableId){
        viewModel.observeCreateOrder(tableId).removeObservers(getViewLifecycleOwner());
        viewModel.observeCreateOrder(tableId).observe(getViewLifecycleOwner(), new Observer<Resource<Order>>() {
            @Override
            public void onChanged(Resource<Order> resource) {
                if(resource != null){
                    switch (resource.status){
                        case LOADING:{
                            Log.d(TAG, "onChanged: LOADING ....");
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: got tables ....");
                            Log.d(TAG, "onChanged: Order Created:  " + resource.data.getOrderId());
                            orderId = resource.data.getOrderId();
                            onOrderCreatedSuccess(tableId, orderId);
                            break;
                        }
                        case ERROR:{
                            Log.e(TAG, "onChanged: ERROR .... " + resource.message );
                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new TablesRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTableClick(int position, Table table) {
        Log.d(TAG, "onTableClick: table clicked " + table.getTableId());
        subscribeOrderObserver(table.getTableId());
//        Log.i("orderId now", ""+ orderId);
    }

    public void onOrderCreatedSuccess(int tableId, int orderId){
        if(orderId != -1){
            bundle.putInt(SelectMenuFragment.ORDERID, orderId);
            bundle.putInt(SelectMenuFragment.TABLEID, tableId);
            navController.navigate(R.id.action_selectTableScreen_to_selectMenuScreen, bundle);
        } else {
            Snackbar.make(getView(), "Unable to proceed", Snackbar.LENGTH_SHORT).show();
        }
    }

}
