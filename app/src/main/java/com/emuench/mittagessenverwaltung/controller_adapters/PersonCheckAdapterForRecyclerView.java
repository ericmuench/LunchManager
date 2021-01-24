package com.emuench.mittagessenverwaltung.controller_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.List;

public class PersonCheckAdapterForRecyclerView extends RecyclerView.Adapter<PersonCheckAdapterForRecyclerView.PersonCheckViewHolder>
{
    //Variables
    private OnItemClickListener clickListener;
    private List<Person> persons;
    private List<Person> sharedSelectionData;
    private Context context;

    //Constructors
    public PersonCheckAdapterForRecyclerView(List<Person> persons, List<Person> sharedSelectionData, @NonNull Context context)
    {
        this.persons = persons;
        this.sharedSelectionData = sharedSelectionData;
        this.context = context;
    }

    //Getter & Setter

    public List<Person> getSharedSelectionData()
    {
        return sharedSelectionData;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    //Methods
    @NonNull
    @Override
    public PersonCheckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.checkable_person_list_item_with_nli,viewGroup,false);
        return new PersonCheckViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonCheckViewHolder personCheckViewHolder, int i)
    {
        Person p = persons.get(i);
        personCheckViewHolder.getCheckBox().setText(p.getName());

        if(sharedSelectionData.contains(p))
        {
            personCheckViewHolder.getCheckBox().setChecked(true);
        }

        if(p.getNotLikedIngredients() == null || p.getNotLikedIngredients().isEmpty())
        {
            personCheckViewHolder.getNotLikedIngredientsInfo().setHeight(0);
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getResources().getString(R.string.head_notLikedIngredients));
            stringBuilder.append('\n');
            String nLI = p.getNotLikedIngredients().toString().replace("[","").replace("]","");
            stringBuilder.append(nLI);
            personCheckViewHolder.getNotLikedIngredientsInfo().setText(stringBuilder);
            personCheckViewHolder.getNotLikedIngredientsInfo().setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    @Override
    public int getItemCount()
    {
        return persons.size();
    }

    //ViewHolder
    public static class PersonCheckViewHolder extends RecyclerView.ViewHolder
    {
        private CheckBox checkBox;
        private TextView notLikedIngredientsInfo;

        public PersonCheckViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener)
        {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkPersonWithNLI);
            notLikedIngredientsInfo = itemView.findViewById(R.id.notLikedIngredients_info);

            checkBox.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION)
                    {
                        if(onItemClickListener != null )
                        {
                            onItemClickListener.onItemClick(pos);
                        }

                    }



                }
            });

        }

        //Getter
        public CheckBox getCheckBox()
        {
            return checkBox;
        }

        public TextView getNotLikedIngredientsInfo()
        {
            return notLikedIngredientsInfo;
        }
    }

    //OnClickListener Interfaces
    public interface OnItemClickListener
    {
        public void onItemClick(int position);
    }
}
