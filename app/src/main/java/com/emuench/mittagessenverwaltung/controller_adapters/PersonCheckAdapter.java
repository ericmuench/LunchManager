package com.emuench.mittagessenverwaltung.controller_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.List;

public class PersonCheckAdapter extends BaseAdapter
{
    //MemberVariables
    private List<Person> personList;
    private List<Person> sharedSelectedPersons;
    private Context context;
    private LayoutInflater inflater;


    //Constructors
    public PersonCheckAdapter(List<Person> personList,List<Person> sharedSelectedPersons, Context context)
    {
        this.personList = personList;
        this.sharedSelectedPersons = sharedSelectedPersons;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    //Methods
    @Override
    public int getCount()
    {
        return personList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = inflater.inflate(R.layout.checkable_person_list_item,parent,false);
        final Person person ;


        if(personList != null && !personList.isEmpty())
        {
            //Getting person at certain position
            person = personList.get(position);

            //getting checkbox and assingig name of person to it
            CheckBox cBox = convertView.findViewById(R.id.checkPerson);
            cBox.setText(person.getName());

            //set Checkbox as Selected if it in selected persons
            if(sharedSelectedPersons != null && sharedSelectedPersons.contains(person))
            {
                //cBox.setSelected(true);
                cBox.setChecked(true);
            }


            //defining action for adding/removing persons to selectedlist
            cBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(sharedSelectedPersons.contains(person))
                    {
                        sharedSelectedPersons.remove(person);
                    }
                    else
                    {
                        sharedSelectedPersons.add(person);
                    }
                }
            });
        }


        return convertView;
    }

}
