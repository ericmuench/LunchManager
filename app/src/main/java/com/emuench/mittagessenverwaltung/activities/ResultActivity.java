package com.emuench.mittagessenverwaltung.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.controller_adapters.DishListAdapter;
import com.emuench.mittagessenverwaltung.model.Dish;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends BaseActivity
{

    //Member Variables
    private TextView txt_no_result_information;
    private List<Dish> likedDishes;
    private RecyclerView likedDishesRecyclerView;
    private DishListAdapter dishListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //finding gui components
        txt_no_result_information = findViewById(R.id.txt_no_result_information);

        //getting likedDishes-List asParcelable List from Intent
        ArrayList<Parcelable> parcelables = getIntent().getParcelableArrayListExtra("likedDishes");

        //instanciating liked-Dishes List
        likedDishes = new ArrayList<>();

        if(parcelables != null)
        {
            for(Parcelable p : parcelables)
            {
                Dish d = (Dish) p;
                likedDishes.add(d);
            }
        }

        //show if no result were found
        if(!likedDishes.isEmpty())
        {
            txt_no_result_information.setHeight(0);
        }

        buildRecyclerView();

    }

    //private Methods
    private void buildRecyclerView()
    {
        likedDishesRecyclerView = findViewById(R.id.likedDishRecyclerView);
        dishListAdapter = new DishListAdapter(likedDishes,this);
        layoutManager = new LinearLayoutManager(this);
        likedDishesRecyclerView.setHasFixedSize(true);

        dishListAdapter.setOnItemClickListener(new DishListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClicked(int position)
            {
                Dish dish = likedDishes.get(position);
                if(dish != null && !(dish.getRecipeLink()).isEmpty())
                {
                    try
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(dish.getRecipeLink()));
                        startActivity(browserIntent);
                    }
                    catch(ActivityNotFoundException ex)
                    {
                        ex.printStackTrace();
                        Toast.makeText(ResultActivity.this,getResources().getString(R.string.warning_invalid_weblink),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        likedDishesRecyclerView.setLayoutManager(layoutManager);
        likedDishesRecyclerView.setAdapter(dishListAdapter);
    }
}
