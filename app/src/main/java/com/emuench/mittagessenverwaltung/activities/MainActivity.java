package com.emuench.mittagessenverwaltung.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.controller_adapters.PersonCheckAdapter;
import com.emuench.mittagessenverwaltung.model.DataManager;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Dish;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity
{
    private ListView availiblePersonsView;
    private Button lunchGeneration;
    private PersonCheckAdapter personCheckAdapter;
    private ArrayList<Person> sharedSelectedPersons;
    private List<Person> persons;

    private static boolean isFirstStart = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //restoring data from sharedPreferences if given
        if(isFirstStart)
        {
            new DataManager(this).loadData();
            isFirstStart = false;
        }

        //finding gui components
        availiblePersonsView = findViewById(R.id.avaliblePersons);
        lunchGeneration = findViewById(R.id.lunchGeneration);

        persons = DataStorage.getInstance().getPersons();
        Collections.sort(persons);


        //restoring selected Persons
        if(savedInstanceState == null)
        {
            sharedSelectedPersons = new ArrayList<>();
        }
        else
        {
            sharedSelectedPersons = savedInstanceState.getParcelableArrayList("sharedSelectedPersons");

            if(sharedSelectedPersons == null)
            {
                sharedSelectedPersons = new ArrayList<>();
            }
        }


        //instanciating and assigning listview adapter
        personCheckAdapter = new PersonCheckAdapter(persons,sharedSelectedPersons,this);
        availiblePersonsView.setAdapter(personCheckAdapter);

        availiblePersonsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println(personCheckAdapter.getItem(position));
            }
        });

    }

    //Optionsmenu / Menubar
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.manage_persons:
                Intent changeToPersonManagement = new Intent(MainActivity.this,PersonManagementActivity.class);
                startActivity(changeToPersonManagement);
                return true;

            case R.id.manage_dishes:
                startActivity(new Intent(MainActivity.this,DishManagementActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        persons = DataStorage.getInstance().getPersons();
        Collections.sort(persons);
        personCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    //Save Instance State before changing to portrait or landscape mode
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList("sharedSelectedPersons",sharedSelectedPersons);

        super.onSaveInstanceState(outState);
    }

    //Result / Button Click Method
    public void showResults(View view)
    {
        Intent intent = new Intent(MainActivity.this,ResultActivity.class);
        intent.putExtra("likedDishes",findOutLikedDishes());
        startActivity(intent);
    }

    //private Methods
    private ArrayList<Dish> findOutLikedDishes()
    {
        ArrayList<Dish> likedDishes = new ArrayList<>();

        if(sharedSelectedPersons != null && !sharedSelectedPersons.isEmpty())
        {
            List<Dish> firstPersonsDishes = sharedSelectedPersons.get(0).getLikedDishes();

            boolean isLikedByAll;

            //checking if dish from first person is liked by all others too
            for(Dish dish : firstPersonsDishes)
            {
                isLikedByAll = true;

                for(Person p : sharedSelectedPersons)
                {
                    if(!p.getLikedDishes().contains(dish))
                    {
                        isLikedByAll = false;
                        break;
                    }
                }

                if(isLikedByAll)
                {
                    likedDishes.add(dish);
                }
            }
        }


        return likedDishes;
    }
}
