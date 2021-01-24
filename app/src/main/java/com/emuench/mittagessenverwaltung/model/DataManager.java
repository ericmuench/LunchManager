package com.emuench.mittagessenverwaltung.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.emuench.mittagessenverwaltung.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataManager
{
    //MemberVariables
    private Gson gson;
    private Context context;
    private SharedPreferences sharedPreferences;

    //Constructors
    public DataManager(@NonNull Context context)
    {
        this.context = context;
        gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //Methods
    public void saveData()
    {
        if(sharedPreferences != null)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String personsJSON = gson.toJson(DataStorage.getInstance().getPersonsEditable());
            editor.putString(context.getResources().getString(R.string.data_key_persons),personsJSON);

            String dishesJSON = gson.toJson(DataStorage.getInstance().getDishesEditable());
            editor.putString(context.getResources().getString(R.string.data_key_dishes),dishesJSON);

            editor.apply();
        }
    }

    public boolean loadData()
    {
        if(sharedPreferences != null && context != null)
        {
            //Persons
            String personsJSON = sharedPreferences.getString(context.getResources().getString(R.string.data_key_persons),null);

            if(personsJSON != null)
            {
                Type type = new TypeToken<ArrayList<Person>>(){}.getType();
                ArrayList<Person> personArrayList = gson.fromJson(personsJSON,type);

                if(personArrayList != null)
                {
                    DataStorage.getInstance().setPersonsEditable(personArrayList);
                }
                else
                {
                    System.err.println("Fehler beim Laden: personArrayList war null");
                    return false;
                }
            }
            else
            {
                System.err.println("Fehler beim Laden: personJSON war null");
                return false;
            }

            //Dishes
            String dishesJSON = sharedPreferences.getString(context.getResources().getString(R.string.data_key_dishes),null);

            if(dishesJSON != null)
            {
                Type type = new TypeToken<ArrayList<Dish>>(){}.getType();
                ArrayList<Dish> dishes = gson.fromJson(dishesJSON,type);

                if(dishes != null)
                {
                    DataStorage.getInstance().setDishesEditable(dishes);
                }
                else
                {
                    System.err.println("Fehler beim Laden: dishes war null");
                    return false;
                }

            }
            else
            {
                System.err.println("Fehler beim Laden: dishesJSON war null");
                return false;
            }


            //sucess
            System.out.println("Laden erfolgreich");
            return true;

        }
        else
        {
            System.err.println("Laden fehlgeschlagen. Sharedprefs oder context waren null");
            return false;
        }
    }
}
