package com.taesua.admeet.admeet;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import conference.Conference;
import conference.model.ConferenceCollection;
import conference.model.ConferenceQueryForm;
import conference.model.Profile;
import conference.model.ProfileForm;

/**
 * Created by Hector on 14/04/2015.
 */
public class EditarPerfil extends ActionBarActivity {

    private EditText nombre;
    private TextView nombreNick;
    private EditText ciudad;
    private EditText tlf;

    private ListView eventos;
    private List<conference.model.Conference> listaeventos = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        eventos = (ListView)findViewById(R.id.listViewEv);

        Button evento = (Button) findViewById(R.id.buttonAnuncios);
        Button perfil = (Button) findViewById(R.id.buttonPerfil);
        Button publicar = (Button) findViewById(R.id.buttonPublicar);
//        perfil.setBackground(getDrawable(R.drawable.bordeazulseleccionado));
  //      evento.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));
    //    publicar.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));

        System.out.println("HA ENTRADO EN EDITARPERFIL!!!!!!!!!!!!!!!!!!!");
        //BOTON VOLVER A EVENTOS
        Button b = (Button) findViewById(R.id.buttonAnuncios);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, Eventos.class);
                startActivity(intent);
            }
        });


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
                    Intent intent = new Intent(EditarPerfil.this, Perfil.class);
                    intent.putExtra("editado", true);
                    startActivity(intent);
                }
            }
        });

        Button p = (Button) findViewById(R.id.buttonPerfil);
        p.setTextColor(Color.parseColor("#000000"));

        nombreNick = (TextView) findViewById(R.id.textViewNombreNick);
        nombre = (EditText) findViewById(R.id.editTextName);
        ciudad = (EditText) findViewById(R.id.editTextCity);
        tlf = (EditText) findViewById(R.id.editTextPhone);

//        nombreNick.setText(this.getIntent().getExtras().getString("nombre"));
        nombre.setText(this.getIntent().getExtras().getString("nombre"));
        ciudad.setText(this.getIntent().getExtras().getString("ciudad"));
        tlf.setText(this.getIntent().getExtras().getString("tlf"));

        tlf.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Ver todos los eventos
     */
    private class GuardarPerfil extends AsyncTask<Void, Void,Profile>
    {

        public GuardarPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
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
        adapter = new EventosAdapter(EditarPerfil.this, nombres, categorias, asis, maxasis, fecha);
        // Asignamos el Adapter al ListView, en este punto hacemos que el
        // ListView muestre los datos que queremos.
        eventos.setAdapter(adapter);
    }


}
