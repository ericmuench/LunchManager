package com.emuench.mittagessenverwaltung.controller_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.emuench.mittagessenverwaltung.R;
import com.emuench.mittagessenverwaltung.model.DataStorage;
import com.emuench.mittagessenverwaltung.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class DishListAdapter extends RecyclerView.Adapter<DishListAdapter.DishViewHolder> implements Filterable
{

    //Member Variables
    private OnItemClickListener onItemClickListener;
    private List<Dish> dishList;
    private List<Dish> dishListCopy;
    private Context context;


    //Constructors
    public DishListAdapter(List<Dish> dishList,Context c)
    {
        this.dishList = dishList;
        this.dishListCopy = new ArrayList<>(dishList);
        this.context = c;
    }


    //Setter
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDishList(List<Dish> dishList)
    {
        this.dishList = dishList;
        this.dishListCopy = new ArrayList<>(dishList);
    }

    //Methods
    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dishlist_item,viewGroup,false);

        return new DishViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder dishViewHolder, int i)
    {
        Dish dish = dishList.get(i);

        dishViewHolder.getDishTitle().setText(dish.getTitle());

        if(dish.getBook().isEmpty())
        {
            dishViewHolder.getBookInfo().setHeight(0);
        }
        else
        {
            dishViewHolder.getBookInfo().setText(String.format("%s, S. %d",dish.getBook(),dish.getBookSite()));
        }

        if(dish.getRecipeLink().isEmpty())
        {
            dishViewHolder.getLinkInfo().setHeight(0);
            //dishViewHolder.getLinkInfo()
        }
        else
        {
            dishViewHolder.getLinkInfo().setText(context.getResources().getString(R.string.information_link_avalible));
            dishViewHolder.getLinkInfo().setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }

    }

    @Override
    public int getItemCount()
    {
        return dishList.size();
    }

    //View Holder
    public static class DishViewHolder extends RecyclerView.ViewHolder
    {
        //MemberVariables
        private TextView dishTitle;
        private TextView bookInfo;
        private TextView linkInfo;

        //Constructor
        public DishViewHolder(@NonNull View itemView, final OnItemClickListener clickListener)
        {
            super(itemView);

            dishTitle = itemView.findViewById(R.id.dish_title);
            bookInfo = itemView.findViewById(R.id.dish_bookinfo);
            linkInfo = itemView.findViewById(R.id.dish_linkinfo);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(clickListener!= null)
                    {
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION)
                        {
                            clickListener.onItemClicked(position);
                        }
                    }
                }
            });
        }

        //Getter & Setter
        public TextView getDishTitle()
        {
            return dishTitle;
        }

        public void setDishTitle(TextView dishTitle)
        {
            this.dishTitle = dishTitle;
        }

        public TextView getBookInfo()
        {
            return bookInfo;
        }

        public void setBookInfo(TextView bookInfo)
        {
            this.bookInfo = bookInfo;
        }

        public TextView getLinkInfo()
        {
            return linkInfo;
        }

        public void setLinkInfo(TextView linkInfo)
        {
            this.linkInfo = linkInfo;
        }
    }

    //internal Interface
    public interface OnItemClickListener
    {
        public void onItemClicked(int position);
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
            List<Dish> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(dishListCopy);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Dish dish : dishListCopy)
                {
                    if((dish.getTitle().toLowerCase().trim().contains(filterPattern)
                            || dish.getBook().toLowerCase().trim().contains(filterPattern)
                            || filterPattern.contains(Integer.toString(dish.getBookSite()))
                            || dish.getRecipeLink().toLowerCase().trim().contains(filterPattern)))
                    {
                        filteredList.add(dish);
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
            dishList.clear();
            System.out.println(results);
            dishList.addAll((List<Dish>)results.values);
            System.out.println(DataStorage.getInstance().getDishesEditable());
            notifyDataSetChanged();
        }
    };
}
