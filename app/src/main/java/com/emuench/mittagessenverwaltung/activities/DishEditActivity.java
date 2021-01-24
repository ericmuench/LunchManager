package com.emuench.mittagessenverwaltung.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.controller_adapters.PersonCheckAdapterForRecyclerView;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Dish;
import com.emuench.mittagessenverwaltung.model.DishException;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.ArrayList;
import java.util.List;

public class DishEditActivity extends BaseActivity
{

    //Variables
    private Dish dish;

    private EditText dishTitle;
    private EditText bookTitle;
    private EditText bookSite;
    private EditText weblink;
    private Button btn_delete;
    private Button btn_showRecipeInWeb;

    private PersonCheckAdapterForRecyclerView personAdapter;
    private RecyclerView personView;
    private RecyclerView.LayoutManager personsLayoutManager;
    private List<Person> personList;
    private ArrayList<Person> sharedPersonData;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_edit);

        dish = getIntent().getParcelableExtra("dish");
        personList = DataStorage.getInstance().getPersons();

        dishTitle = findViewById(R.id.enter_dish_title);
        bookTitle = findViewById(R.id.book_title_enter);
        bookSite = findViewById(R.id.book_site_enter);
        weblink = findViewById(R.id.weblink_editText);
        btn_delete = findViewById(R.id.delete_Dish);
        btn_showRecipeInWeb = findViewById(R.id.show_RecipeInWeb);

        if(dish == null)
        {
            btn_delete.setEnabled(false);
            btn_delete.setBackgroundColor(getResources().getColor(R.color.componentUnavalible));
            btn_delete.setTextColor(getResources().getColor(R.color.text_unavalible));
        }
        else
        {
            btn_delete.setEnabled(true);
            btn_delete.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            btn_delete.setTextColor(getResources().getColor(R.color.defaultWhite));
        }

        weblink.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() == 0)
                {
                    changeEnabledStateOfBtnShowRecipeInWeb(false);
                }
                else
                {
                    changeEnabledStateOfBtnShowRecipeInWeb(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


        if(savedInstanceState == null)
        {
            sharedPersonData = DataStorage.getInstance().findPersonsThatLikeDish(dish);
        }
        else
        {
            sharedPersonData = savedInstanceState.getParcelableArrayList("dish_sharedSelectedPersons");

            if(sharedPersonData == null)
            {
                sharedPersonData = new ArrayList<>();
            }
        }

        buildRecyclerView();

        fillViewWithData(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_dish_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_saveDish:

                List<Person> allPersons = DataStorage.getInstance().getPersonsEditable();

                if(dish == null)
                {
                    if(!(isInvalidInput()))
                    {
                        try
                        {
                            int bSite = (bookSite.getText().toString().isEmpty())? 0 : Integer.parseInt(bookSite.getText().toString());

                            Dish dish = new Dish(dishTitle.getText().toString(),bookTitle.getText().toString(),bSite,weblink.getText().toString());

                            for(Person p : sharedPersonData)
                            {
                                allPersons.get(allPersons.indexOf(p)).getLikedDishes().add(dish);
                            }

                            DataStorage.getInstance().getDishesEditable().add(dish);

                            //startActivity(new Intent(DishEditActivity.this,DishManagementActivity.class));

                            finish();
                        }
                        catch(DishException ex)
                        {
                            ex.printStackTrace();
                            Toast.makeText(this,R.string.warning_saving_error,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Dish d = DataStorage.getInstance().getDishesEditable().get(DataStorage.getInstance().getDishes().indexOf(dish));

                    if(!(isInvalidInput()))
                    {
                        d.setTitle(dishTitle.getText().toString());
                        d.setBook(bookTitle.getText().toString());

                        if(!(bookSite.getText().toString().isEmpty()))
                        {
                            d.setBookSite(Integer.parseInt(bookSite.getText().toString()));
                        }
                        d.setRecipeLink(weblink.getText().toString());

                        for(Person p : allPersons)
                        {
                            if(sharedPersonData.contains(p) && !(p.getLikedDishes().contains(d)))
                            {
                                p.getLikedDishes().add(d);
                            }
                            else if(!(sharedPersonData.contains(p)) && p.getLikedDishes().contains(d))
                            {
                                p.getLikedDishes().remove(d);
                            }
                        }

                        //startActivity(new Intent(DishEditActivity.this,DishManagementActivity.class));
                        finish();
                    }



                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList("dish_sharedSelectedPersons",sharedPersonData);
        super.onSaveInstanceState(outState);
    }

    private void buildRecyclerView()
    {
        personView = findViewById(R.id.personsThatLikeDish_List);
        personAdapter = new PersonCheckAdapterForRecyclerView(personList,sharedPersonData,this);
        personsLayoutManager = new LinearLayoutManager(this);
        personAdapter.setOnItemClickListener(new PersonCheckAdapterForRecyclerView.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                Person p = personList.get(position);

                if(sharedPersonData.contains(p))
                {
                    sharedPersonData.remove(p);
                }
                else
                {
                    sharedPersonData.add(p);
                }
            }
        });
        personView.setHasFixedSize(true);
        personView.setLayoutManager(personsLayoutManager);
        personView.setAdapter(personAdapter);
    }

    private void fillViewWithData(Bundle savedInstanceState)
    {
        if(dish != null)
        {
            dishTitle.setText(dish.getTitle());

            if(!(dish.getBook() == null) && !(dish.getBook().isEmpty()))
            {
                bookTitle.setText(dish.getBook());
                bookSite.setText(Integer.toString(dish.getBookSite()));
            }

            if(!(dish.getRecipeLink() == null) && !(dish.getRecipeLink().isEmpty()))
            {
                weblink.setText(dish.getRecipeLink());
                changeEnabledStateOfBtnShowRecipeInWeb(true);
            }
            else
            {
                changeEnabledStateOfBtnShowRecipeInWeb(false);
            }

        }
        else
        {
            changeEnabledStateOfBtnShowRecipeInWeb(false);
        }
    }

    //Button Methods
    private void changeEnabledStateOfBtnShowRecipeInWeb(boolean enabled)
    {
        if(enabled)
        {
            btn_showRecipeInWeb.setEnabled(true);
            btn_showRecipeInWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_showRecipeInWeb.setTextColor(getResources().getColor(android.R.color.black));
        }
        else
        {
            btn_showRecipeInWeb.setEnabled(false);
            btn_showRecipeInWeb.setBackgroundColor(getResources().getColor(R.color.componentUnavalible));
            btn_showRecipeInWeb.setTextColor(getResources().getColor(R.color.text_unavalible));
        }
    }

    public void deleteDish(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.warning_head))
                .setMessage(R.string.warning_msg_delete_dish)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(dish != null)
                        {
                            List<Person> personsThatLikeDish = DataStorage.getInstance().findPersonsThatLikeDish(dish);

                            for(Person p : personsThatLikeDish)
                            {
                                p.getLikedDishes().remove(dish);
                            }

                            List<Dish> dishes = DataStorage.getInstance().getDishesEditable();
                            dishes.remove(dishes.indexOf(dish));
                            startActivity(new Intent(DishEditActivity.this,DishManagementActivity.class));
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showRecipeInWeb(View view)
    {
        String link = weblink.getText().toString();

        try
        {
            if(link != null && !(link.isEmpty()))
            {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(link));
                startActivity(webIntent);
            }

        }
        catch(ActivityNotFoundException exception)
        {
            exception.printStackTrace();
            Toast.makeText(this,getResources().getString(R.string.warning_invalid_weblink),Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInvalidInput()
    {
        if(dishTitle.getText().toString().isEmpty())
        {
            Toast.makeText(this,R.string.warning_invalid_title,Toast.LENGTH_SHORT).show();
            return true;
        }

        if(bookSite.getText().toString().contains("-"))
        {
            Toast.makeText(this,R.string.warning_invalid_booksite,Toast.LENGTH_SHORT).show();
            return true;
        }

        if (!(bookSite.getText().toString().isEmpty()) && bookSite.getText().toString().isEmpty())
        {
            Toast.makeText(this,R.string.warning_site_without_book,Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }


}
