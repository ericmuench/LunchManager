package com.emuench.mittagessenverwaltung.activities;

import android.support.v7.app.AppCompatActivity;

import com.emuench.mittagessenverwaltung.model.DataManager;

public abstract class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onPause()
    {
        new DataManager(this).saveData();
        super.onPause();
    }
}
