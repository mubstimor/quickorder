package mubstimor.android.quickorder.ui.main.orders.confirm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mubstimor.android.quickorder.R;
import mubstimor.android.quickorder.di.viewmodels.ViewModelProviderFactory;
import mubstimor.android.quickorder.models.Order;
import mubstimor.android.quickorder.models.OrderDetail;
import mubstimor.android.quickorder.ui.main.Resource;
import mubstimor.android.quickorder.ui.main.orders.condiments.CondimentFragment;
import mubstimor.android.quickorder.ui.main.orders.menu.SelectMenuFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ConfirmFragment extends DaggerFragment {

    public static final String CONDIMENTNAME = "accompaniment";

    public static final String MEALNAME = "mealName";
    public static final String MEALID = "mealId";

    private ConfirmViewModel viewModel;
    private RecyclerView recyclerView;
    NavController navController;
    Bundle bundle;

    TextView mealTxt;
    TextView accompanimentTxt;
    TextView orderQuantityTxt;
    TextView selectedQuantity;
    Button btnIncrement;
    Button btnDecrement;
    Button btnConfirmOrder;
    Button btnReset;
    Button btnAddMore;

    int tableId = -1;
    int mealId = -1;
    int orderId = -1;
    int quantity = 1;
    String mealName = "";
    String accompaniment = "";


    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        return inflater.inflate(R.layout.fragment_confirm_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bundle = new Bundle();
        viewModel = ViewModelProviders.of(this, providerFactory).get(ConfirmViewModel.class);
        mealTxt = view.findViewById(R.id.order_primary_text);
        accompanimentTxt = view.findViewById(R.id.order_condiment);
        orderQuantityTxt = view.findViewById(R.id.order_txtPayStatus);
        selectedQuantity = view.findViewById(R.id.order_qty);

        btnIncrement = view.findViewById(R.id.btnIncrement);
        btnDecrement = view.findViewById(R.id.btnDecrement);
        btnConfirmOrder = view.findViewById(R.id.btnConfirm);
        btnReset = view.findViewById(R.id.btnReset);
        btnAddMore = view.findViewById(R.id.btnAddMore);

        mealTxt.setText(mealName);
        accompanimentTxt.setText(accompaniment);
        orderQuantityTxt.setText("");
        selectedQuantity.setText(Integer.toString(quantity));

        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConfirmOrder.setEnabled(false);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setMealId(mealId);
                orderDetail.setQuantity(quantity);
                String[] condiments = new String[]{accompaniment};
                orderDetail.setAccompaniments(condiments);
                confirmOrder(orderId, orderDetail);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = 1;
                selectedQuantity.setText(Integer.toString(quantity));
                btnConfirmOrder.setEnabled(true);
            }
        });

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt(SelectMenuFragment.ORDERID, orderId);
                bundle.putInt(SelectMenuFragment.TABLEID, tableId);
                Log.i("sending--back", orderId + " - " + tableId);
                navController.navigate(R.id.action_confirmOrderScreen_to_selectMenuScreen, bundle);
            }
        });

    }

    private void getDataFromBundle(){
        Bundle args = getArguments();
        Log.d(TAG, "onStart: args " + args);
        if(args != null){
            tableId = args.getInt(SelectMenuFragment.TABLEID);
            orderId = args.getInt(SelectMenuFragment.ORDERID);
            mealId = args.getInt(CondimentFragment.MEALID);
            mealName = args.getString(CondimentFragment.MEALNAME);
            accompaniment = args.getString(ConfirmFragment.CONDIMENTNAME);
            Log.d(TAG, "onStart: orderId " + tableId);
        }
    }

    public void increaseQuantity(){
        quantity++;
        selectedQuantity.setText(Integer.toString(quantity));
    }

    public void decreaseQuantity(){
        if(quantity > 1) {
            quantity--;
        } else{
            Snackbar.make(getView(), "Quantity can't be less than 1", Snackbar.LENGTH_SHORT).show();
        }
        selectedQuantity.setText(Integer.toString(quantity));
    }

    private void subscribeOrderDetailObserver(int orderId, OrderDetail orderDetail){
        viewModel.observeCreateOrderDetail(orderId, orderDetail).removeObservers(getViewLifecycleOwner());
        viewModel.observeCreateOrderDetail(orderId, orderDetail).observe(getViewLifecycleOwner(), new Observer<Resource<OrderDetail>>() {
            @Override
            public void onChanged(Resource<OrderDetail> resource) {
                if(resource != null){
                    switch (resource.status){
                        case LOADING:{
                            Log.d(TAG, "onChanged: LOADING ....");
                            break;
                        }
                        case SUCCESS:{
                            Log.d(TAG, "onChanged: got tables ....");
                            Log.d(TAG, "onChanged: Order DETAIL submitted:  " + resource.data.getOrderId());
                            Snackbar.make(getView(), "Order DETAIL submitted " + resource.data.getOrderId(), Snackbar.LENGTH_SHORT).show();
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

    private void confirmOrder(int orderId, OrderDetail orderDetail){
        subscribeOrderDetailObserver(orderId, orderDetail);
    }

}
