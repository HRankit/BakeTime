package com.udacity.baketime.baketime.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.baketime.baketime.R;
import com.udacity.baketime.baketime.models.RecipeResult;

import java.util.List;

import static com.udacity.baketime.baketime.misc.MiscFunctions.isEmpty;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {
    private final List<RecipeResult> resultList;

    private final ItemClickListener mItemClickListener;


    public RecipeListAdapter(List<RecipeResult> resultList, ItemClickListener mItemClickListener) {
        this.resultList = resultList;


        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecipeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_recipe_item, parent, false);

        return new RecipeListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecipeResult rr = resultList.get(position);
        holder.bind(rr);

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public interface ItemClickListener {
        void onItemClickListener(int itemId, RecipeResult rr);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CardView recipeCardView;
        final TextView recipeName;
        final ImageView image_food;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeCardView = itemView.findViewById(R.id.recipeCardView);
            image_food = itemView.findViewById(R.id.iv_measure);
        }

        void bind(final RecipeResult recipe) {
            if (!isEmpty(recipe.getImage())) {
                Picasso.get().load(recipe.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(image_food);
            }


            recipeName.setText(recipe.getName());

        }


        @Override
        public void onClick(View view) {
            int elementId = resultList.get(getAdapterPosition()).getId();
            RecipeResult rr = resultList.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(elementId, rr);
        }
    }

}
