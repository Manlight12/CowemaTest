package com.sml.cowematest.activitie.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sml.cowematest.R;
import com.sml.cowematest.activitie.detail.DetailActivity;
import com.sml.cowematest.databinding.ItemRestoBinding;
import com.sml.cowematest.model.RestaurantItem;
import com.sml.cowematest.util.Fkey;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.RestoHolder> {
    Context context;
    ArrayList<RestaurantItem> restaurantItems=new ArrayList<>();

    public RestoAdapter(Context context, ArrayList<RestaurantItem> restaurantItems) {
        this.context = context;
        this.restaurantItems = restaurantItems;
    }

    @NonNull
    @Override
    public RestoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRestoBinding binding=ItemRestoBinding.inflate(LayoutInflater.from(context),parent,false);

        return new RestoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoHolder holder, int position) {
        RestaurantItem item=restaurantItems.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return restaurantItems.size();
    }

    public class RestoHolder extends RecyclerView.ViewHolder{
        ItemRestoBinding binding;
        public RestoHolder(ItemRestoBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void setData(RestaurantItem item){
            String title= item.getTitre();
            String pHUrl=item.getPlaceHoldUrl();
            binding.title.setText(title);
            Picasso.with(context)
                    .load(pHUrl)
                    .placeholder(androidx.cardview.R.color.cardview_dark_background)
                    .into(binding.image);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(context, DetailActivity.class);
                    intent.putExtra(Fkey.ID,item.id);
                    intent.putExtra(Fkey.TITLE,item.getTitre());
                    intent.putExtra(Fkey.URL,item.getUrl());
                    context.startActivity(intent);

                }
            });
        }
    }
}
