package com.udacity.baketime.baketime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.baketime.baketime.R;
import com.udacity.baketime.baketime.models.Ingredient;

import java.util.List;

import static com.udacity.baketime.baketime.misc.MiscFunctions.CapitalizeString;

public class IngredientsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Ingredient> resultList;
    private final Context mContext;
    private final String recipeName;


    public IngredientsListAdapter(Context context, List<Ingredient> resultList, String name) {
        this.resultList = resultList;
        this.mContext = context;
        this.recipeName = name;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = new TextView(parent.getContext());
            return new IngredientsListAdapter.SimpleViewHolder(view);

        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_ingredient_list, parent, false);

            return new IngredientsListAdapter.MyViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            if (holder instanceof IngredientsListAdapter.SimpleViewHolder) {
                ((SimpleViewHolder) holder).textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                ((SimpleViewHolder) holder).textView.setText(recipeName);
            }

        } else {
            if (holder instanceof IngredientsListAdapter.MyViewHolder) {
                Ingredient rr = resultList.get(position);

                ((MyViewHolder) holder).bind(rr);

            }
        }

    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        SimpleViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView;
            textView.setAllCaps(true);
            textView.setId(R.id.recipe_name);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            textView.setPadding(10, 10, 10, 10);

        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ImageView iv_measure;
        private final TextView tv_ingredient;
        private final TextView tv_quantity;
        private final TextView tv_measure_name;

        MyViewHolder(View itemView) {
            super(itemView);
            iv_measure = itemView.findViewById(R.id.iv_measure);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_measure_name = itemView.findViewById(R.id.tv_measure_name);
            tv_ingredient = itemView.findViewById(R.id.tv_ingredient);
        }

        void bind(final Ingredient ingredient) {


            Picasso.get().load(processMeasureDrawable(ingredient.getMeasure()))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(iv_measure);


            tv_ingredient.setText(CapitalizeString((ingredient.getIngredient())));
            tv_quantity.setText(ingredient.getQuantity());
            tv_measure_name.setText(processMeasureString(ingredient.getMeasure()));

        }


        int processMeasureDrawable(String measure) {
            int returnint;
            switch (measure) {
                case "CUP":
                    returnint = R.mipmap.cup;
                    break;
                case "TBLSP":
                    returnint = R.mipmap.tablespoon;
                    break;
                case "TSP":
                    returnint = R.mipmap.teaspoon;
                    break;
                case "K":
                    returnint = R.mipmap.kg;
                    break;
                case "G":
                    returnint = R.mipmap.gram;
                    break;
                case "OZ":
                    returnint = R.mipmap.oz;
                    break;
                case "UNIT":
                    returnint = R.mipmap.unit;
                    break;
                default:
                    returnint = R.drawable.ic_launcher_background;
                    break;

            }

            return returnint;
        }

        String processMeasureString(String measure) {
            String returnString;
            switch (measure) {
                case "CUP":
                    returnString = mContext.getResources().getString(R.string.label_cup);
                    break;
                case "TBLSP":
                    returnString = mContext.getResources().getString(R.string.label_tblspn);
                    break;
                case "TSP":
                    returnString = mContext.getResources().getString(R.string.label_teaspn);
                    break;
                case "K":
                    returnString = mContext.getResources().getString(R.string.label_kg);
                    break;
                case "G":
                    returnString = mContext.getResources().getString(R.string.label_gram);
                    break;
                case "OZ":
                    returnString = mContext.getResources().getString(R.string.label_ounce);
                    break;
                default:
                    returnString = measure;
                    break;

            }

            return returnString;
        }

    }


}