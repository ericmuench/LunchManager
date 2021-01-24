package com.emuench.mittagessenverwaltung.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable,Comparable<Dish>
{
    //Member Variables
    private String title;
    private String book;
    private int bookSite;
    private String recipeLink;


    //Constructors
    public Dish(String title, String book, int bookSite, String link) throws DishException
    {
        this.title = title;
        this.book = book;
        this.recipeLink = link;

        if(bookSite < 0)
        {
            throw new DishException("Invalid Booknumber");
        }
        else
        {
            this.bookSite = bookSite;
        }

    }

    public Dish(String title, String book, int bookSite) throws DishException
    {
        this(title,book,bookSite,"");
    }

    public Dish(String title, String link)
    {
        this.title = title;
        this.book = "";
        this.bookSite = 0;
        this.recipeLink = link;
    }

    public Dish (String title)
    {
        this(title,"");
    }

    private Dish(Parcel in)
    {
        this.title = in.readString();
        this.book = in.readString();
        this.bookSite = in.readInt();
        this.recipeLink = in.readString();
    }

    //Getter
    public String getTitle()
    {
        return title;
    }

    public String getBook()
    {
        return book;
    }

    public int getBookSite()
    {
        return bookSite;
    }

    public String getRecipeLink()
    {
        return recipeLink;
    }

    //Setter


    public void setTitle(String title)
    {
        if(title != null && !(title.isEmpty()))
        {
            this.title = title;
        }

    }

    public void setBook(String book)
    {
        if(book != null)
        {
            this.book = book;
        }


    }

    public void setBookSite(int bookSite)
    {
        if(bookSite >= 0)
        {
            this.bookSite = bookSite;
        }

    }

    public void setRecipeLink(String recipeLink)
    {
        if(recipeLink != null)
        {
            this.recipeLink = recipeLink;
        }


    }

    //Methods
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Dish))
        {
            return false;
        }
        else
        {
            Dish dish = (Dish)o;

            if(dish.hashCode() != this.hashCode())
            {
                return false;
            }

            return (this.title.equals(dish.title) && this.book.equals(dish.getBook()) && this.bookSite == dish.getBookSite() && this.getRecipeLink().equals(dish.getRecipeLink()));

        }
    }

    @Override
    public int hashCode()
    {
        int hash = 0;

        hash += 31*getTitle().hashCode();
        hash += 31*getBook().hashCode();
        hash += 31*getBookSite();
        hash += 31*getRecipeLink().hashCode();

        return hash;
    }

    //Comparable Interface Methods
    @Override
    public int compareTo(Dish dish)
    {
        if(this.equals(dish))
        {
            return 0;
        }

        int cmpTitle = this.getTitle().compareTo(dish.getTitle());

        if(cmpTitle == 0)
        {
            int cmpBook = this.getBook().compareTo(dish.getBook());

            if(cmpBook == 0)
            {
                return dish.getBookSite()-this.getBookSite();
            }
            else
            {
                return cmpBook;
            }
        }
        else
        {
            return cmpTitle;
        }
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
        dest.writeString(title);
        dest.writeString(book);
        dest.writeInt(bookSite);
        dest.writeString(recipeLink);
    }

    //Parcelable Creator
    public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>()
    {
        public Dish createFromParcel(Parcel in)
        {
            return new Dish(in);
        }

        public Dish[] newArray(int size)
        {
            return new Dish[size];
        }
    };
}
