package com.emuench.mittagessenverwaltung.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersonEditActivity extends BaseActivity
{
    private LinearLayout notLikedIngredientsFields;
    private List<EditText> notLikedIngrediensEditTexts;
    private EditText etxt_enter_person_name;
    private Button btn_delete_person;
    private Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);

        person = getIntent().getParcelableExtra("person");
        System.out.println(person);

        btn_delete_person = findViewById(R.id.btn_delete_person);

        if(person == null)
        {
            btn_delete_person.setEnabled(false);
            btn_delete_person.setBackgroundColor(getResources().getColor(R.color.componentUnavalible));
            btn_delete_person.setTextColor(getResources().getColor(R.color.text_unavalible));
        }
        else
        {
            btn_delete_person.setEnabled(true);
            btn_delete_person.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
            btn_delete_person.setTextColor(getResources().getColor(R.color.defaultWhite));
        }

        etxt_enter_person_name = findViewById(R.id.person_enter_person_name);
        notLikedIngredientsFields = findViewById(R.id.notLikedIngredients_fields);

        notLikedIngrediensEditTexts = new ArrayList<>();

        EditText notLikedIngredient1 = findViewById(R.id.editText_notLikedIngredient1);
        EditText notLikedIngredient2 = findViewById(R.id.editText_notLikedIngredient2);
        EditText notLikedIngredient3 = findViewById(R.id.editText_notLikedIngredient3);

        notLikedIngrediensEditTexts.add(notLikedIngredient1);
        notLikedIngrediensEditTexts.add(notLikedIngredient2);
        notLikedIngrediensEditTexts.add(notLikedIngredient3);

        if(savedInstanceState != null)
        {
            restoreNotLikedIngredientsFields(savedInstanceState.getStringArrayList("notLikedIngredientsTexts"),savedInstanceState.getInt("numberFields_NotLikedIngredients"));
        }

        fillGuiComponentsWithData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_edit_person,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_savePerson:

                if(etxt_enter_person_name.getText().toString().isEmpty())
                {
                    Toast.makeText(this,R.string.warning_invalidPersonName,Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(person != null)
                {
                    List<Person> personsInDataStorage = DataStorage.getInstance().getPersons();
                    Person target = personsInDataStorage.get(personsInDataStorage.indexOf(person));
                    target.setName(etxt_enter_person_name.getText().toString());
                    target.setNotLikedIngredients(collectNotLikedIngredients());
                }
                else
                {
                    Person person = new Person(etxt_enter_person_name.getText().toString(),collectNotLikedIngredients());
                    DataStorage.getInstance().getPersonsEditable().add(person);
                }

                //startActivity(new Intent(this,PersonManagementActivity.class));

                finish();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        ArrayList<String> notLikedIngredientsTexts = new ArrayList<>();

        for(EditText e : notLikedIngrediensEditTexts)
        {
            notLikedIngredientsTexts.add(e.getText().toString());
        }

        outState.putStringArrayList("notLikedIngredientsTexts",notLikedIngredientsTexts);
        outState.putInt("numberFields_NotLikedIngredients",notLikedIngrediensEditTexts.size()-3);

        super.onSaveInstanceState(outState);
    }

    //BUTTON Method fpr deleting person
    public void deletePerson(View view)
    {
            new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.warning_head))
                .setMessage(R.string.warning_msg_delete_person)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(person != null)
                        {
                            int targetIndex = DataStorage.getInstance().getPersons().indexOf(person);
                            DataStorage.getInstance().getPersonsEditable().remove(targetIndex);
                            finish();
                            //startActivity(new Intent(PersonEditActivity.this,PersonManagementActivity.class));
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    //fill Gui Components with data
    private void fillGuiComponentsWithData()
    {
        if(person != null)
        {
            List<String> nlIngrOfPerson = person.getNotLikedIngredients();

            etxt_enter_person_name.setText(person.getName());

            while(notLikedIngrediensEditTexts.size() < nlIngrOfPerson.size())
            {
                addFieldsToNotLikedIngredients();
            }

            for(int i = 0;i < nlIngrOfPerson.size();i++)
            {
                notLikedIngrediensEditTexts.get(i).setText(nlIngrOfPerson.get(i));
            }
        }
    }

    //adding fields to notlikedingredients
    public void btnAddFieldsToNotLikedIngredients(View view)
    {
        addFieldsToNotLikedIngredients();
    }

    private void addFieldsToNotLikedIngredients()
    {
        if(notLikedIngredientsFields != null)
        {
            EditText newOne1 = new EditText(this);
            newOne1.setHint(R.string.information_enter_notLikedIngredient);

            EditText newOne2 = new EditText(this);
            newOne2.setHint(R.string.information_enter_notLikedIngredient);

            EditText newOne3 = new EditText(this);
            newOne3.setHint(R.string.information_enter_notLikedIngredient);

            notLikedIngredientsFields.addView(newOne1);
            notLikedIngredientsFields.addView(newOne2);
            notLikedIngredientsFields.addView(newOne3);

            notLikedIngrediensEditTexts.add(newOne1);
            notLikedIngrediensEditTexts.add(newOne2);
            notLikedIngrediensEditTexts.add(newOne3);
        }
    }

    //restoring fields of not liked ingredients
    private void restoreNotLikedIngredientsFields(List<String> texts,int numberAdditionalEditTexts)
    {
        int howOftenAddMethod = numberAdditionalEditTexts / 3;

        if(notLikedIngrediensEditTexts != null)
        {
            for(int i = 0;i < howOftenAddMethod;i++)
            {
                addFieldsToNotLikedIngredients();
            }

            for (int i = 0;i < notLikedIngrediensEditTexts.size();i++)
            {
                notLikedIngrediensEditTexts.get(i).setText(texts.get(i));
            }
        }
    }

    //collect not liked ingredients for saving person
    private List<String> collectNotLikedIngredients()
    {
        List<String> nLIngr = new LinkedList<>();


        for(EditText e : notLikedIngrediensEditTexts)
        {
            if(e != null && !(e.getText().toString().isEmpty()))
            {
                nLIngr.add(e.getText().toString());
            }
        }

        return nLIngr;
    }


}
