package com.example.goostore;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Goods> mGoods;
    private OnItemClickListener mListener;


    public GoodsAdapter(Context context, List<Goods> goods) {
        mContext = context;
        mGoods = goods;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.goods_list, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Goods goodsCurrent = mGoods.get(position);
        holder.textViewName.setText(goodsCurrent.getName());
        holder.textViewCurrentPrice.setText(goodsCurrent.getBasePrice() + "WON");
        holder.textViewDeadLine.setText(goodsCurrent.getDeadLineMon() + "/" + goodsCurrent.getDeadLineDay() + ", " + goodsCurrent.getDeadLineYear() + ". 23:59");
        Picasso.get()
                .load(goodsCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewName;
        public TextView textViewCurrentPrice;
        public TextView textViewDeadLine;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_goods_name);
            textViewCurrentPrice = itemView.findViewById(R.id.text_goods_price);
            textViewDeadLine = itemView.findViewById(R.id.text_goods_deadline);
            imageView = itemView.findViewById(R.id.goods_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }


    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
