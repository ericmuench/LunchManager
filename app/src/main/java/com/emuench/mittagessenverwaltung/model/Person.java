package com.emuench.mittagessenverwaltung.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Person implements Parcelable, Comparable<Person>
{
    //Member Variables
    private String name;
    private List<String> notLikedIngredients;
    private List<Dish> likedDishes;

    //Constructors
    public Person(String name, List<String> notLikedIngredients)
    {
        this.name = name;
        this.notLikedIngredients = notLikedIngredients;
        likedDishes = new ArrayList<>();
    }

    public Person(String name)
    {
        this(name, new ArrayList<String>());
    }

    private Person(Parcel in)
    {
        this.name = in.readString();
        System.out.println(this.name);
        //in.readStringList(notLikedIngredients);
        int numOfIngredients = in.readInt();
        System.out.println(numOfIngredients);

        this.notLikedIngredients = new ArrayList<>();

        for(int i = 0;i < numOfIngredients;i++)
        {
            notLikedIngredients.add(in.readString());
        }

        System.out.println(notLikedIngredients);

        int numofDishes = in.readInt();

        this.likedDishes = new ArrayList<>();

        for(int i = 0;i < numofDishes;i++)
        {
            likedDishes.add((Dish)in.readParcelable(Dish.class.getClassLoader()));
        }

        //Dish[] dishes = (Dish[])in.readParcelableArray(Dish.class.getClassLoader());
        //this.likedDishes = Arrays.asList(dishes);
    }

    //Getter
    public String getName()
    {
        return name;
    }

    public List<String> getNotLikedIngredients()
    {
        return notLikedIngredients;
    }

    public List<Dish> getLikedDishes()
    {
        return likedDishes;
    }

    //Setter
    public void setName(String name)
    {
        this.name = name;
    }

    public void setNotLikedIngredients(List<String> notLikedIngredients)
    {
        this.notLikedIngredients = notLikedIngredients;
    }

    //Methods
    public Person addLikedDish(Dish d)
    {
        if(d != null)
        {
            getLikedDishes().add(d);
        }

        Collections.sort(getLikedDishes());

        return this;
    }

    public boolean addLikedDishes(Dish... dishes)
    {
        int oldSize = getLikedDishes().size();

        for(Dish d: dishes)
        {
            if(d != null)
            {
                getLikedDishes().add(d);
            }
        }

        Collections.sort(getLikedDishes());

        return getLikedDishes().size() > oldSize;


    }

    @Override
    public boolean equals(Object o)
    {
        /*
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) &&
                Objects.equals(getNotLikedIngredients(), person.getNotLikedIngredients()) &&
                Objects.equals(getLikedDishes(), person.getLikedDishes());
                */
        if(!(o instanceof Person))
        {
            return false;
        }
        else
        {
            Person p = (Person) o;

            if(p.hashCode() != this.hashCode())
            {
                return false;
            }

            return (this.name.equals(p.getName()) && this.likedDishes.equals(p.getLikedDishes()) && this.notLikedIngredients.equals(p.getNotLikedIngredients()));

        }
    }

    @Override
    public int hashCode()
    {
        int hash = 0;

        hash += 31*getName().hashCode();
        hash += 31*getLikedDishes().hashCode();
        hash += 31*getLikedDishes().hashCode();

        return hash;
    }


    @Override
    public String toString()
    {
        return getName();
    }

    //Comparable Interface Methods
    @Override
    public int compareTo(Person person)
    {
        if(person.equals(this))
        {
            return 0;
        }

        return getName().compareTo(person.getName());
    }

    //Parcelable Interface Methods
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeInt(notLikedIngredients.size());
        for(String s: notLikedIngredients)
        {
            dest.writeString(s);
        }
        //dest.writeStringArray(getNotLikedIngredients().toArray(new String[getNotLikedIngredients().size()]));
        //dest.writeStringList(notLikedIngredients);
        dest.writeInt(likedDishes.size());
        for(Dish d: likedDishes)
        {
            dest.writeParcelable(d,flags);
        }
        //dest.writeParcelableArray(likedDishes.toArray(new Dish[likedDishes.size()]),flags);
    }

    //Parcelable Creator
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>()
    {
        public Person createFromParcel(Parcel in)
        {
            return new Person(in);
        }

        public Person[] newArray(int size)
        {
            return new Person[size];
        }
    };
}
