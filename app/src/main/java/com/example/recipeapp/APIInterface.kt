package com.example.recipeapp

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @GET("recipes/")
    fun getRecipes(): Call<Recipes>

    @POST("recipes/")
    fun postRecipe(@Body userRecipe: RecipesItem): Call<RecipesItem>
}