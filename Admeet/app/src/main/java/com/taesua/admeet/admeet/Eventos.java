package com.taesua.admeet.admeet;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.ConferenceRequest;
import com.appspot.ad_meet.conference.ConferenceRequestInitializer;
import com.appspot.ad_meet.conference.ConferenceScopes;
import com.appspot.ad_meet.conference.model.Announcement;
import com.appspot.ad_meet.conference.model.ConferenceCollection;
import com.appspot.ad_meet.conference.model.ConferenceForm;
import com.appspot.ad_meet.conference.model.ConferenceQueryForm;
import com.appspot.ad_meet.conference.model.Filter;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Eventos extends ActionBarActivity {

    private Context context;
    private Conference conferenciaendpoint;
    private ListView eventos;
    private List<com.appspot.ad_meet.conference.model.Conference> listaeventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        this.context = this;
        eventos = (ListView)findViewById(R.id.listviewev);

        Conference.Builder builder = new Conference.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
        //builder.setApplicationName("ad-meet");
        conferenciaendpoint = builder.build();

        GetMessage getMessage = new GetMessage();
        getMessage.execute();

        System.out.println("Hecho execute");

        eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position,
                                    long id) {
                com.appspot.ad_meet.conference.model.Conference evento;
                evento = listaeventos.get(position);
                Long idevento = evento.getId();
                Intent intent = new Intent(Eventos.this, Evento.class);
                intent.putExtra("name", evento.getName());
                intent.putExtra("city", evento.getCity());
                intent.putExtra("description", evento.getDescription());
                intent.putExtra("fecha", evento.getStartDate());
                intent.putExtra("asientos", evento.getSeatsAvailable()+"/"+evento.getMaxAttendees());
                String categorias="";
                for(int i=0;i<evento.getTopics().size();i++)
                    categorias+=" " + evento.getTopics().get(i);
                intent.putExtra("categorias", categorias);
                startActivity(intent);
            }
        });
    }

    private class GetMessage extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetMessage() { }

        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                System.out.println("HA ENTRADO-----------------");
                ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
                Conference.QueryConferences create = conferenciaendpoint.queryConferences(conferenceQueryForm);
                messages = create.execute();
                /*Filter filter = new Filter();
                filter.setField("CITY");
                filter.setOperator("EQ");
                filter.setValue("London");
                ArrayList filtros = new ArrayList();
                filtros.add(filter);
                // conferenceQueryForm.setFilters(filtros);*/
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return messages;
        }

        @Override
        protected void onPostExecute(ConferenceCollection result)
        {
            listaeventos = result.getItems();
            int tam = listaeventos.size();
            String nombres[] = new String[tam];
            String otro[] = new String[tam];

            for(int i=0;i<tam;i++) {
                nombres[i] = listaeventos.get(i).getName();
                otro[i] = listaeventos.get(i).getDescription();
            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, nombres);
            eventos.setAdapter(adaptador);

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
