package com.ar.azurewebservice.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.azurewebservice.R;
import com.ar.azurewebservice.model.TPromo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.MyViewHolder> {
    private ArrayList<TPromo> data;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;

    public PromoAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<TPromo> getData() {
        return data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_promo, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TPromo promo = data.get(position);
        holder.position = position;

        final long identity = System.currentTimeMillis();
        holder.identity = identity;

        holder.imgURL = promo.getImage();

        if (holder.identity == identity){
            Glide.with(context).
                    load(holder.imgURL).
                    centerCrop().
                    crossFade().
                    into(holder.img);
        }

        holder.title.setText(promo.getTitle());
        holder.desc.setText(promo.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.title) TextView title;
        @BindView(R.id.desc) TextView desc;
        @BindView(R.id.img) ImageView img;

        int position;
        String imgURL;
        long identity;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClick(null, v, position, 0);
            }
        }
    }
}
