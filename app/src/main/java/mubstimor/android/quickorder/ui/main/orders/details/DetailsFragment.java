package mubstimor.android.quickorder.ui.main.orders.details;

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

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.OrderDetail;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuFragment;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DetailsFragment extends DaggerFragment implements DetailsRecyclerAdapter.OnOrderListener {

    public static final String ORDERID = "orderId";

    private DetailsViewModel viewModel;
    private RecyclerView recyclerView;
    private DetailsRecyclerAdapter adapter;
    NavController navController;
    Bundle bundle;
    TextView emptyView;
    int orderId = -1;
    int tableId = -1;
    FloatingActionButton floatingActionButton;

//    @Inject
//    DetailsRecyclerAdapter adapter;
    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @NonNull Bundle savedInstanceState) {
        Log.i("savedInstance", ""+ savedInstanceState);
        getDataFromBundle();
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void getDataFromBundle(){
        Bundle args = getArguments();
        Log.d(TAG, "onStart: args " + args);
        if(args != null){
            orderId = args.getInt(ORDERID);
            tableId = args.getInt(SelectMenuFragment.TABLEID);
            Log.d(TAG, "onStart: orderId " + orderId);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bundle = new Bundle();
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);
        floatingActionButton = view.findViewById(R.id.fab);

        viewModel = ViewModelProviders.of(this, providerFactory).get(DetailsViewModel.class);

        initRecyclerView();
        subscribeObservers();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt(SelectMenuFragment.ORDERID, orderId);
                bundle.putInt(SelectMenuFragment.TABLEID, tableId);
                Log.i("sending--to-menu", orderId + " - " + tableId);
                navController.navigate(R.id.action_orderDetailsScreen_to_selectMenuScreen, bundle);
            }
        });
    }

    private void subscribeObservers(){
        viewModel.observePosts(orderId).removeObservers(getViewLifecycleOwner());
        viewModel.observePosts(orderId).observe(getViewLifecycleOwner(), new Observer<Resource<List<OrderDetail>>>() {
            @Override
            public void onChanged(Resource<List<OrderDetail>> listResource) {
                if(listResource != null){
                    switch (listResource.status){
                        case LOADING:{
                            Log.d(TAG, "onChanged: LOADING ....");
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: got order details ....");
                            if(listResource.data.size() > 0){
                                adapter.setOrderDetails(listResource.data);
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

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new DetailsRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOrderClick(int position) {
        Log.d("onClick", "onClick: Imp");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ORDERID, orderId);
        outState.putInt(SelectMenuFragment.TABLEID, tableId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            orderId = savedInstanceState.getInt(ORDERID);
            tableId = savedInstanceState.getInt(SelectMenuFragment.TABLEID);
        }
    }

}
