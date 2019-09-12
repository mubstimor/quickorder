package mubstimor.android.quickorder.ui.main.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.SessionManager;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.User;
import mubstimor.android.quickorder.ui.auth.AuthResource;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.details.DetailsFragment;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuFragment;
import mubstimor.android.quickorder.util.Constants;
import mubstimor.android.quickorder.util.PreferencesManager;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;


public class OrdersFragment extends DaggerFragment implements OrdersRecyclerAdapter.OnOrderListener {

    private static final String TAG = "OrdersFragment";

    private OrdersViewModel viewModel;
    private RecyclerView recyclerView;
    private OrdersRecyclerAdapter adapter;
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    NavController navController;
    Bundle bundle;

    @Inject
    ViewModelProviderFactory providerFactory;

    PreferencesManager preferencesManager;

    @Inject
    SessionManager sessionManager;

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
        preferencesManager = new PreferencesManager(getContext());

        viewModel = ViewModelProviders.of(this, providerFactory).get(OrdersViewModel.class);
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewOrder();
            }
        });

        initRecyclerView();
        subscribeObservers();
        Log.i("usertoken", preferencesManager.getValue(Constants.KEY_USERTOKEN));
        LiveData<AuthResource<User>> user = sessionManager.getAuthUser();
        Log.i("usertoken from session", user.getValue().data.getToken());
    }

    private void subscribeObservers(){
        viewModel.observeOrders().removeObservers(getViewLifecycleOwner());
        viewModel.observeOrders().observe(getViewLifecycleOwner(), new Observer<Resource<List<Order>>>() {
            @Override
            public void onChanged(Resource<List<Order>> listResource) {
                if(listResource != null){
                    switch (listResource.status){
                        case LOADING:{
                            break;
                        }
                        case SUCCESS:{
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
        adapter = new OrdersRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOrderClick(int position, final Order order) {
        bundle.putInt(DetailsFragment.ORDERID, order.getOrderId());
        bundle.putInt(SelectMenuFragment.TABLEID, order.getTable());
        navController.navigate(R.id.action_ordersScreen_to_orderDetailsScreen, bundle);
    }

    public void addNewOrder(){
        navController.navigate(R.id.action_ordersScreen_to_selectTableScreen);
    }

}
