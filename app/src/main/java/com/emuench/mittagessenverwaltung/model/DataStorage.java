package com.emuench.mittagessenverwaltung.model;

import com.emuench.mittagessenverwaltung.util.ReadOnlyList;

import java.util.ArrayList;
import java.util.List;

public class DataStorage
{
    //MemberVariables
    private ArrayList<Person> persons;
    private ArrayList<Dish> dishes;

    private ReadOnlyList<Person> personsReadOnly;
    private ReadOnlyList<Dish> dishReadOnly;

    private static DataStorage instance = new DataStorage();

    //Constructors
    private DataStorage()
    {
        persons = new ArrayList<>();
        dishes = new ArrayList<>();
        personsReadOnly = new ReadOnlyList<>(persons);
        dishReadOnly = new ReadOnlyList<>(dishes);
        //setTestData();
    }

    //Getter
    public static synchronized DataStorage getInstance()
    {
        return instance;
    }

    public ReadOnlyList<Person> getPersons()
    {
        personsReadOnly.setList(persons);
        return personsReadOnly;
    }

    public ReadOnlyList<Dish> getDishes()
    {
        dishReadOnly.setList(dishes);
        return dishReadOnly;
    }

    public ArrayList<Person> getPersonsEditable()
    {
        return persons;
    }

    public ArrayList<Dish> getDishesEditable()
    {
        return dishes;
    }

    //Setter
    public void setPersonsEditable(ArrayList<Person> persons)
    {
        this.persons = persons;
        personsReadOnly.setList(this.persons);
    }

    public void setDishesEditable(ArrayList<Dish> dishes)
    {
        this.dishes = dishes;
        dishReadOnly.setList(this.dishes);
    }

    private void setTestData()
    {
        //testData
        Dish nudeln = null;
        try
        {
            nudeln = new Dish("Nudeln","Buch2",89);
        }
        catch (DishException ex)
        {
            ex.printStackTrace();
            nudeln = new Dish("Nudeln");
        }

        Dish spinat = new Dish("Spinat");
        Dish banane = new Dish("Banane","https://de.wikipedia.org/wiki/Bananen");
        Dish leber = new Dish("Leber");
        Dish eierkuchen = new Dish("Eierkuchen");

        dishes.add(spinat);
        dishes.add(banane);
        dishes.add(leber);
        dishes.add(eierkuchen);
        dishes.add(nudeln);


        Person eric = new Person("Eric");
        eric.addLikedDish(nudeln).addLikedDish(eierkuchen).addLikedDishes(banane,leber);
        eric.getNotLikedIngredients().add("Selerie");
        Person annika = new Person("Annika");
        annika.addLikedDish(nudeln).addLikedDish(spinat).addLikedDish(banane);
        annika.getNotLikedIngredients().add("Milch");
        annika.getNotLikedIngredients().add("Mais");
        annika.getNotLikedIngredients().add("Salzhering");
        annika.getNotLikedIngredients().add("Sushi");
        annika.getNotLikedIngredients().add("Apfelmus");
        annika.getNotLikedIngredients().add("Kokosfett");
        annika.getNotLikedIngredients().add("Brocolli");
        annika.getNotLikedIngredients().add("Bauchspeck");
        annika.getNotLikedIngredients().add("irgendwas anderes");

        persons = new ArrayList<>();
        persons.add(new Person("Mama"));
        persons.add(new Person("Papa"));
        persons.add(eric);
        persons.add(annika);
        persons.add(new Person("Omi"));
        persons.add(new Person("Opa"));
    }

    //Methods
    public ArrayList<Person> findPersonsThatLikeDish(Dish dish)
    {
        ArrayList<Person> result = new ArrayList<>();

        if(dish == null)
        {
            return result;
        }

        for(Person p : getPersons())
        {
            if(p.getLikedDishes().contains(dish))
            {
                result.add(p);
            }
        }

        return result;
    }


}
