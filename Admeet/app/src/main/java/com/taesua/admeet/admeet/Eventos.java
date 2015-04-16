package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import conference.Conference;
import conference.model.ConferenceCollection;
import conference.model.ConferenceQueryForm;
import conference.model.Filter;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;


public class Eventos extends ActionBarActivity {

    private String accountName;
    private static String PREF_ACCOUNT_NAME = "accountName";
    private Context context;
    private Conference conferenciaendpoint;
    private ListView eventos;
    private List<conference.model.Conference> listaeventos = new ArrayList();

    SharedPreferences settings;
    GoogleAccountCredential credential;

    ConferenceQueryForm query = new ConferenceQueryForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        this.context = this;
        final Button botontodos = (Button) findViewById(R.id.buttontodos);
        final Button botonmios = (Button) findViewById(R.id.buttonmios);
        final Button botonasisto = (Button) findViewById(R.id.buttonasisto);
        eventos = (ListView)findViewById(R.id.listviewev);


        //if(this.getIntent().getExtras().size()>0)
        if(this.getIntent().getExtras()!=null)
        {
            ArrayList filtros = new ArrayList();
            int tam = this.getIntent().getExtras().size()/3;
            for(int i=0;i<tam;i++) {
                Filter filter = new Filter();
                filter.setField(this.getIntent().getExtras().getString("field" + i));

                //CUIDAO OJO !!!!!!!!!!!!!!!!!!!!!!!!!!
                filter.setOperator(this.getIntent().getExtras().getString("operator" + i));
                filter.setValue(this.getIntent().getExtras().getString("value" + i));
                filtros.add(filter);
            }
            query.setFilters(filtros);
        }


        botontodos.setTextColor( Color.parseColor("#FFFFFF")); //seleccionado pro defecto TODOS al principio

        //GetEventos getMessage = new GetEventos();
        GetEventos getMessage = new GetEventos();
        getMessage.execute();

        eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position,
                                    long id) {
                conference.model.Conference evento;
                evento = listaeventos.get(position);
                Long idevento = evento.getId();
                Intent intent = new Intent(Eventos.this, Evento.class);
                intent.putExtra("name", evento.getName());
                intent.putExtra("city", evento.getCity());
                intent.putExtra("description", evento.getDescription());
                //intent.putExtra("fecha", evento.getStartDate());
                String t = evento.getStartDate().toString();
                String[] trozos = t.split("T");
                intent.putExtra("fecha",trozos[0]);
                intent.putExtra("asientos", evento.getSeatsAvailable()+"/"+evento.getMaxAttendees());
                String categorias="";
                for(int i=0;i<evento.getTopics().size();i++)
                    categorias+=" " + evento.getTopics().get(i);
                intent.putExtra("categorias", categorias);

                intent.putExtra("websafeKey",evento.getWebsafeKey());
                startActivity(intent);
            }
        });

        //Accion del boton
        botonmios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonmios.setTextColor( Color.parseColor("#FFFFFF"));
                botontodos.setTextColor( Color.parseColor("#000000"));
                botonasisto.setTextColor( Color.parseColor("#000000"));

                GetEventosMios getEventosMios = new GetEventosMios();
                getEventosMios.execute();
            }
        });

        //Accion del boton
        botonasisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonasisto.setTextColor( Color.parseColor("#FFFFFF"));
                botonmios.setTextColor( Color.parseColor("#000000"));
                botontodos.setTextColor( Color.parseColor("#000000"));

                GetEventosAtendidos getEventos = new GetEventosAtendidos();
                getEventos.execute();
            }
        });

        //Accion del boton
        botontodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botontodos.setTextColor( Color.parseColor("#FFFFFF"));
                botonmios.setTextColor( Color.parseColor("#000000"));
                botonasisto.setTextColor( Color.parseColor("#000000"));

                GetEventos getEventos = new GetEventos();
                getEventos.execute();
            }
        });



        //BOTON EVENTOS, MARCARLO
        Button anuncios = (Button)findViewById(R.id.buttonAnuncios);
        anuncios.setTypeface(null, Typeface.BOLD);


        //PARA IR A FILTERS
        findViewById(R.id.buttonFilters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Eventos.this,Filtros.class);
                startActivity(ji);
            }
        });

        //PARA IR A PERFIL
        findViewById(R.id.buttonPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Eventos.this,Perfil.class);
                startActivity(ji);
            }
        });

        //PARA IR A PUBLICAR
        findViewById(R.id.buttonPublicar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Eventos.this,CrearEvento.class);
                startActivity(ji);
            }
        });





        /*
        // Inside your Activity class onCreate method
        //settings = getSharedPreferences(Context.MODE_PRIVATE);
        settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingAudience(this,
                Ids.AUDIENCE);
        setSelectedAccountName(PREF_ACCOUNT_NAME);

        Conference.Builder endpointBuilder = new Conference.Builder(AndroidHttp.newCompatibleTransport(),
                new GsonFactory(), credential);
        //endpoint = CloudEndpointUtils.updateBuilder(endpointBuilder).build();
        conferenciaendpoint = endpointBuilder.build();

        if (credential.getSelectedAccountName() != null) {
            System.out.println("LOGEADO");
        } else {
            // Not signed in, show login window or request an account.
            System.out.println("NO LOGEADO");
        }

        */

    }

    // setSelectedAccountName definition
    private void setSelectedAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        this.accountName = accountName;
    }

    /**
     * Ver todos los eventos
     */
    private class GetEventos extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventos() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Display success message to user
            if(query.getFilters()!=null) {
                Toast.makeText(getBaseContext(), "Se han aplicado correctamente los filtros.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
               // ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
                //Eventos.get.getIntent();
                //if(this.getIntent())



                Conference.QueryConferences create = ConferenceUtils.getEventos(query);
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
            int tam=0;

            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

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

    /**
     * Para obtener los eventos del usuario
     */
    private class GetEventosMios extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventosMios() { }

        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                System.out.println("HA ENTRADO EVENTOS MIOS-----------------");

                Conference.GetConferencesCreated create = ConferenceUtils.getEventosMios();
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
            int tam=0;
            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

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


    /**
     * Para obtener los eventos que el usuario a atendido
     */
    private class GetEventosAtendidos extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventosAtendidos() { }

        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                System.out.println("HA ENTRADO-----------------");
                ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
                Conference.GetConferencesToAttend create = ConferenceUtils.getEventosAsisto();
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
            int tam=0;

            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

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