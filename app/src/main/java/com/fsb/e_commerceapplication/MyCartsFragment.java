package com.fsb.e_commerceapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fsb.e_commerceapplication.activities.PlacedOrderActivity;
import com.fsb.e_commerceapplication.adapters.MyCartAdapter;
import com.fsb.e_commerceapplication.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartsFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    TextView overTotalAmount;
    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> cartModelList;
    ProgressBar progressBar;
    AppCompatButton buynow;
    ConstraintLayout constraintLayout1 ,constraintLayout2;
    public MyCartsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_my_carts, container, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recycler_view);
        buynow = root.findViewById(R.id.buy_now_items);
        recyclerView.setVisibility(View.GONE);
        progressBar = root.findViewById(R.id.progressbar);
        constraintLayout1 = root.findViewById(R.id.constraint1);
        constraintLayout2 = root.findViewById(R.id.constrain2);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        overTotalAmount = root.findViewById(R.id.textViewPrice);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver , new IntentFilter("MyTotalAmount"));
        cartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(myCartAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                MyCartModel data = cartModelList.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
                myCartAdapter.removeItem(position);
                Snackbar snackbar = Snackbar.make(((MyCartAdapter.ViewHolder) viewHolder).itemView,data.getProductName()+" have been removed ?",Snackbar.LENGTH_LONG)   ;
                snackbar.show();
                if(cartModelList.size() == 0){
                    constraintLayout2.setVisibility(View.GONE);
                    constraintLayout1.setVisibility(View.VISIBLE);
                }
                else{
                    constraintLayout2.setVisibility(View.VISIBLE);
                    constraintLayout1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                    super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
                }
                else{
                    final View forigroungView = ((MyCartAdapter.ViewHolder)viewHolder).viewBack;
                    getDefaultUIUtil().onDrawOver(c,recyclerView,forigroungView,dX,dY,actionState,isCurrentlyActive);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(actionState != ItemTouchHelper.ACTION_STATE_DRAG)
                { final View forigroungView = ((MyCartAdapter.ViewHolder)viewHolder).linearlayout_acceuil1;
                    getDefaultUIUtil().onDraw(c,recyclerView,forigroungView,dX,dY,actionState,isCurrentlyActive);
                }}


            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                //super.clearView(recyclerView, viewHolder);
                final View forigroundView = ((MyCartAdapter.ViewHolder)viewHolder).linearlayout_acceuil1;
//                forigroundView.setBackgroundColor(ContextCompat.getColor(((MyCartAdapter.ViewHolder)viewHolder).linearlayout_acceuil1.getContext(),new Color(R.color.black)));
                getDefaultUIUtil().clearView(forigroundView);

            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(viewHolder!=null){
                    final View forigroungView = ((MyCartAdapter.ViewHolder)viewHolder).linearlayout_acceuil1;
                    if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                        forigroungView.setBackgroundColor(Color.LTGRAY);
                    }
                    getDefaultUIUtil().onSelected(forigroungView);
                }
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        String documentId = documentSnapshot.getId();
                        MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                        cartModel.setDocumentId(documentId);
                        cartModelList.add(cartModel);
                        myCartAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    calculateTotalAmount(cartModelList);
                }
            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) cartModelList);
                startActivity(intent);
            }
        });



        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {

        double totalAmount = 0.0 ;
        for (MyCartModel myCartModel : cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        if(cartModelList.size() == 0){
            constraintLayout2.setVisibility(View.GONE);
            constraintLayout1.setVisibility(View.VISIBLE);
        }
        else{
            constraintLayout2.setVisibility(View.VISIBLE);
            constraintLayout1.setVisibility(View.GONE);
        }
        overTotalAmount.setText("Total Amount : $"+totalAmount);

    }

    /*public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totallBill  = intent.getIntExtra("totalAmount",0);
            overTotalAmount.setText("Total Bill : "+totallBill+"$");
        }
    };*/




}