package mubstimor.android.quickorder.ui.main.orders.condiments;

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

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.Condiment;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.confirm.ConfirmFragment;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuFragment;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CondimentFragment extends DaggerFragment implements CondimentRecyclerAdapter.OnMealListener {

    public static final String MEALNAME = "mealName";
    public static final String MEALID = "mealId";

    private CondimentViewModel viewModel;
    private RecyclerView recyclerView;
    private CondimentRecyclerAdapter adapter;
    TextView emptyView;
    NavController navController;
    Bundle bundle;
    int tableId = -1;
    int orderId = -1;
    int mealId = -1;
    String mealName = "";


    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        return inflater.inflate(R.layout.fragment_recycler_condiments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bundle = new Bundle();
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        viewModel = ViewModelProviders.of(this, providerFactory).get(CondimentViewModel.class);

        initRecyclerView();
        subscribeObservers();
    }

    private void getDataFromBundle(){
        Bundle args = getArguments();
        Log.d(TAG, "onStart: args " + args);
        if(args != null){
            tableId = args.getInt(SelectMenuFragment.TABLEID);
            orderId = args.getInt(SelectMenuFragment.ORDERID);
            mealId = args.getInt(CondimentFragment.MEALID);
            mealName = args.getString(CondimentFragment.MEALNAME);
            Log.d(TAG, "onStart: orderId " + tableId);
        }
    }

    private void subscribeObservers(){
        viewModel.observePosts().removeObservers(getViewLifecycleOwner());
        viewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Condiment>>>() {
            @Override
            public void onChanged(Resource<List<Condiment>> listResource) {
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

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new CondimentRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTableClick(int position, Condiment condiment) {
        Log.d(TAG, "onTableClick: order clicked " + condiment.getName());
        bundle.putString(ConfirmFragment.CONDIMENTNAME, condiment.getName());
        bundle.putInt(SelectMenuFragment.TABLEID, tableId);
        bundle.putInt(SelectMenuFragment.ORDERID, orderId);
        bundle.putInt(CondimentFragment.MEALID, mealId);
        bundle.putString(CondimentFragment.MEALNAME, mealName);
        navController.navigate(R.id.action_selectCondimentScreen_to_confirmOrderScreen, bundle);
    }

}
