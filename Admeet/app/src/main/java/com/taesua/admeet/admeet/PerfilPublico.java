package com.taesua.admeet.admeet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.ConferenceCollection;
import com.appspot.ad_meet.conference.model.ConferenceQueryForm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Hector on 14/04/2015.
 */
public class PerfilPublico extends ActionBarActivity {


    private ListView listView;
    private ListView eventos;
    private DrawerLayout drawerLayout = null;
    private List<com.appspot.ad_meet.conference.model.Conference> listaeventos = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        listView = (ListView) findViewById(R.id.list_view);
        eventos = (ListView)findViewById(R.id.listViewEv);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = new MenuAdapter(PerfilPublico.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;

                if(opciones[arg2].equals("Eventos"))
                    intent = new Intent(PerfilPublico.this,MainActivity.class);
                else if(opciones[arg2].equals("Filtros"))
                    intent = new Intent(PerfilPublico.this,Filtros.class);
                else if(opciones[arg2].equals("Publicar"))
                    intent = new Intent(PerfilPublico.this,CrearEvento.class);
                else
                    intent = new Intent(PerfilPublico.this,EditarPerfil.class);


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                drawerLayout.closeDrawers();
            }
        });

        TextView nombre = (TextView) findViewById(R.id.textViewNombre);
        TextView nombreNick = (TextView) findViewById(R.id.textViewNombreNick);
        TextView ciudad = (TextView) findViewById(R.id.textViewCiudad);
        TextView tlf = (TextView) findViewById(R.id.textViewTlf);

        nombre.setText(getIntent().getExtras().getString("nombre"));
        nombreNick.setText(getIntent().getExtras().getString("nombre"));
        ciudad.setText(getIntent().getExtras().getString("ciudad"));
        tlf.setText(getIntent().getExtras().getString("telefono"));




        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);
        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GetEventos getEventos = new GetEventos();
        getEventos.execute();


        //Clicar en un evento
        eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position,
                                    long id) {
                com.appspot.ad_meet.conference.model.Conference evento;
                evento = listaeventos.get(position);
                Long idevento = evento.getId();
                Intent intent = new Intent(PerfilPublico.this, Evento.class);
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
                intent.putExtra("eventoid",evento.getId());
                intent.putExtra("creador", evento.getOrganizerDisplayName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
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
        }
        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                ConferenceQueryForm query = new ConferenceQueryForm();
                query.clear();
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

    public void rellenaListView(List<com.appspot.ad_meet.conference.model.Conference> listaeventos, int tam)  {



        for(com.appspot.ad_meet.conference.model.Conference c: listaeventos)
        {
            //GUARRADA MONUMENTAL
            //
            //
            if(c.getCity().contains("elche"))
            {
                listaeventos.remove(c);
                tam--;
                break;
            }

            //
            //
        }

        //FILTRO, SOLO SALEN LOS QUE ASISTA EL DEL PERFIL
        //
        List<com.appspot.ad_meet.conference.model.Conference> filtrado = new ArrayList();
        String userid = PerfilPublico.this.getIntent().getExtras().getString("userid");
        for(com.appspot.ad_meet.conference.model.Conference c: listaeventos)
        {
           if(c.getParticipants()==null || c.getParticipants().size()==0)
               continue;
            if(c.getParticipants().contains(userid))
                filtrado.add(c);
        }

        listaeventos.clear();
        listaeventos.addAll(filtrado);

        System.out.println("EL USERID PUBLICO DE ESTE TIO ES " + userid);
        System.out.println("EL TAMANIO DE FILTRADO ES " + listaeventos.size());

        tam = listaeventos.size();

        //SE SUPONE QUE CON ESTO YA ESTA ORDENADO
        Collections.sort(listaeventos, new Comparator<com.appspot.ad_meet.conference.model.Conference>() {
            public int compare(com.appspot.ad_meet.conference.model.Conference c1, com.appspot.ad_meet.conference.model.Conference c2) {
                return new Long(c2.getStartDate().getValue()).compareTo(new Long(c1.getStartDate().getValue()));
            }
        });

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
        TextView numCelda = (TextView) findViewById(R.id.textviewNombre);
        EventosAdapter adapter;
        // Inicializamos el adapter.
        adapter = new EventosAdapter(PerfilPublico.this, nombres, categorias, asis, maxasis, fecha);
        // Asignamos el Adapter al ListView, en este punto hacemos que el
        // ListView muestre los datos que queremos.
        eventos.setAdapter(adapter);
    }

    public void onBackPressed() {

        Intent intent = new Intent(PerfilPublico.this, Evento.class);
        intent.putExtras(this.getIntent().getExtras());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eventos, menu);
        return true;
    }

    // Mostramos el botón en la barra de la aplicación
    //getActionBar().setDisplayHomeAsUpEnabled(true);
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(listView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(listView);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
