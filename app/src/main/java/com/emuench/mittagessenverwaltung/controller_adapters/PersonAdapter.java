package com.emuench.mittagessenverwaltung.controller_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> implements Filterable
{
    //Member Varables
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<Person> personList;
    private List<Person> personListCopy;
    private Context context;

    //Constructors
    public PersonAdapter(List<Person> personList, Context context)
    {
        this.personList = personList;
        this.context = context;
        personListCopy = new ArrayList<>(personList);

    }

    //Setter
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener)
    {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setPersonList(List<Person> personList)
    {
        this.personList = personList;
        this.personListCopy = new ArrayList<>(personList);
    }

    //Methods
    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.person_list_item,viewGroup,false);
        return new PersonViewHolder(v,onItemClickListener,onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder personViewHolder, int i)
    {
        Person person = personList.get(i);
        List<String> notLikedIngredients = person.getNotLikedIngredients();
        personViewHolder.getPersonName().setText(person.getName());

        if(notLikedIngredients == null || notLikedIngredients.isEmpty())
        {
            personViewHolder.getNotLikedIngredientsView().setHeight(0);
        }
        else
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getResources().getString(R.string.head_notLikedIngredients));
            stringBuilder.append('\n');

            /*
            for(String s : notLikedIngredients)
            {
                stringBuilder.append(s);
                stringBuilder.append(", ");
            }
            */
            //stringBuilder.deleteCharAt(stringBuilder.length()-2);

            stringBuilder.append(person.getNotLikedIngredients().toString().replace("[","").replace("]",""));

            personViewHolder.getNotLikedIngredientsView().setText(stringBuilder);
            personViewHolder.getNotLikedIngredientsView().setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }


    }

    @Override
    public int getItemCount()
    {
        return personList.size();
    }



    //View Holder
    public static class PersonViewHolder extends RecyclerView.ViewHolder
    {
        //Member Variables
        private LinearLayout backgroundLayout;
        private TextView personName;
        private TextView notLikedIngredientsView;

        //Constructor
        public PersonViewHolder(@NonNull View itemView, final PersonAdapter.OnItemClickListener clickListener, final PersonAdapter.OnItemLongClickListener longClickListener)
        {
            super(itemView);

            backgroundLayout = itemView.findViewById(R.id.person_list_item);
            personName  = itemView.findViewById(R.id.person_name);
            notLikedIngredientsView = itemView.findViewById(R.id.person_notLikedIngredients);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(clickListener != null)
                    {
                        int position = getAdapterPosition();

                        clickListener.onItemClicked(position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int position = getAdapterPosition();

                    if(longClickListener == null || position == RecyclerView.NO_POSITION)
                    {
                        return false;
                    }
                    else
                    {
                        return longClickListener.onItemLongClicked(position);
                    }
                }
            });
        }



        //Getter

        public TextView getPersonName()
        {
            return personName;
        }

        public TextView getNotLikedIngredientsView()
        {
            return notLikedIngredientsView;
        }

        public LinearLayout getBackgroundLayout()
        {
            return backgroundLayout;
        }
    }

    //internal Interfaces
    public interface OnItemClickListener
    {
        public void onItemClicked(int pos);
    }

    public interface OnItemLongClickListener
    {
        public boolean onItemLongClicked(int pos);
    }



    //Filterable Methods
    @Override
    public Filter getFilter()
    {
        return filter;
    }

    private Filter filter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            List<Person> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(personListCopy);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Person p : personListCopy)
                {
                    if(p.getName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(p);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            personList.clear();
            personList.addAll((List<Person>)results.values);
            System.out.println(DataStorage.getInstance().getPersonsEditable());
            notifyDataSetChanged();
        }
    };
}
