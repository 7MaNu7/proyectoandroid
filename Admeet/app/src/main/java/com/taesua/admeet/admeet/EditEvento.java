package com.taesua.admeet.admeet;

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

import com.appspot.ad_meet.conference.model.Conference;
import com.appspot.ad_meet.conference.model.ConferenceForm;
import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hector on 17/05/2015.
 */
public class EditEvento extends ActionBarActivity {

    private EditText titulo;
    private Spinner categoria;
    private EditText lugar;
    private CalendarView calendar;
    private EditText n_personas;
    private EditText descripcion;
    private Intent IntentExtras;
    private DrawerLayout drawerLayout = null;
    private ListView listView;

    private int DISPONIBLES;
    private int MAX_PERSONAS;
    private int OCUPADOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_evento);

        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = adapter = new MenuAdapter(EditEvento.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = null;

                if (opciones[arg2].equals("Filtros"))
                    intent = new Intent(EditEvento.this, Filtros.class);
                else if (opciones[arg2].equals("Perfil"))
                    intent = new Intent(EditEvento.this, EditarPerfil.class);
                else if (opciones[arg2].equals("Publicar"))
                    intent = new Intent(EditEvento.this, CrearEvento.class);
                else
                    intent = new Intent(EditEvento.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                drawerLayout.closeDrawers();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);

        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        IntentExtras = this.getIntent();

        ////////////////////////////////////////////////////////

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
                R.layout.text_spinner2, listcategorias);
        dataAdapterfield.setDropDownViewResource(R.layout.text_spinner2);
        categoria.setAdapter(dataAdapterfield);

        titulo.setText(getIntent().getExtras().getString("name"));

        String micategoria = getIntent().getExtras().getString("categorias");
        micategoria = micategoria.replaceAll("\\s","");
        categoria.setSelection(listcategorias.indexOf(micategoria));
        lugar.setText(getIntent().getExtras().getString("city"));

        String fecha_string = getIntent().getExtras().getString("fecha");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = formatter.parse(fecha_string);
            calendar.setDate(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String cosa = getIntent().getExtras().getString("asientos");
        String[] trozos = cosa.split("/");
        DISPONIBLES = Integer.parseInt(trozos[0]);
        MAX_PERSONAS  = Integer.parseInt(trozos[1]);
        OCUPADOS = MAX_PERSONAS - DISPONIBLES;
        n_personas.setText(trozos[1]);
        descripcion.setText(getIntent().getExtras().getString("description"));

        //EDITAR EVENTO
        findViewById(R.id.buttonUpdateEvento).setOnClickListener(new View.OnClickListener() {
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

                int max = Integer.parseInt(n_personas.getText().toString());
                int calc = MAX_PERSONAS - max;
                if(calc>=0 && calc>DISPONIBLES) {
                    n_personas.setError("¡Ten en cuenta que ya hay " + OCUPADOS + " huecos ocupados!");
                    return;
                }


                EditarEvento edit = (EditarEvento) new EditarEvento().execute();

                Intent intent = new Intent(EditEvento.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private class EditarEvento extends AsyncTask<Void, Void,Conference>
    {

        public EditarEvento() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Conference doInBackground(Void ... unused)
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
                //com.appspot.ad_meet.conference.Conference.CreateConference crear = ConferenceUtils.crearEvento(form);
                //evento = crear.execute();
                String websafeKey = EditEvento.this.getIntent().getExtras().getString("websafeKey");
                com.appspot.ad_meet.conference.Conference.UpdateConference up = ConferenceUtils.actualizarConferencia(websafeKey,form);
                evento = up.execute();


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

    public void onBackPressed() {
        Intent intent = new Intent(EditEvento.this, Evento.class);
        intent.putExtras(IntentExtras.getExtras());
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
