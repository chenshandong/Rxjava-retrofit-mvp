package com.chensd.funnydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chensd.funnydemo.R;
import com.chensd.funnydemo.model.ImageInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chen on 2017/1/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {

    private List<ImageInfo> list;
    private OnItemClickListener listener;

    public Object getItem(int position){
        return list.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem_layout, null);
        return new DeBounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, holder.getLayoutPosition());
                }
            });
        }

        DeBounceViewHolder deBounceViewHolder = (DeBounceViewHolder) holder;
        Glide.with(holder.itemView.getContext()).load(list.get(position).getImage_url())
                .placeholder(R.drawable.loading).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(deBounceViewHolder.imageView);
        deBounceViewHolder.textView.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setImages(List<ImageInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class DeBounceViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.recycler_img)
        ImageView imageView;
        @Bind(R.id.description_tv)
        TextView textView;

        public DeBounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
}
