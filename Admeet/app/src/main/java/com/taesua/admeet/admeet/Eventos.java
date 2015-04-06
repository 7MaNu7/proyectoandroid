package com.taesua.admeet.admeet;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.ConferenceRequest;
import com.appspot.ad_meet.conference.ConferenceRequestInitializer;
import com.appspot.ad_meet.conference.ConferenceScopes;
import com.appspot.ad_meet.conference.model.Announcement;
import com.appspot.ad_meet.conference.model.ConferenceForm;
import com.appspot.ad_meet.conference.model.ConferenceQueryForm;
import com.appspot.ad_meet.conference.model.Filter;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Eventos extends ActionBarActivity {

    private Conference conferencia;
    private ListView eventos;
    private ConferenceScopes conferenciareq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        try {

            conferencia = new Conference(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
            List<com.appspot.ad_meet.conference.model.Conference> listaeventos;
            ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
            String eve[];

            eventos = (ListView)findViewById(R.id.listviewev);

            Conference.QueryConferences c;
            System.out.println("EEEEEEEEEEEEEEEEEEEEEE2");
            Filter filter = new Filter();
            ArrayList filtros = new ArrayList();
            filtros.add(filter);
            conferenceQueryForm.setFilters(filtros);

            c =  conferencia.queryConferences(conferenceQueryForm);
            System.out.println("EEE"+c.getAlt());
            System.out.println("EEEEEEEEEEEEEEEEEEEEEE3");
            listaeventos = c.execute().getItems();
            System.out.println("EEEEEEEEEEEEEEEEEEEEEE4");
            int tam = listaeventos.size();
            eve = new String[tam];
            System.out.println("TAMAÑO:::::" + tam);

            for(int i=0;i<tam;i++)
                eve[i]=listaeventos.get(i).getName();
            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eve);
            eventos.setAdapter(adaptador);

            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA333");
            eventos.setAdapter(adaptador);
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA444");

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERROoooooR: "+e.getMessage());
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
