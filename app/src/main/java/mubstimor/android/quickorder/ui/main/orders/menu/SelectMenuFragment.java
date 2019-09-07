package mubstimor.android.quickorder.ui.main.orders.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
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
import mubstimor.android.quickorder.models.Meal;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.condiments.CondimentFragment;
import mubstimor.android.quickorder.util.VerticalSpacingItemDecoration;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SelectMenuFragment extends DaggerFragment implements MenuRecyclerAdapter.OnMealListener {

    public static final String TABLEID = "tableId";

    private SelectMenuViewModel viewModel;
    private RecyclerView recyclerView;
    private MenuRecyclerAdapter adapter;
    TextView emptyView;
    FloatingActionButton floatingActionButton;
    NavController navController;
    Bundle bundle;
    int tableId = -1;


    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tableId = getTableIdFromBundle();
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bundle = new Bundle();
        recyclerView = view.findViewById(R.id.recycler_view);
        emptyView = view.findViewById(R.id.empty_view);

        viewModel = ViewModelProviders.of(this, providerFactory).get(SelectMenuViewModel.class);
        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.hide();

        initRecyclerView();
        subscribeObservers();
    }

    private int getTableIdFromBundle(){
        Bundle args = getArguments();
        Log.d(TAG, "onStart: args " + args);
        int table_id = -1;
        if(args != null){
            table_id = args.getInt(TABLEID);
            Log.d(TAG, "onStart: orderId " + tableId);
        }
        return table_id;
    }

    private void subscribeObservers(){
        viewModel.observePosts().removeObservers(getViewLifecycleOwner());
        viewModel.observePosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Meal>>>() {
            @Override
            public void onChanged(Resource<List<Meal>> listResource) {
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
        adapter = new MenuRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTableClick(int position, Meal meal) {
        Log.d(TAG, "oncondiment: order clicked " + meal.getName());
        bundle.putInt(SelectMenuFragment.TABLEID, tableId);
        bundle.putString(CondimentFragment.MEALNAME, meal.getName());
        navController.navigate(R.id.action_selectMenuScreen_to_selectCondimentScreen, bundle);
    }

}
