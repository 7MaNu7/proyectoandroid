package com.taesua.admeet.admeet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;
import java.util.List;

import conference.Conference;
import conference.model.ConferenceCollection;
import conference.model.ConferenceQueryForm;
import conference.model.Filter;


public class Eventos extends ActionBarActivity {

    private String accountName;
    private static String PREF_ACCOUNT_NAME = "accountName";
    private Context context;
    private Conference conferenciaendpoint;
    private ListView eventos;
    private List<conference.model.Conference> listaeventos = new ArrayList();
    private Drawable estiloselec = null;
    private Drawable estilonoselec = null;

    SharedPreferences settings;
    GoogleAccountCredential credential;

    ConferenceQueryForm query = new ConferenceQueryForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        final Button buttonPerfil = (Button) findViewById(R.id.buttonPerfil);
        final Button buttonAnuncios = (Button) findViewById(R.id.buttonPerfil);
        final Button buttonPublicar = (Button) findViewById(R.id.buttonPublicar);
        final Button botontodos = (Button) findViewById(R.id.buttontodos);
        final Button botonmios = (Button) findViewById(R.id.buttonmios);
        final Button botonasisto = (Button) findViewById(R.id.buttonasisto);
        eventos = (ListView)findViewById(R.id.listviewev);

        //Definimos los estilos guardandolos primero
        estiloselec = botontodos.getBackground();
        estilonoselec = botonasisto.getBackground();

        /**
         * Mostrar todos los eventos al iniciar esta activity
         */
        botontodos.setTextColor( Color.parseColor("#000000"));
        botonmios.setTextColor( Color.parseColor("#000000"));
        botonasisto.setTextColor( Color.parseColor("#000000"));
        botontodos.setBackground(estiloselec);
        botonmios.setBackground(estilonoselec);
        botonasisto.setBackground(estilonoselec);

        GetEventos getEventos = new GetEventos();
        getEventos.execute();

        botontodos.setTextColor( Color.parseColor("#000000"));
        botonmios.setTextColor( Color.parseColor("#000000"));
        botonasisto.setTextColor( Color.parseColor("#000000"));
        botontodos.setBackground(estiloselec);
        botonmios.setBackground(estilonoselec);
        botonasisto.setBackground(estilonoselec);

        GetEventos getEventos1 = new GetEventos();
        getEventos1.execute();

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
                intent.putExtra("creador", evento.getOrganizerDisplayName());
                startActivity(intent);
            }
        });

        //Accion del boton mios
        botonmios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonmios.setTextColor( Color.parseColor("#000000"));
                botontodos.setTextColor( Color.parseColor("#000000"));
                botonasisto.setTextColor( Color.parseColor("#000000"));
                botontodos.setBackground(estilonoselec);
                botonmios.setBackground(estiloselec);
                botonasisto.setBackground(estilonoselec);

                GetEventosMios getEventosMios = new GetEventosMios();
                getEventosMios.execute();
            }
        });

        //Accion del boton asisto
        botonasisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonasisto.setTextColor( Color.parseColor("#000000"));
                botonmios.setTextColor( Color.parseColor("#000000"));
                botontodos.setTextColor( Color.parseColor("#000000"));
                botontodos.setBackground(estilonoselec);
                botonmios.setBackground(estilonoselec);
                botonasisto.setBackground(estiloselec);

                GetEventosAtendidos getEventos = new GetEventosAtendidos();
                getEventos.execute();
            }
        });

        //Accion del boton todos
        botontodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botontodos.setTextColor( Color.parseColor("#000000"));
                botonmios.setTextColor( Color.parseColor("#000000"));
                botonasisto.setTextColor( Color.parseColor("#000000"));
                botontodos.setBackground(estiloselec);
                botonmios.setBackground(estilonoselec);
                botonasisto.setBackground(estilonoselec);

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
                Intent ji = new Intent(Eventos.this,EditarPerfil.class);
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
                Conference.QueryConferences create = ConferenceUtils.getEventos(query);
                messages = create.execute();
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

            rellenaListView(listaeventos, tam);
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
                Conference.GetConferencesCreated create = ConferenceUtils.getEventosMios();
                messages = create.execute();
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

            rellenaListView(listaeventos, tam);

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
                ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
                Conference.GetConferencesToAttend create = ConferenceUtils.getEventosAsisto();
                messages = create.execute();
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

            rellenaListView(listaeventos, tam);
        }
    }

    public void rellenaListView(List<conference.model.Conference> listaeventos, int tam)  {
        String nombres[] = new String[tam];
        String categorias[] = new String[tam];
        String fecha[] = new String[tam];
        Integer asis[] = new Integer[tam];
        Integer maxasis[] = new Integer[tam];

        for(int i=0;i<tam;i++) {
            nombres[i] = listaeventos.get(i).getName();
            categorias[i] = listaeventos.get(i).getTopics().get(0);
            asis[i] = (listaeventos.get(i).getMaxAttendees()-listaeventos.get(i).getSeatsAvailable());
            maxasis[i] = listaeventos.get(i).getMaxAttendees();
            fecha[i] = listaeventos.get(i).getStartDate().toString().split("T")[0];
        }

        ImageView imagen = (ImageView) findViewById(R.id.imgAnimal);
        TextView nombre = (TextView) findViewById(R.id.tvContent);
        TextView numCelda = (TextView) findViewById(R.id.tvField);
        EventosAdapter adapter;
        // Inicializamos el adapter.
        adapter = new EventosAdapter(Eventos.this, nombres, categorias, asis, maxasis, fecha);
        // Asignamos el Adapter al ListView, en este punto hacemos que el
        // ListView muestre los datos que queremos.
        eventos.setAdapter(adapter);
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