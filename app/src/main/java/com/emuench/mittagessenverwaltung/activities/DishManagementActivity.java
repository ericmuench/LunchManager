package com.emuench.mittagessenverwaltung.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.controller_adapters.DishListAdapter;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DishManagementActivity extends BaseActivity
{
    //Variables
    private List<Dish> dishList;
    private RecyclerView dishView;
    private DishListAdapter dishListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_management);

        dishList = new ArrayList<>(DataStorage.getInstance().getDishes());

        buildRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_dish_management,menu);
        MenuItem searchItem = menu.findItem(R.id.item_searchDish);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                dishListAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        System.out.println("ON RESUME");
        dishList = new ArrayList<>(DataStorage.getInstance().getDishes());
        Collections.sort(dishList);
        buildRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_addDish:
                startActivity(new Intent(this,DishEditActivity.class));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void buildRecyclerView()
    {
        dishView = findViewById(R.id.all_dishes);
        dishListAdapter = new DishListAdapter(dishList,this);
        layoutManager = new LinearLayoutManager(this);
        dishListAdapter.setOnItemClickListener(new DishListAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClicked(int position)
            {
                Intent intent = new Intent(DishManagementActivity.this,DishEditActivity.class);
                intent.putExtra("dish",dishList.get(position));
                startActivity(intent);

            }
        });

        dishView.setHasFixedSize(true);
        dishView.setLayoutManager(layoutManager);
        dishView.setAdapter(dishListAdapter);

    }


}
