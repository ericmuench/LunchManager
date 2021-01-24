package com.emuench.mittagessenverwaltung.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Toast;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.controller_adapters.PersonAdapter;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonManagementActivity extends BaseActivity
{
    //MemberVariables
    private List<Person> personList;
    private RecyclerView personRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PersonAdapter personAdapter;


    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_management);

        personList = new ArrayList<>(DataStorage.getInstance().getPersonsEditable());
        System.out.println(personList);

        Collections.sort(personList);

        buildRecyclerView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        personList = new ArrayList<>(DataStorage.getInstance().getPersons());
        Collections.sort(personList);
        buildRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_person_management,menu);

        MenuItem searchItem = menu.findItem(R.id.person_searchPerson);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
                personAdapter.getFilter().filter(s);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.person_addPerson:
                startActivity(new Intent(this,PersonEditActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    //private Methods
    private void buildRecyclerView()
    {
        personRecyclerView = findViewById(R.id.all_persons);
        personAdapter = new PersonAdapter(personList,this);
        layoutManager = new LinearLayoutManager(this);
        personRecyclerView.setHasFixedSize(true);

        personRecyclerView.setLayoutManager(layoutManager);
        personRecyclerView.setAdapter(personAdapter);

        personAdapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClicked(int pos)
            {
                Intent intent = new Intent(PersonManagementActivity.this,PersonEditActivity.class);

                Person p = personList.get(pos);
                intent.putExtra("person",p);
                startActivity(intent);
            }
        });

    }







}
