package com.udacity.baketime.baketime.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabLayoutAdapter extends RecyclerView.Adapter<TabLayoutAdapter.SimpleViewHolder> {

    private final CustomViewPagerAdapter pagerAdapter;

    private final TabLayoutAdapter.TabClickListener mItemClickListener;


    public TabLayoutAdapter(CustomViewPagerAdapter adapter, TabClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;

        pagerAdapter = adapter;
    }

    @NonNull
    @Override
    public TabLayoutAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new TextView(parent.getContext());
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabLayoutAdapter.SimpleViewHolder holder, int position) {

        holder.textView.setTag("View" + position);

        holder.textView.setText(pagerAdapter.getPageTitle(position));
    }

    @Override
    public int getItemCount() {
        return pagerAdapter.getCount();
    }

    public interface TabClickListener {
        void onTabClickListener(int itemId, TextView tag);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView textView;

        SimpleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            textView = (TextView) itemView;
            textView.setAllCaps(true);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setPadding(10, 10, 10, 10);

        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onTabClickListener(getAdapterPosition(), (TextView) view);
        }
    }
}
