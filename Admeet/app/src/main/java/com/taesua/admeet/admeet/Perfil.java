package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import conference.Conference;
import conference.model.ConferenceCollection;
import conference.model.Profile;

/**
 * Created by Hector on 14/04/2015.
 */
public class Perfil extends ActionBarActivity {

    private TextView nombre;
    private TextView ciudad;
    private TextView tlf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //BOTON VOLVER A EVENTOS
        Button b = (Button) findViewById(R.id.buttonAnuncios);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, Eventos.class);
                startActivity(intent);
            }
        });


        //BOTON EDITAR PERFIL
        Button e = (Button) findViewById(R.id.buttonEditarPerfil);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, EditarPerfil.class);
                intent.putExtra("nombre",nombre.getText().toString());
                intent.putExtra("ciudad",ciudad.getText().toString());
                intent.putExtra("tlf",tlf.getText().toString());
                startActivity(intent);
            }
        });

        Button p = (Button) findViewById(R.id.buttonPerfil);
        p.setTextColor(Color.parseColor("#000000"));

        nombre = (TextView) findViewById(R.id.textViewNombre);
        ciudad = (TextView) findViewById(R.id.textViewCiudad);
        tlf = (TextView) findViewById(R.id.textViewTlf);



        GetPerfil getperfil = (GetPerfil) new GetPerfil().execute();
    }

    /**
     * Ver todos los eventos
     */
    private class GetPerfil extends AsyncTask<Void, Void,Profile>
    {
        private ProgressDialog pd;

        public GetPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(Perfil.this);
            pd.setMessage("Cargando información de perfil...");
            pd.show();
        }
        @Override
        protected Profile doInBackground(Void ... unused)
        {
            Profile perfil = null;
            try
            {
                // ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
                //Eventos.get.getIntent();
                //if(this.getIntent())



                //Conference.QueryConferences create = ConferenceUtils.getEventos(query);
                Conference.GetProfile prof = ConferenceUtils.getProfile();

                perfil = prof.execute();
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

            return perfil;
        }

        @Override
        protected void onPostExecute(Profile result)
        {
            //Clear the progress dialog and the fields
            pd.dismiss();
            nombre.setText(result.getDisplayName());
            ciudad.setText(result.getCiudad());
            tlf.setText(result.getTelefono());

            //Display success message to user
            Toast.makeText(getBaseContext(), "Información de perfil cargada correctamente",
                    Toast.LENGTH_SHORT).show();
            /*
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
            */

        }
    }
}
