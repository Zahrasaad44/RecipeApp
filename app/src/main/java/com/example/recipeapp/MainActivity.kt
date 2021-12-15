package com.example.recipeapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userRecipes: Recipes
    private lateinit var recyclerAdapter: RecipeAdapter

   private val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRecipes = Recipes()
        recyclerAdapter = RecipeAdapter(userRecipes)
        binding.recipesRV.adapter = recyclerAdapter
        binding.recipesRV.layoutManager = LinearLayoutManager(this)
        // binding.recipesRV.scrollToPosition()

        // val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        apiInterface?.getRecipes()?.enqueue(object : Callback<Recipes> {
            override fun onResponse(call: Call<Recipes>, response: Response<Recipes>) {
                userRecipes = response.body()!!
                recyclerAdapter.updateRecyclerView(userRecipes)
            }

            override fun onFailure(call: Call<Recipes>, t: Throwable) {
                Log.d("main", "Unable to get recipes $t")
            }
        })

        binding.addRecipeBtn.setOnClickListener { showAddRecipeDialog() }
    }

    private fun showAddRecipeDialog() {
        val addRecipeDialog = Dialog(this)
        addRecipeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        addRecipeDialog.setCancelable(false)
        addRecipeDialog.setContentView(R.layout.add_recipe_dialog)

        val submitBtn = addRecipeDialog.findViewById(R.id.submitBtn) as Button
        val cancelBtn = addRecipeDialog.findViewById(R.id.cancelBtn) as Button
        val titleET = addRecipeDialog.findViewById(R.id.titleET) as EditText
        val authorET = addRecipeDialog.findViewById(R.id.authorET) as EditText
        val ingredientsET = addRecipeDialog.findViewById(R.id.ingredientsET) as EditText
        val instructionsET = addRecipeDialog.findViewById(R.id.instructionsET) as EditText

        submitBtn.setOnClickListener {
            apiInterface!!.postRecipe(
                RecipesItem(
                    authorET.text.toString(),
                    ingredientsET.text.toString(),
                    instructionsET.text.toString(),
                    0,
                    titleET.text.toString()
                )
            ).enqueue(object : Callback<RecipesItem> {
                override fun onResponse(call: Call<RecipesItem>, response: Response<RecipesItem>) {
                    Toast.makeText(this@MainActivity, "Recipe added!", Toast.LENGTH_LONG).show()
                    recreate()
                    //binding.recipesRV.scrollToPosition(userRecipes.size -1) //Note to myself: It didn't work
                }

                override fun onFailure(call: Call<RecipesItem>, t: Throwable) {
                    Log.d("main", "Failed to add recipe")
                    Toast.makeText(this@MainActivity, "Failed to add Recipe", Toast.LENGTH_LONG).show()
                }

            })
            addRecipeDialog.dismiss()
        }

        cancelBtn.setOnClickListener { addRecipeDialog.dismiss() }

        addRecipeDialog.show()
    }
}