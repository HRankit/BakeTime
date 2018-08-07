package com.udacity.baketime.baketime.network;


import com.udacity.baketime.baketime.models.RecipeResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface GetDataService {

    @GET("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipeResult>> getRecipes();


}