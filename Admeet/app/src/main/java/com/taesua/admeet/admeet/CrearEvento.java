package com.taesua.admeet.admeet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.api.client.util.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import conference.Conference;
import conference.model.ConferenceForm;
import conference.model.Profile;
import conference.model.ProfileForm;


public class CrearEvento extends ActionBarActivity {

    private EditText titulo;
    private Spinner categoria;
    private EditText lugar;
    private CalendarView calendar;
    private EditText n_personas;
    private EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearevento);

        titulo = (EditText)findViewById(R.id.editTextTitulo);
        categoria = (Spinner)findViewById(R.id.spinnerCategoria);
        lugar = (EditText)findViewById(R.id.editTextLugar);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        n_personas = (EditText)findViewById(R.id.editTextNPersonas);
        descripcion = (EditText)findViewById(R.id.editTextDescripcion);

        final List<String> listcategorias = new ArrayList<String>();
        listcategorias.add("Deportes");
        listcategorias.add("Juegos de mesa");
        listcategorias.add("Cine");
        listcategorias.add("Teatro");


        ArrayAdapter<String> dataAdapterfield = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listcategorias);
        dataAdapterfield.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(dataAdapterfield);



        //BOTON VOLVER A EVENTOS
        Button b = (Button) findViewById(R.id.buttonAnuncios);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearEvento.this, Eventos.class);
                startActivity(intent);
            }
        });

        //PARA IR A PERFIL
        findViewById(R.id.buttonPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(CrearEvento.this,Perfil.class);
                startActivity(ji);
            }
        });

        Button x = (Button) findViewById(R.id.buttonPublicar);
        x.setTypeface(null, Typeface.BOLD);


        //PUBLICAR ANUNCIO
        findViewById(R.id.buttonAnadirAnuncio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicarEvento publicarEvento = (PublicarEvento) new PublicarEvento().execute();
                Intent intent = new Intent(CrearEvento.this,Eventos.class);
                startActivity(intent);
            }
        });


    }


    private class PublicarEvento extends AsyncTask<Void, Void,conference.model.Conference>
    {

        public PublicarEvento() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected conference.model.Conference doInBackground(Void ... unused)
        {
            conference.model.Conference evento = null;
            try
            {
                /*
                ProfileForm form = new ProfileForm();
                form.setDisplayName(nombre.getText().toString());
                form.setCiudad(ciudad.getText().toString());
                form.setTelefono(tlf.getText().toString());
                Conference.SaveProfile prof = ConferenceUtils.saveProfile(form);
                perfil = prof.execute();
                */


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
        protected void onPostExecute(conference.model.Conference result)
        {
        }
    }
}
