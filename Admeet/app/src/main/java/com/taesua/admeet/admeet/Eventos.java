package com.taesua.admeet.admeet;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appspot.ad_meet.conference.Conference;

import java.io.IOException;
import java.util.ArrayList;


public class Eventos extends ActionBarActivity {

    private Conference conferencia;
    private ListView eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        try {
           ArrayList a =  new ArrayList();
           eventos = (ListView)findViewById(R.id.listview);
           String eve[] = new String[conferencia.getConferencesCreated().size()];
           a.add( conferencia.getConferencesCreated().getAlt());
           for(int i=0;i<conferencia.getConferencesCreated().size();i++)
               eve[i]=conferencia.getConferencesCreated().getAlt();
           ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eve);
            eventos.setAdapter(adaptador);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
