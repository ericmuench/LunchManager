package com.emuench.mittagessenverwaltung.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

public class ReadOnlyList<T> implements List<T>
{
    //Member Variables
    private List<T> list = new ArrayList<>();

    //Constructors
    public ReadOnlyList(ArrayList<T> list)
    {
        this.list = list;
    }

    //Setter
    public void setList(List<T> list)
    {
        this.list = list;
    }

    //Methods
    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o)
    {
        return list.contains(o);
    }

    @Override
    public Iterator<T> iterator()
    {
        return list.iterator();
    }


    @Override
    public Object[] toArray()
    {
        return list.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a)
    {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t)
    {
        return false;
    }

    @Override
    public boolean remove(Object o)
    {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c)
    {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {

    }

    @Override
    public void sort(Comparator<? super T> c)
    {
        Collections.sort(list,c);
    }

    @Override
    public void clear()
    {
        System.err.println("Löschen nicht möglich");
    }

    @Override
    public T get(int index)
    {
        return list.get(index);
    }

    @Override
    public T set(int index, T element)
    {
        return null;
    }

    @Override
    public void add(int index, T element)
    {
        System.err.println("Einfügen nicht möglich");
    }

    @Override
    public T remove(int index)
    {
        return null;
    }

    @Override
    public int indexOf(Object o)
    {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return list.lastIndexOf(o);
    }


    @Override
    public ListIterator<T> listIterator()
    {
        return list.listIterator();
    }


    @Override
    public ListIterator<T> listIterator(int index)
    {
        return list.listIterator(index);
    }


    @Override
    public List<T> subList(int fromIndex, int toIndex)
    {
        return list.subList(fromIndex,toIndex);
    }


    @Override
    public Spliterator<T> spliterator()
    {
        return null;
    }
}
