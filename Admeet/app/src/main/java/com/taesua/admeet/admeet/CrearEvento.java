package com.taesua.admeet.admeet;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.ConferenceForm;
import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class CrearEvento extends ActionBarActivity {

    private EditText titulo;
    private Spinner categoria;
    private EditText lugar;
    private CalendarView calendar;
    private EditText n_personas;
    private EditText descripcion;
    private DrawerLayout drawerLayout = null;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_evento);

        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = adapter = new MenuAdapter(CrearEvento.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = null;


                if(!opciones[arg2].equals("Publicar")) {
                    if (opciones[arg2].equals("Filtros"))
                        intent = new Intent(CrearEvento.this, Filtros.class);
                    else if (opciones[arg2].equals("Perfil"))
                        intent = new Intent(CrearEvento.this, EditarPerfil.class);
                    else
                        intent = new Intent(CrearEvento.this, MainActivity.class);
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




        titulo = (EditText)findViewById(R.id.editTextTitulo);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria);
        lugar = (EditText)findViewById(R.id.editTextLugar);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        n_personas = (EditText)findViewById(R.id.editTextNPersonas);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion);

        final List<String> listcategorias = new ArrayList<String>();
        listcategorias.add("Deportes");
        listcategorias.add("Cultura");
        listcategorias.add("Fiesta");
        listcategorias.add("Otros");

        n_personas.setInputType(InputType.TYPE_CLASS_NUMBER);

        ArrayAdapter<String> dataAdapterfield = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listcategorias);
        dataAdapterfield.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(dataAdapterfield);

        //PUBLICAR ANUNCIO
        findViewById(R.id.buttonAnadirAnuncio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titulo.getText().toString().equals("")) //si esta vacio, validar
                {
                    titulo.setError("¡No se puede dejar vacío el campo de título!");
                    return;
                }

                if(lugar.getText().toString().equals(""))
                {
                    lugar.setError("¡No se puede dejar vacío el campo del lugar!");
                    return;
                }

                if(n_personas.getText().toString().equals(""))
                {
                    n_personas.setError("¡No se puede dejar el número de asistentes vacío!");
                    return;
                }


                PublicarEvento publicarEvento = (PublicarEvento) new PublicarEvento().execute();
                Intent intent = new Intent(CrearEvento.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void onBackPressed() {
        Intent intent = new Intent(CrearEvento.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private class PublicarEvento extends AsyncTask<Void, Void,com.appspot.ad_meet.conference.model.Conference>
    {

        public PublicarEvento() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected com.appspot.ad_meet.conference.model.Conference doInBackground(Void ... unused)
        {
            com.appspot.ad_meet.conference.model.Conference evento = null;
            try
            {
                ConferenceForm form = new ConferenceForm();
                form.setName(titulo.getText().toString());

                List<String> categorias = new ArrayList<String>();
                categorias.add(categoria.getSelectedItem().toString());
                form.setTopics(categorias);

                form.setCity(lugar.getText().toString());

                //OJO
                Date d1 = new Date(calendar.getDate());
                DateTime d2 = new DateTime(d1);
                
                form.setStartDate(d2);

                form.setMaxAttendees(Integer.parseInt(n_personas.getText().toString()));
                form.setDescription(descripcion.getText().toString());
                Conference.CreateConference crear = ConferenceUtils.crearEvento(form);
                evento = crear.execute();

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return evento;
        }

        @Override
        protected void onPostExecute(com.appspot.ad_meet.conference.model.Conference result)
        {
        }
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
