package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RecipeRowBinding

class RecipeAdapter(var userRecipes: Recipes): RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(val binding: RecipeRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
       return RecipeViewHolder(RecipeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
       val reci = userRecipes[position]

        holder.binding.apply {
            titleTV.text = reci.title
            authorTV.text = reci.author
            ingredientsTV.text = reci.ingredients
            instructionsTV.text = reci.instructions

           // nameLocationCV.setOnClickListener {  }
        }
    }

    override fun getItemCount(): Int {
        return userRecipes.size
    }

    fun updateRecyclerView(userInput: Recipes) {
        this.userRecipes = userInput
        notifyDataSetChanged()
    }
}