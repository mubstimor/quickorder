package mubstimor.android.quickorder.ui.main.orders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.details.DetailsFragment;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class OrdersFragment extends DaggerFragment implements OrdersRecyclerAdapter.OnOrderListener {

    private OrdersViewModel viewModel;
    private RecyclerView recyclerView;
    private OrdersRecyclerAdapter adapter;

//    @Inject
//    OrdersRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_view);

        viewModel = ViewModelProviders.of(this, providerFactory).get(OrdersViewModel.class);

        initRecyclerView();
        subscribeObservers();
    }

    private void subscribeObservers(){
        viewModel.observePosts().removeObservers(getViewLifecycleOwner());
        viewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Order>>>() {
            @Override
            public void onChanged(Resource<List<Order>> listResource) {
                if(listResource != null){
                    switch (listResource.status){
                        case LOADING:{
                            Log.d(TAG, "onChanged: LOADING ....");
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: got posts ....");
                            adapter.setOrders(listResource.data);
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

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new OrdersRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOrderClick(int position, Order order) {
        Log.d(TAG, "onOrderClick: order clicked " + order);
        DetailsFragment fragment = new DetailsFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
//        transaction.replace(R.id.orderDetailsScreen, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
//        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.orderDetailsScreen);
    }
}
