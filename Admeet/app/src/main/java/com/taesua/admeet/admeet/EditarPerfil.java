package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.ConferenceCollection;
import com.appspot.ad_meet.conference.model.ConferenceQueryForm;
import com.appspot.ad_meet.conference.model.Profile;
import com.appspot.ad_meet.conference.model.ProfileForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector on 14/04/2015.
 */
public class EditarPerfil extends ActionBarActivity {

    private EditText nombre;
    private TextView nombreNick;
    private EditText ciudad;
    private EditText tlf;

    private ListView eventos;
    private List<com.appspot.ad_meet.conference.model.Conference> listaeventos = new ArrayList();

    private DrawerLayout drawerLayout = null;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = adapter = new MenuAdapter(EditarPerfil.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = null;

                if(!opciones[arg2].equals("Perfil")) {
                    if (opciones[arg2].equals("Filtros"))
                        intent = new Intent(EditarPerfil.this, Filtros.class);
                    else if (opciones[arg2].equals("Publicar"))
                        intent = new Intent(EditarPerfil.this, CrearEvento.class);
                    else
                        intent = new Intent(EditarPerfil.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawers();
            }
        });

        // Mostramos el botón en la barra de la aplicación
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);




        eventos = (ListView)findViewById(R.id.listViewEv);

        GetEventosAtendidos getEventos1 = new GetEventosAtendidos();
        getEventos1.execute();

        //BOTON EDITAR PERFIL

        Button s = (Button) findViewById(R.id.buttonSaveProf);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().equals(""))
                {
                    nombre.setError("¡No se puede dejar vacío el campo de nombre!");
                }
                else {
                    GuardarPerfil guardarPerfil = (GuardarPerfil) new GuardarPerfil().execute();
                }
            }
        });

        nombreNick = (TextView) findViewById(R.id.textViewNombreNick);
        nombre = (EditText) findViewById(R.id.editTextName);
        ciudad = (EditText) findViewById(R.id.editTextCity);
        tlf = (EditText) findViewById(R.id.editTextPhone);

        tlf.setInputType(InputType.TYPE_CLASS_NUMBER);


        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        GetPerfil getperfil = (GetPerfil) new GetPerfil().execute();

        //Clicar en un evento
        eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position,
                                    long id) {
                com.appspot.ad_meet.conference.model.Conference evento;
                evento = listaeventos.get(position);
                Long idevento = evento.getId();
                Intent intent = new Intent(EditarPerfil.this, Evento.class);
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


    public void onBackPressed() {
        Intent intent = new Intent(EditarPerfil.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Get datos de perfil
     */
    private class GetPerfil extends AsyncTask<Void, Void,Profile>
    {
        private ProgressDialog pd;

        public GetPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(EditarPerfil.this);
            pd.setMessage("Cargando información de perfil...");
            pd.show();
        }
        @Override
        protected Profile doInBackground(Void ... unused)
        {
            Profile perfil = null;
            try
            {
                Conference.GetProfile prof = ConferenceUtils.getProfile();
                perfil = prof.execute();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return perfil;
        }

        @Override
        protected void onPostExecute(Profile result)
        {
            //Clear the progress dialog and the fields
            pd.dismiss();

            if(result==null)
            {
                nombre.setText("No especificado");
                nombreNick.setText("No especificado");
                ciudad.setText("No especificado");
                tlf.setText("No especificado");
            }
            else {
                nombre.setText(result.getDisplayName());
                nombreNick.setText(result.getDisplayName());
                ciudad.setText(result.getCiudad());
                tlf.setText(result.getTelefono());
                //id.setText(result.getMainEmail());
            }
            //Display success message to user
            Toast.makeText(getBaseContext(), "Información de perfil cargada correctamente",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Guardar perfil
     */
    private class GuardarPerfil extends AsyncTask<Void, Void,Profile>
    {
        private ProgressDialog pd;
        public GuardarPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(EditarPerfil.this);
            pd.setMessage("Guardando información de perfil...");
            pd.show();
        }
        @Override
        protected Profile doInBackground(Void ... unused)
        {
            Profile perfil = null;
            try
            {
                ProfileForm form = new ProfileForm();
                form.setDisplayName(nombre.getText().toString());
                form.setCiudad(ciudad.getText().toString());
                form.setTelefono(tlf.getText().toString());
                Conference.SaveProfile prof = ConferenceUtils.saveProfile(form);
                perfil = prof.execute();

                TextView nombre1 = (EditText) findViewById(R.id.editTextName);
                TextView nombreNick = (TextView) findViewById(R.id.textViewNombreNick);
                nombreNick.setText(nombre1.getText().toString());
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return perfil;
        }

        @Override
        protected void onPostExecute(Profile result)
        {
            pd.dismiss();
            Toast.makeText(getBaseContext(), "Información de perfil guardada correctamente",
                    Toast.LENGTH_SHORT).show();
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

    public void rellenaListView(List<com.appspot.ad_meet.conference.model.Conference> listaeventos, int tam)  {
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
        adapter = new EventosAdapter(EditarPerfil.this, nombres, categorias, asis, maxasis, fecha);
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

