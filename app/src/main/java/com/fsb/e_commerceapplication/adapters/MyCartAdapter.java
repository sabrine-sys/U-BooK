package com.fsb.e_commerceapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsb.e_commerceapplication.R;
import com.fsb.e_commerceapplication.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Context context;
    List<MyCartModel> cartModelList;
    int totalPrice = 0 ;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotalPrice()));
        holder.quantity.setText(cartModelList.get(position).getTotalQuantity());
        holder.date.setText(cartModelList.get(position).getCurrentDate());
        holder.time.setText(cartModelList.get(position).getCurrentTime() );

        /*totalPrice += cartModelList.get(position).getTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount",totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void removeItem(int position) {
        MyCartModel xxx =cartModelList.get(position);
        Toast.makeText(context,xxx.getProductName(),Toast.LENGTH_SHORT);
        cartModelList.remove(position);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").document(xxx.getDocumentId())
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartModelList.remove(xxx);
                    totalPrice -= xxx.getTotalPrice();

                    notifyDataSetChanged();
                }else{
                    Toast.makeText(context,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        notifyItemRemoved(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearlayout_acceuil1;
        public RelativeLayout viewBack;
        TextView name, price ,date, time, quantity, totalPrice;
        ImageView deleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.product_name);
            price =  itemView.findViewById(R.id.product_price);
            date =  itemView.findViewById(R.id.current_date);
            time =  itemView.findViewById(R.id.current_time);
            quantity =  itemView.findViewById(R.id.total_quantity);
            totalPrice =  itemView.findViewById(R.id.total_price);
            linearlayout_acceuil1 = itemView.findViewById(R.id.linearlayout_acceuil1);
            viewBack = itemView.findViewById(R.id.view_back);
        }
    }
}
